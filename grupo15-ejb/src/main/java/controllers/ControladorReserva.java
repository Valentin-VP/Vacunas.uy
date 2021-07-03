package controllers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtReserva;
import datatypes.DtReservaCompleto;
import datatypes.DtTareaNotificacion;
import datatypes.DtUsuarioExterno;
import datatypes.DtVacunatorio;
import datatypes.EstadoReserva;
import entities.Agenda;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Enfermedad;
import entities.Etapa;
import entities.PlanVacunacion;
import entities.Puesto;
import entities.ReglasCupos;
import entities.Reserva;
import entities.Vacunatorio;
import exceptions.AccionInvalida;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IReservaDAOLocal;
import interfaces.IReservaDAORemote;
import persistence.EtapaID;

/**
 * Session Bean implementation class ControladorReserva
 */

//TODO:No estoy considerando Etapa y Usuario, ni Puesto (y soy un Tipo Asociativo) => TODO: cambiar nombreUser por la entidad
/*
 * TODO:Constancia puede modificarse a si mismo, debe agregarse 1 reserva, puede
 * agregar (o modificar) * lotedosis. No puede crearse, ni destruirse (por si
 * propio), porque dejaria huerfano a su link actual con Certificado.
 * 
 * Agenda puede modificarse a si mismo, puede agregar (o modificar/eliminar) *
 * reservas, puede agregar (o modificar/eliminar) * cupos. No puede crearse, ni
 * destruirse (por si propio), porque dejaria huerfano a su link actual con
 * Vacunatorio. [Si un Vacunatorio lo destruye, se debera destruir los cupos
 * asociados a la Agenda]
 * 
 * Certificado no puede modificarse a si mismo, puede agregar (o
 * modificar/eliminar) * Constancias. No puede crearse, ni destruirse (por si
 * propio), porque dejaria huerfano a su link actual con Usuario. [Si un Usuario
 * lo destruye, se debera destruir las Constancias asociados al Certificado]
 * 
 * Reserva puede modificarse a si mismo, puede agregar (o modificar/eliminar) 1
 * puesto. No puede crearse, ni destruirse (por si propio), porque es un tipo
 * asociativo.
 * 
 * Cupo puede modificarse a si mismo. No puede crearse, ni destruirse (por si
 * propio), porque dejaria huerfano a su link actual con Agenda.
 */

@Stateless
@LocalBean
public class ControladorReserva implements IReservaDAORemote, IReservaDAOLocal {

	@PersistenceContext(name = "test")
	private EntityManager em;

	/**
	 * Default constructor.
	 */

	public ControladorReserva() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<DtPlanVacunacion> seleccionarEnfermedad(String enfermedad) throws PlanVacunacionInexistente, EnfermedadInexistente {
		if (em.find(Enfermedad.class, enfermedad)==null)
			throw new EnfermedadInexistente("No existe esa enfermedad.");
		Query query = em.createQuery("SELECT p FROM PlanVacunacion p");
		@SuppressWarnings("unchecked")
		List<PlanVacunacion> pVacs = query.getResultList();
		if (!pVacs.isEmpty()) {
			ArrayList<DtPlanVacunacion> dtPlanVacs = new ArrayList<>();
			for (PlanVacunacion pV : pVacs) {
				if (pV.getEnfermedad()!=null && pV.getEnfermedad().equals(em.find(Enfermedad.class, enfermedad)))
					dtPlanVacs.add(pV.toDtPlanVacunacion());
			}
			return dtPlanVacs;
		} else {
			throw new PlanVacunacionInexistente("No existen planes de vacunacion registrados para esa enfermedad.");
		}
	}

	
	public ArrayList<DtEtapa> seleccionarPlanVacunacion(int idPlan, int idUser, DtUsuarioExterno datosExternosUsuario) throws PlanVacunacionInexistente, EtapaInexistente, UsuarioInexistente{
		ArrayList<DtEtapa> retorno = new ArrayList<DtEtapa>();
		PlanVacunacion pv = em.find(PlanVacunacion.class, idPlan);
		if (pv == null)
			throw new PlanVacunacionInexistente("No existe ese plan de vacunación.");
		else {
			if (pv.getEtapas().isEmpty())
				throw new EtapaInexistente("No hay etapas de vacunación asociadas a ese plan.");
			else {
				for (Etapa e: pv.getEtapas()) {
					retorno.add(e.toDtEtapa());
				}
				Ciudadano c = em.find(Ciudadano.class, idUser);
				if (c==null) {
					throw new UsuarioInexistente("El usuario no existe.");
				}else {
					Etapa etapaSelect = getEtapaHabilitadoDePlan(pv, c, datosExternosUsuario);
					if (etapaSelect == null) {
						throw new EtapaInexistente("No hay una etapa a la cual el usuario pueda registrarse.");
					}
					//for (DtEtapa e: retorno) {
						for (Reserva r: c.getReservas()) {
							//Si ya tuviese una reserva asociada a una etapa que contuviese un plan con la misma enfernedad que el nuevo, NO.
							if (r.getEtapa().getPlanVacunacion().getEnfermedad().getNombre()==pv.getEnfermedad().getNombre()) {
								throw new EtapaInexistente("El usuario ya posee una reserva para esa enfermedad.");
							}
						}
					//}
					
				}
				
				
			}
		}
		return new ArrayList<DtEtapa>();
	}
	
	public ArrayList<String> seleccionarFecha(LocalDate fecha, String idVacunatorio, int idPlan, int idCiudadano, DtUsuarioExterno datosExternosUsuario) throws VacunatorioNoCargadoException, PlanVacunacionInexistente, UsuarioInexistente, EtapaInexistente, CupoInexistente{
		ArrayList<LocalTime> libresFinal = new ArrayList<LocalTime>();
		Vacunatorio v = em.find(Vacunatorio.class, idVacunatorio);
		if (v == null)
			throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
		else {
			PlanVacunacion pv = em.find(PlanVacunacion.class, idPlan);
			if (pv == null)
				throw new PlanVacunacionInexistente("No existe ese plan.");
			else {
				if (pv.getEtapas().isEmpty())
					throw new EtapaInexistente("No hay etapas de vacunación asociadas a ese plan.");
				else {
					Ciudadano c = em.find(Ciudadano.class, idCiudadano);
					if (c == null) {
						throw new UsuarioInexistente("El usuario seleccionado no existe.");
					} else {
						Etapa etapaSelect = getEtapaHabilitadoDePlan(pv, c, datosExternosUsuario);
						if (etapaSelect == null) {
							throw new EtapaInexistente("No hay una etapa a la cual el usuario pueda registrarse.");
						}
						if (!fecha.isAfter(LocalDate.now())) {
							throw new CupoInexistente("Fecha incorrecta.");
						}
						int cantReservasNecesarias = etapaSelect.getVacuna().getCantDosis();
						int diasEntreDosis = etapaSelect.getVacuna().getTiempoEntreDosis();
						ArrayList<ArrayList<LocalTime>> libres = new ArrayList<ArrayList<LocalTime>>(cantReservasNecesarias);
						
						ArrayList<Agenda> agendas = new ArrayList<Agenda>();
						for (int i=0; i<cantReservasNecesarias; i++) {
							Agenda a = getAgendaFecha(v, fecha.plusDays(diasEntreDosis*i));
							if (a==null)
								a = new Agenda(fecha.plusDays(diasEntreDosis*i));
							agendas.add(a);
							//libres.add(calcularHorasSegunReglas(v.getReglasCupos(), fecha.plusDays(diasEntreDosis*i)));
							libres.add(calcularHorasLibresVacunatorio(a, v));
							//ListIterator<ArrayList<LocalTime>> iter = libres.listIterator();
							//if (iter.hasNext())
							//	iter.next();
							//iter.set(calcularHorasSegunReglas(v.getReglasCupos(), fecha.plusDays(diasEntreDosis*i)));
							
						}
						libresFinal = calcularHorasLibresVariasAgendas(libres);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						ArrayList<String> retorno = new ArrayList<String>();
						for (LocalTime lt: libresFinal) {
							retorno.add(lt.format(formatter));
						}
						return retorno;
						//intento tomar una agenda con esa fecha en ese vacunatorio, si no existe, devuelvo todas las horas posteriores a NOW
						/*Agenda a = getAgendaFecha(v, fecha);
						if (a==null) {
							//a = new Agenda(fecha);
			        		//v.getAgenda().add(a);
			        		//a.setVacunatorio(v);
			        		//em.merge(v);
			        		//em.persist(a);			
							
							libres = calcularHorasSegunReglas(v.getReglasCupos(), fecha);
							//return libres;
							
							///////PRUEBA///////
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
							ArrayList<String> retorno = new ArrayList<String>();
							for (LocalTime lt: libres) {
								retorno.add(lt.format(formatter));
							}
							return retorno;

						}else {
							libres = calcularHorasLibresVacunatorio(a, v);
							//return libres;
							
							
							
							
							///////PRUEBA///////
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
							ArrayList<String> retorno = new ArrayList<String>();
							for (LocalTime lt: libres) {
								retorno.add(lt.format(formatter));
							}
							return retorno;
							
							/*libres = calcularHorasSegunReglas(v.getReglasCupos());
							retorno.addAll(libres);
							
							for (LocalTime lt: libres) {
								ArrayList<Reserva> reservas = getReservasTurno(a, lt);
								if (reservas.size() >= v.getPuesto().size()) { //si tengo menos reservas a esa hora que puestos en el vacunatorio, tengo lugar
									retorno.remove(lt);
								}
								/*if (reservas.size() < v.getPuesto().size()) { //si tengo menos reservas a esa hora que puestos en el vacunatorio, tengo lugar
									Puesto p = encontrarPuestoLibre(reservas, v.getPuesto());
									if (p==null) //no deberia pasar
										retorno.remove(lt);
								}else {
									retorno.remove(lt);
								}
							}
							return retorno;
						}*/
					
					}
				}
			}
		}
	}
	// TODO: controlar stock, y condiciones de etapa
	// devolver DtN
	public ArrayList<DtTareaNotificacion> confirmarReserva(int idCiudadano, String idEnfermedad, int idPlan, String idVacunatorio,
			LocalDate fecha, LocalTime hora, DtUsuarioExterno datosExternosUsuario)
			throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente, EtapaInexistente {
		Ciudadano c = em.find(Ciudadano.class, idCiudadano);
		if (c == null) {
			throw new UsuarioInexistente("El usuario seleccionado no existe.");
		} else {
			Enfermedad tvirus = em.find(Enfermedad.class, idEnfermedad);
			if (tvirus == null)
				throw new EnfermedadInexistente("No existe esa enfermedad.");
			else {
				PlanVacunacion pv = em.find(PlanVacunacion.class, idPlan);
				if (pv == null )
					throw new PlanVacunacionInexistente("No existe ese plan.");
				else {
					if (!pv.getEnfermedad().getNombre().equals(tvirus.getNombre())) {
						throw new EnfermedadInexistente("No parece que esa enfermedad corresponda a ese plan.");
					}else {
						Vacunatorio v = em.find(Vacunatorio.class, idVacunatorio);
						if (v == null)
							throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
						else {
							if (pv.getEtapas().isEmpty())
								throw new EtapaInexistente("No hay etapas de vacunación asociadas a ese plan.");
							else {
								if (!fecha.isAfter(LocalDate.now())) {
									throw new CupoInexistente("Fecha incorrecta.");
								}
								//Primero compruebo el pipe de condicion que tiene las edades, y el comienzo y fin de la etapa. Si dio bien, me traigo una etapa.
								Etapa etapaSelect = getEtapaHabilitadoDePlan(pv, c, datosExternosUsuario);
								if (etapaSelect == null) {
									throw new EtapaInexistente("No hay una etapa a la cual el usuario pueda registrarse.");
								}
								//Sin embargo, si ya estoy registrado para esa enfermedad, no deberia poder registrarme de nuevo
								for (Reserva r: c.getReservas()) {
									if (r.getEtapa().getPlanVacunacion().getEnfermedad().getNombre()==pv.getEnfermedad().getNombre()) {
										throw new EtapaInexistente("El usuario ya posee una reserva para esa enfermedad.");
									}
									if (r.getFechaRegistro().toLocalTime().equals(hora)) {
										throw new CupoInexistente("El usuario ya posee una reserva para esa hora para la vacuna " + r.getEtapa().getVacuna().getNombre());
									}
								}
								//creo lista de horas libres considerando todas las inyecciones que me tengo que dar
								ArrayList<LocalTime> libresFinal = new ArrayList<LocalTime>();
								int cantReservasNecesarias = etapaSelect.getVacuna().getCantDosis();
								int diasEntreDosis = etapaSelect.getVacuna().getTiempoEntreDosis();
								ArrayList<ArrayList<LocalTime>> libres = new ArrayList<ArrayList<LocalTime>>(cantReservasNecesarias);
								
								//creo lista de agendas para cada una de esas fechas que tengo que ir
								ArrayList<Agenda> agendas = new ArrayList<Agenda>();
								for (int i=0; i<cantReservasNecesarias; i++) {
									//busco una Agenda para cada fecha de la vacuna, si no existe la creo
									Agenda a = getAgendaFecha(v, fecha.plusDays(diasEntreDosis*i));
									if (a==null)
										a = new Agenda(fecha.plusDays(diasEntreDosis*i));
									agendas.add(a);
									//calculo horas libres para cada una de las agendas (dias que debo ir para esa vacuna)
									libres.add(calcularHorasLibresVacunatorio(a, v));
								}
								//combino esa lista de horas libres de todos los dias que tengo que ir
								libresFinal = calcularHorasLibresVariasAgendas(libres);
								/*Agenda a = getAgendaFecha(v, fecha);
								if (a==null) {
									a = new Agenda(fecha);
					        		
					        		//em.merge(v);
					        		//em.persist(a);
					        		
								}
								ArrayList<LocalTime> libres = calcularHorasLibresVacunatorio(a, v);*/
								if (!contieneLocalTime(libresFinal, hora)) {
									//System.out.println("Corto por contieneLocalTime.");
									throw new CupoInexistente("No hay cupos para esa hora.");
								}
								ArrayList<DtTareaNotificacion> retorno = new ArrayList<>();
								for (Agenda a: agendas) {
									//para cada agenda (dia), veo que puesto me toca en esa hora
									ArrayList<Reserva> reservas = getReservasTurno(a, hora);
									if (reservas.size() < v.getPuesto().size()) { //si hay menos reservas para esa hora que puestos en el vacunatorio, entonces tengo lugar
										Puesto p = encontrarPuestoLibre(reservas, v.getPuesto());
										if (p==null) //no deberia pasar porque sino estoy buscando mal las horas libres en la funcion anterior
											throw new CupoInexistente("No hay cupos para esa hora.");
										v.getAgenda().add(a);
						        		a.setVacunatorio(v);
						        		//creo una reserva con la hora que me pasaron y la fecha de esa agenda (dia) para cada dosis
										Reserva r = new Reserva(LocalDateTime.of(a.getFecha(), hora), EstadoReserva.EnProceso, etapaSelect, c, p);
										a.getReservas().add(r);
										c.getReservas().add(r);
										em.merge(c);
										em.merge(v);
										em.merge(r);
										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
										DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
										retorno.add(new DtTareaNotificacion(p.getId(), null, v.getNombre(), a.getFecha().format(formatter), hora.format(formatter2)));
									}else {
										throw new CupoInexistente("No hay cupos para esa hora.");
									}
								}
								return retorno;
							}
							// si cuento la cantidad de reservas que apuntan a un puesto, y ese numero es
							// mayor que los turnos diarios del vacunatorio, no puedo reservar para ese
							// puesto, debo buscar otro
							// hora x, busca hora x en reservas, si cantidad reservas = cantidad puestos,
							// DENIED, sino, recorro puestos a encontrar uno libre.
							// si no hay agenda, la creo
						}
					}
				}
			}
		}
		
		
	}
	
	public DtReservaCompleto obtenerReserva(int ciudadano, int plan, int etapa, LocalDateTime fecha) throws ReservaInexistente, UsuarioInexistente, EtapaInexistente {
		Ciudadano c = em.find(Ciudadano.class, ciudadano);
		if (c == null) {
			throw new UsuarioInexistente("El usuario seleccionado no existe.");
		} else {
			/*Enfermedad e = em.find(Enfermedad.class, enfermedad);
			if (e == null) {
				throw new EnfermedadInexistente("La enfermedad seleccionada no existe.");
			} else {
				Reserva r = getReservaCiudadanoDeEnfermedadEnFecha(c.getReservas(), enfermedad, fecha);//getReservaEtapa(c.getReservas(), e, fecha);
			}*/
			Etapa e = em.find(Etapa.class, new EtapaID(etapa, plan));
			if (e == null) {
				throw new EtapaInexistente("La etapa seleccionada no existe.");
			} else {
				Reserva r = getReservaEtapa(c.getReservas(), e, fecha);
				if (r != null && r.getEstado().equals(EstadoReserva.EnProceso)) {
					DtReservaCompleto temp = r.getDtReservaCompleto();
					temp.setPuesto(r.getPuesto().getVacunatorio().getNombre() + " - Puesto " + r.getPuesto().getId());
					DtReservaCompleto retorno = temp;

					return retorno;
				} else {
					throw new ReservaInexistente("No existe esa reserva.");
				}
			}
		}
	}
	
	public ArrayList<DtReservaCompleto> listarReservasCiudadano(int ciudadano) throws ReservaInexistente, UsuarioInexistente {
		Ciudadano c = em.find(Ciudadano.class, ciudadano);
		if (c == null) {
			throw new UsuarioInexistente("El usuario seleccionado no existe.");
		} else {
			if (c.getReservas().isEmpty()) {
				throw new ReservaInexistente("No hay reservas.");
			}else {
				ArrayList<DtReservaCompleto> retorno = new ArrayList<DtReservaCompleto>();
				for (Reserva r: c.getReservas()) {
					if (r.getEstado().equals(EstadoReserva.EnProceso)) {
						DtReservaCompleto temp = r.getDtReservaCompleto();
						temp.setPuesto(r.getPuesto().getVacunatorio().getNombre() + " - Puesto " + r.getPuesto().getId());
						retorno.add(temp);
					}
				}
				if (retorno.isEmpty()) {
					throw new ReservaInexistente("No hay reservas.");
				}else {
					return retorno;
				}
			}
		}
	}
	
	public void eliminarReserva(int ciudadano, LocalDateTime fecha, String enfermedad) throws ReservaInexistente, UsuarioInexistente, EnfermedadInexistente {
		Ciudadano c = em.find(Ciudadano.class, ciudadano);
		if (c == null) {
			throw new UsuarioInexistente("El usuario seleccionado no existe.");
		} else {
			Enfermedad e = em.find(Enfermedad.class, enfermedad);
			if (e == null) {
				throw new EnfermedadInexistente("La enfermedad seleccionada no existe.");
			} else {
				Reserva r = getReservaCiudadanoDeEnfermedadEnFecha(c.getReservas(), enfermedad, fecha);//getReservaEtapa(c.getReservas(), e, fecha);
				if (r != null) {
					c.getReservas().remove(r);
					
					//recorrer vacunatorios y sus agendas para eliminarle esa reserva al que la tenga
					Query query = em.createQuery("SELECT v FROM Vacunatorio v");
					@SuppressWarnings("unchecked")
					List<Vacunatorio> aux = query.getResultList();
					for (Vacunatorio v: aux) {
						boolean salgo = false;
						if (!salgo) {
							Agenda a = getAgendaFecha(v, fecha.toLocalDate());
							if (a!=null) {
								for (Reserva reservaAComparar: a.getReservas()) {
									if (reservaAComparar.equals(r)) {
										a.getReservas().remove(reservaAComparar);
										em.merge(reservaAComparar);
										salgo = true;
										break;
									}
								}	
							}
						}
					}
					em.merge(c);
					em.remove(r);
				} else {
					throw new ReservaInexistente("No existe esa reserva.");
				}
			}
		}
	}
	
	public void cambiarEstadoReserva(int idCiudadano, LocalDateTime fecha, EstadoReserva estado) throws AccionInvalida {
		Boolean bien = false;
    	Ciudadano c = em.find(Ciudadano.class, idCiudadano);
		if (c != null) {
			List<Reserva> lr= c.getReservas();
			if (!lr.isEmpty()) {
				for (Reserva r: lr) {
					if (r.getEstado().equals(EstadoReserva.EnProceso) && r.getFechaRegistro().isEqual(fecha)) { //&& r.getEtapa().getPlanVacunacion().getEnfermedad().getNombre().equals(idEnfermedad)) {
						r.setEstado(estado);
						em.merge(r);
						if (estado.equals(EstadoReserva.Completada)) {
							boolean habiaConstancia = false;
							for (ConstanciaVacuna cv: c.getCertificado().getConstancias()) { //busco si existe una constancia con esa vacuna
								if (cv.getVacuna().equals(r.getEtapa().getVacuna().getNombre())) { //si existe seteo los datos, significa que tenia mas de una dosis
									cv.setDosisRecibidas(cv.getDosisRecibidas()+1);
									cv.setFechaUltimaDosis(r.getFechaRegistro().toLocalDate());
									if (r.getEtapa().getVacuna().getCantDosis()==cv.getDosisRecibidas()) {
										cv.setPeriodoInmunidad(r.getEtapa().getVacuna().getExpira());
									}else {
										cv.setPeriodoInmunidad(0); //no deberia ser necesario porque ya deberia haber sido creado con este valor en 0
									}
									em.merge(cv);
									habiaConstancia = true;
									break;
								}
							}
							if (!habiaConstancia) { //si no encontre constancia, es porque es la primera dosis, o es una sola dosis
								if (r.getEtapa().getVacuna().getCantDosis()>1) { //si es de multiples dosis el tiempo de inmunidad es 0 meses
									ConstanciaVacuna cv =new ConstanciaVacuna(0, 1, r.getFechaRegistro().toLocalDate(), r.getEtapa().getVacuna().getNombre(), r);
									c.getCertificado().getConstancias().add(cv);
									em.persist(cv);
									em.merge(c.getCertificado());
								}else {	//si es de una dosis, la inmunidad va directo de la vacuna
									ConstanciaVacuna cv =new ConstanciaVacuna(r.getEtapa().getVacuna().getExpira(), 1, r.getFechaRegistro().toLocalDate(), r.getEtapa().getVacuna().getNombre(), r);
									c.getCertificado().getConstancias().add(cv);
									em.persist(cv);
									em.merge(c.getCertificado());
								}
							}
						}
						bien = true;
						break;
					}
				}
				if (!bien) {
					throw new AccionInvalida("No se puede cambiar esa reserva. Contacte al administrador del sistema.");
				}
			}else {
				throw new AccionInvalida("El ciudadano no tiene reservas. Contacte al administrador del sistema.");
			}
		}else {
			throw new AccionInvalida("El ciudadano no existe. Contacte al administrador del sistema.");
		}
	}
	// enfermedad => plan, vacunatorio, fecha => horas libres
/*
	public void agregarReserva(int ciudadano, int etapa, String puesto, LocalDateTime fecha, EstadoReserva estado)
			throws ReservaRepetida, PuestoNoCargadoException, ReservaInexistente, UsuarioExistente {
		Ciudadano c = em.find(Ciudadano.class, ciudadano);
		if (c == null) {
			throw new UsuarioExistente("El usuario seleccionado no existe.");
		} else {
			Etapa e = em.find(Etapa.class, etapa);
			if (e == null) {
				throw new ReservaInexistente("La etapa seleccionada no existe.");
			} else {
				Puesto p = em.find(Puesto.class, puesto);
				if (p != null) {
					Reserva test = getReservaEtapa(c.getReservas(), e);
					if (test == null) { // si no existe una reserva con el objeto Etapa e (de id etapa)

						Reserva r = new Reserva(fecha, estado, e, c, p);
						c.getReservas().add(r);
						// e.setReservas(e.getReservas().add(r));
						// p.getReservas().add(r);; //puesto setea reserva
						em.persist(c);
						// em.persist(e);
						em.persist(r);
						// em.merge(p);
					} else {
						throw new ReservaRepetida("Ya existe una reserva para esa etapa.");
					}
				} else
					throw new PuestoNoCargadoException("El puesto seleccionado no existe.");
			}

		}
	}

	public void modificarReserva(int ciudadano, int etapa, String puesto, LocalDateTime fecha, EstadoReserva estado)
			throws ReservaInexistente, PuestoNoCargadoException, UsuarioExistente {
		Ciudadano c = em.find(Ciudadano.class, ciudadano);
		if (c == null) {
			throw new UsuarioExistente("El usuario seleccionado no existe.");
		} else {
			Etapa e = em.find(Etapa.class, etapa);
			if (e == null) {
				throw new ReservaInexistente("La etapa seleccionada no existe.");
			} else {
				Puesto p = em.find(Puesto.class, puesto);
				if (p != null) {
					Reserva r = getReservaEtapa(c.getReservas(), e);
					if (r != null) {

						r.setFechaRegistro(fecha);
						r.setEstado(estado);
						// compruebo si el puesto es distinto al anterior
						Puesto oldPuesto = r.getPuesto();
						if (!p.equals(oldPuesto)) {
							// oldPuesto.getReservas().remove(r); //borro reserva de puesto anterior
							r.setPuesto(p);
							// p.getReservas().add(r); //puesto setea reserva
							// em.merge(p);
						}
						em.merge(r);
					} else {
						throw new ReservaInexistente("No hay una reserva para esa etapa.");
					}
				} else {
					throw new PuestoNoCargadoException("No existe un puesto con ese ID.");
				}
			}
		}
	}

	public ArrayList<DtReserva> listarReservasGenerales() throws ReservaInexistente {
		Query query = em.createQuery("SELECT r FROM Reserva r");
		@SuppressWarnings("unchecked")
		ArrayList<Reserva> result = (ArrayList<Reserva>) query.getResultList();
		ArrayList<DtReserva> retorno = new ArrayList<>();
		if (result != null) {
			for (Reserva r : result) {
				retorno.add(new DtReserva(r.getEstado(), getDtUsuario(r.getCiudadano()), r.getFechaRegistro(),
						r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
						r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(),
						r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId()));
			}
			return retorno;
		} else {
			throw new ReservaInexistente("No hay ninguna reserva.");
		}
	}

	private Reserva getReserva(int id) {
		Reserva r = em.find(Reserva.class, id);
		return r;
	}

	private boolean existeReservaEtapa(ArrayList<Reserva> lista, Etapa e) {
		for (Reserva r : lista) {
			if (r.getEtapa().equals(e))
				return true;
		}

		return false;
	}



	private DtCiudadano getDtUsuario(Ciudadano u) {
		if (u != null)
			return new DtCiudadano(u.getIdUsuario(), u.getNombre(), u.getApellido(), u.getFechaNac(), u.getEmail(),
					u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado());
		else
			return null;
	}
*/
	//Devuelvo una Agenda que tenga determinada fecha en un Vacunatorio
	private Agenda getAgendaFecha(Vacunatorio v, LocalDate fecha) {
		Agenda retorno;
		List<Agenda> list = v.getAgenda();
		for (Agenda a : list) {
			if (a.getFecha().isEqual(fecha)) {
				return a;
			}
		}
		return null;
	}

	//me busco todas las reservas de una agenda para determinada hora (recordando que pueden haber X puestos disponibles en cada vacunatorio)
	private ArrayList<Reserva> getReservasTurno(Agenda a, LocalTime hora) {
		ArrayList<Reserva> retorno = new ArrayList<Reserva>();
		List<Reserva> list = a.getReservas();
		for (Reserva r : list) {
			if (r.getFechaRegistro().toLocalTime().equals(hora)) { //hago uso del toLocalTime()
				retorno.add(r);
				//System.out.println("getReservasTurno: A la hora " + hora.toString() + " el dia " + a.getFecha().toString() + " tengo una reserva en el puesto " + r.getPuesto().getId());
			}
		}
		return retorno;
	}

	
	//calculo horas/turnos segun las reglasCupos, no quiero turnos anteriores a NOW, ni turnos que esten demasiado cerca del cierre
	private ArrayList<LocalTime> calcularHorasSegunReglas(ReglasCupos rc, LocalDate f) {
		ArrayList<LocalTime> horas = new ArrayList<LocalTime>();
		Long diferencia = Duration.between(rc.getHoraApertura(), rc.getHoraCierre()).toMinutes();
		int turno = rc.getDuracionTurno();
		int turnosTotales = (int)(diferencia.intValue() / turno); //trunco los decimales (redondea hacia abajo)
		LocalTime hora = LocalTime.from(rc.getHoraApertura());
		if (f.isAfter(LocalDate.now()) || hora.isAfter(LocalTime.now())) //si la fecha es despues de hoy o la hora es despues de ahora, ADD
			horas.add(hora);
		for (int i=1; i<turnosTotales; i++) {
			if (f.isAfter(LocalDate.now()) || hora.plusMinutes(turno*i).isAfter(LocalTime.now())) {
				horas.add(hora.plusMinutes(turno*i));
				//System.out.println("calcularHorasSegunReglas: Agrego hora " + hora.plusMinutes(turno*i).toString() + " el dia " + f.toString());
			}
				
		}
		return horas;
	}
	
	//veo que horas estan libres en una determinada agenda de un vacunatorio
	private ArrayList<LocalTime> calcularHorasLibresVacunatorio(Agenda a, Vacunatorio v) {
		ArrayList<LocalTime> libres = new ArrayList<LocalTime>();
		ArrayList<LocalTime> retorno = new ArrayList<LocalTime>();
		
		//aca veo los horarios puros segun las reglas de ese vacunatorio
		libres = calcularHorasSegunReglas(v.getReglasCupos(), a.getFecha());
		retorno.addAll(libres);
		
		for (LocalTime lt: libres) {
			//System.out.println("calcularHorasLibresVacunatorio: Check hora " + lt.toString() + " de Vacunatorio " + v.getId() + " el dia " + a.getFecha().toString());
			//para cada turno, me fijo que reservas hay en esa agenda
			ArrayList<Reserva> reservas = getReservasTurno(a, lt);
			if (reservas.size() >= v.getPuesto().size()){ //si tengo igual/mas reservas a esa hora que puestos en el vacunatorio, no hay lugar
				//System.out.println("calcularHorasLibresVacunatorio: Borro hora " + lt.toString() + " de Vacunatorio " + v.getId() + " el dia " + a.getFecha().toString());
				retorno.remove(lt);	//saco esa hora (porque no habian puestos libres para el turno)
			}
			/*if (reservas.size() < v.getPuesto().size()) { //si tengo menos reservas a esa hora que puestos en el vacunatorio, tengo lugar
				Puesto p = encontrarPuestoLibre(reservas, v.getPuesto());
				if (p==null) //no deberia pasar
					retorno.remove(lt);
			}else {
				retorno.remove(lt);
			}*/
		}
		return retorno;
	}

	//veo que horas estan libres en una determinada agenda de un vacunatorio
		private ArrayList<LocalTime> calcularHorasLibresVariasAgendas(ArrayList<ArrayList<LocalTime>> horasLibresAgendas) {
			ArrayList<LocalTime> comunes = new ArrayList<LocalTime>();
			comunes.addAll(horasLibresAgendas.get(0));
			for (ListIterator<ArrayList<LocalTime>> iter = horasLibresAgendas.listIterator(0); iter.hasNext(); ) {
				comunes.retainAll(iter.next());
			}
			
			return comunes;
		}
	
	//me encuentro un puesto libre en una lista de reservas para un cierto horario, solo deberia usar esto si ya se que hay un puesto libre
	private Puesto encontrarPuestoLibre(ArrayList<Reserva> reservasHora, List<Puesto> puestosVac) {
		ArrayList<Puesto> temp = new ArrayList<Puesto>();
		for (Reserva r : reservasHora) {
			temp.add(r.getPuesto());
		}
		for (Puesto p : puestosVac) {
			if (!temp.contains(p)) {
				return p;
			}
		}
		return null;
	}
	
	private boolean contieneLocalTime(ArrayList<LocalTime> horas, LocalTime horaComprobada) {
		for (LocalTime h: horas) {
			//System.out.println("contieneLocalTime: Será" + h.toString() + "igual a mi "+ horaComprobada.toString()+"?");
			if (h.equals(horaComprobada)) {
				//System.out.println("contieneLocalTime: "+h.toString() + " era igual a mi "+ horaComprobada.toString());
				return true;
			}
				
		}
		return false;
	}
	
	//veo entre la edad de la etapa (condicion con pipe), y la fecha de inicio/fin de la etapa, si un determinado ciudadano esta habilitado para alguna etapa de un plan
	private Etapa getEtapaHabilitadoDePlan(PlanVacunacion pv, Ciudadano c, DtUsuarioExterno datosExternosUsuario) {
		String[] temp;
		for (Etapa e: pv.getEtapas()) {
			temp = e.getCondicion().split("\\Q|\\E");
			int edadInit = Integer.parseInt(temp[0]);
			int edadFin = Integer.parseInt(temp[1]);
			String sector = temp[2];
			String permitidoEnf = temp[3]; //puede tener enfermedades previas?
			int edad = Period.between(c.getFechaNac(), LocalDate.now()).getYears();
			
			if ((edad >= edadInit && edad <= edadFin)) { //cumple con la edad necesaria?
				//System.out.println("getEtapaHabilitadoDePlan: Edad: " + edad);
				if ( e.getFechaInicio().isBefore(LocalDate.now()) && e.getFechaFin().isAfter(LocalDate.now())){ //esta la etapa en un periodo activo?
					//System.out.println("getEtapaHabilitadoDePlan: Edad: " + edad);
					if (sector.equals("todos")  || datosExternosUsuario.getTipoSector().equals(sector)) { //es el ciudadano del sector correcto (si se requiere)?
						//System.out.println("getEtapaHabilitadoDePlan: Sector Ciudadano: " + datosExternosUsuario.getTipoSector() + " . El necesario es " + sector);
						//System.out.println("getEtapaHabilitadoDePlan: Previas Ciudadano: " + datosExternosUsuario.isEnfermedadesPrevias() + " . Puede? " + permitidoEnf);
						if (permitidoEnf.equals("no")) { //si la etapa no permite enfermedades previas:_
							if (!datosExternosUsuario.isEnfermedadesPrevias()) {                     //no tiene ese ciudadano enfermedades previas?
								return e;	//si todo se cumple retorno esa etapa
							}
						}else {
							return e; //si permite enfermedades previas y llegue aca entonces lo otro se cumple, retorno esa etapa
						}
					}
				}
			}
		}
		return null;
	}
	
	private Reserva getReservaEtapa(List<Reserva> lista, Etapa e, LocalDateTime fecha) {
		for (Reserva r : lista) {
			if (r.getEtapa().equals(e) && r.getFechaRegistro().isEqual(fecha))
				return r;
		}

		return null;
	}
	
	private Reserva getReservaCiudadanoDeEnfermedadEnFecha(List<Reserva> lista, String enfermedad, LocalDateTime fecha) {
		for (Reserva r : lista) {
			if (r.getEtapa().getVacuna().getEnfermedad().getNombre().equals(enfermedad) && r.getFechaRegistro().isEqual(fecha))
				return r;
		}

		return null;
	}
}
