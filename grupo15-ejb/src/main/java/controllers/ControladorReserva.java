package controllers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtEnfermedad;
import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;
import datatypes.DtVacunatorio;
import datatypes.EstadoReserva;
import entities.Agenda;
import entities.Ciudadano;
import entities.Enfermedad;
import entities.Etapa;
import entities.PlanVacunacion;
import entities.Puesto;
import entities.ReglasCupos;
import entities.Reserva;
import entities.Vacunatorio;
import exceptions.CupoInexistente;
import exceptions.EnfermedadInexistente;
import exceptions.EtapaInexistente;
import exceptions.PlanVacunacionInexistente;
import exceptions.UsuarioInexistente;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;
import interfaces.IReservaDAOLocal;
import interfaces.IReservaDAORemote;

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

	//TODO: ELIMINAR ESTO
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente {
		Query query = em.createQuery("SELECT e FROM Enfermedad e ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<Enfermedad> enfermedades = query.getResultList();
		if (!enfermedades.isEmpty()) {
			ArrayList<DtEnfermedad> dtEnfs = new ArrayList<>();
			for (Enfermedad e : enfermedades) {
				dtEnfs.add(new DtEnfermedad(e.getNombre()));
			}
			return dtEnfs;
		} else {
			throw new EnfermedadInexistente("No existen Enfermedades registradas");
		}
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
				if (pV.getEnfermedad().equals(em.find(Enfermedad.class, enfermedad)))
					dtPlanVacs.add(pV.toDtPlanVacunacion());
			}
			return dtPlanVacs;
		} else {
			throw new PlanVacunacionInexistente("No existen planes de vacunacion registrados para esa enfermedad.");
		}
	}

	
	public ArrayList<DtEtapa> seleccionarPlanVacunacion(int idPlan, int idUser) throws PlanVacunacionInexistente, EtapaInexistente{
		ArrayList<DtEtapa> retorno = new ArrayList<DtEtapa>();
		PlanVacunacion pv = em.find(PlanVacunacion.class, idPlan);
		if (pv == null)
			throw new PlanVacunacionInexistente("No existe ese plan de vacunación.");
		else {
			if (pv.getEtapas().isEmpty())
				throw new EtapaInexistente("No hay etapas de vacunación asociadas a ese plan.");
			else {
				for (Etapa e: pv.getEtapas()) {
					retorno.add(new DtEtapa(e.getId(), e.getFechaInicio(), e.getFechaFin(), pv.toDtPlanVacunacion()));
				}
				Ciudadano c = em.find(Ciudadano.class, idUser);
				if (c==null) {
					throw new EtapaInexistente("El usuario no existe.");
				}else {
					for (DtEtapa e: retorno) {
						for (Reserva r: c.getReservas()) {
							if (r.getEtapa().getId()==e.getId()) {
								throw new EtapaInexistente("El usuario ya posee una reserva a ese plan.");
							}
						}
					}
				}
			}
		}
		return retorno;
	}
	
	//TODO: ELIMINAR ESTO
	public ArrayList<DtVacunatorio> listarVacunatorios() throws VacunatoriosNoCargadosException {
		Query query = em.createQuery("SELECT v FROM Vacunatorio v");
		@SuppressWarnings("unchecked")
		List<Vacunatorio> aux = query.getResultList();
		ArrayList<DtVacunatorio> vac = new ArrayList<DtVacunatorio>();
		if (aux.isEmpty()) {
			throw new VacunatoriosNoCargadosException("No existen vacunatorios en el sistema.");
		} else {
			for (Vacunatorio v : aux) {
				DtVacunatorio dtVac = new DtVacunatorio(v.getId(), v.getNombre(), v.getDtDir(), v.getTelefono(),
														v.getLatitud(), v.getLongitud());
				vac.add(dtVac);
			}
			return vac;
		}
	}
	
	public void seleccionarEtapa(int idEtapa) {
		
	}
	
	public ArrayList<LocalTime> seleccionarFecha(LocalDate fecha, String idVacunatorio) throws VacunatorioNoCargadoException{
		ArrayList<LocalTime> libres = new ArrayList<LocalTime>();
		ArrayList<LocalTime> retorno = new ArrayList<LocalTime>();
		Vacunatorio v = em.find(Vacunatorio.class, idVacunatorio);
		if (v == null)
			throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
		else {
			Agenda a = getAgendaFecha(v, fecha);
			if (a==null) {
				a = new Agenda(fecha);
        		v.getAgenda().add(a);
        		a.setVacunatorio(v);
        		em.merge(v);
        		//em.persist(a);
			}
			libres = calcularHorasSegunReglas(v.getReglasCupos());
			retorno.addAll(libres);
			
			for (LocalTime lt: libres) {
				ArrayList<Reserva> reservas = getReservasTurno(a, lt);
				if (reservas.size() < v.getPuesto().size()) { //si tengo menos reservas a esa hora que puestos en el vacunatorio, tengo lugar
					Puesto p = encontrarPuestoLibre(reservas, v.getPuesto());
					if (p==null) //no deberia pasar
						retorno.remove(lt);
				}else {
					retorno.remove(lt);
				}
			}
			return retorno;
		}
	}
	// TODO: controlar stock, y condiciones de etapa
	public void confirmarReserva(int idCiudadano, String idEnfermedad, int idPlan, String idVacunatorio,
			LocalDate fecha, LocalTime hora)
			throws UsuarioInexistente, PlanVacunacionInexistente, VacunatorioNoCargadoException, EnfermedadInexistente, CupoInexistente {
		Ciudadano c = em.find(Ciudadano.class, idCiudadano);
		if (c == null) {
			throw new UsuarioInexistente("El usuario seleccionado no existe.");
		} else {
			Enfermedad tvirus = em.find(Enfermedad.class, idEnfermedad);
			if (tvirus == null)
				throw new EnfermedadInexistente("No existe esa enfermedad.");
			else {
				PlanVacunacion pv = em.find(PlanVacunacion.class, idPlan);
				if (pv == null)
					throw new PlanVacunacionInexistente("No existe ese plan.");
				else {
					Vacunatorio v = em.find(Vacunatorio.class, idVacunatorio);
					if (v == null)
						throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
					else {
						
						Etapa e = pv.getEtapas().get(0); // cambiar esto por condiciones
						//Controlar el tema de las dosis (tambien agregar atributo tiempo en Vacuna)
						for (Reserva r: c.getReservas()) {
							if (r.getEtapa().getId()==e.getId()) {
								throw new CupoInexistente("El usuario ya posee una reserva a ese plan.");
							}
						}
						Agenda a = getAgendaFecha(v, fecha);
						if (a==null) {
							a = new Agenda(fecha);
			        		v.getAgenda().add(a);
			        		a.setVacunatorio(v);
			        		//em.merge(v);
			        		//em.persist(a);
			        		
						}
						
						ArrayList<Reserva> reservas = getReservasTurno(a, hora);
						if (reservas.size() < v.getPuesto().size()) { //si hay menos reservas para esa hora que puestos en el vacunatorio, entonces tengo lugar
							Puesto p = encontrarPuestoLibre(reservas, v.getPuesto());
							if (p==null) //no deberia pasar
								throw new CupoInexistente("No hay cupos para esa hora.");
							Reserva r = new Reserva(LocalDateTime.of(fecha, hora), EstadoReserva.EnProceso, e, c, p);
							a.getReservas().add(r);
							c.getReservas().add(r);
							em.merge(c);
							em.merge(v);
							em.merge(r);
						}else {
							throw new CupoInexistente("No hay cupos para esa hora.");
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

	public void eliminarReserva(int ciudadano, int etapa) throws ReservaInexistente, UsuarioExistente {
		Ciudadano c = em.find(Ciudadano.class, ciudadano);
		if (c == null) {
			throw new UsuarioExistente("El usuario seleccionado no existe.");
		} else {
			Etapa e = em.find(Etapa.class, etapa);
			if (e == null) {
				throw new ReservaInexistente("La etapa seleccionada no existe.");
			} else {
				Reserva r = getReservaEtapa(c.getReservas(), e);
				if (r != null) {
					Puesto p = r.getPuesto();
					c.getReservas().remove(r);
					// e.getReservas().remove(r);
					// p.getReservas().remove(r);
					em.merge(c);
					// em.merge(e);
					// em.merge(p);
					em.remove(r);
				} else {
					throw new ReservaInexistente("No hay una reserva para esa etapa.");
				}
			}
		}
	}

	/*
	 * TODO: DtPuesto tiene Vacunatorio, y no DtVacunatorio.
	 * 
	 
	public DtReserva obtenerReserva(int ciudadano, int etapa) throws ReservaInexistente, UsuarioExistente {
		Ciudadano c = em.find(Ciudadano.class, ciudadano);
		if (c == null) {
			throw new UsuarioExistente("El usuario seleccionado no existe.");
		} else {
			Etapa e = em.find(Etapa.class, etapa);
			if (e == null) {
				throw new ReservaInexistente("La etapa seleccionada no existe.");
			} else {
				Reserva r = getReservaEtapa(c.getReservas(), e);
				if (r != null) {
					DtReserva retorno = new DtReserva(r.getEstado(), getDtUsuario(r.getCiudadano()),
							r.getFechaRegistro(), r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
							r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(),
							r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId());

					return retorno;
				} else {
					throw new ReservaInexistente("No hay una reserva para esa etapa.");
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

	private Reserva getReservaEtapa(List<Reserva> lista, Etapa e) {
		for (Reserva r : lista) {
			if (r.getEtapa().equals(e))
				return r;
		}

		return null;
	}

	private DtCiudadano getDtUsuario(Ciudadano u) {
		if (u != null)
			return new DtCiudadano(u.getIdUsuario(), u.getNombre(), u.getApellido(), u.getFechaNac(), u.getEmail(),
					u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado());
		else
			return null;
	}
*/
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

	private ArrayList<Reserva> getReservasTurno(Agenda a, LocalTime hora) {
		ArrayList<Reserva> retorno = new ArrayList<Reserva>();
		List<Reserva> list = a.getReservas();
		for (Reserva r : list) {
			if (r.getFechaRegistro().toLocalTime().equals(hora)) {
				retorno.add(r);
			}
		}
		return retorno;
	}

	private Puesto encontrarPuestoLibre(ArrayList<Reserva> reservasHora, List<Puesto> puestosVac) {
		Puesto retorno = null;
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
	
	private ArrayList<LocalTime> calcularHorasSegunReglas(ReglasCupos rc) {
		ArrayList<LocalTime> horas = new ArrayList<LocalTime>();
		Long diferencia = Duration.between(rc.getHoraApertura(), rc.getHoraCierre()).toMinutes();
		int turno = rc.getDuracionTurno();
		int turnosTotales = (int)(diferencia.intValue() / turno); //trunco los decimales (redondea hacia abajo)
		LocalTime hora = LocalTime.from(rc.getHoraApertura());
		horas.add(hora);
		for (int i=1; i<turnosTotales; i++) {
			horas.add(hora.plusMinutes(turno*i));
		}
		return horas;
	}

}
