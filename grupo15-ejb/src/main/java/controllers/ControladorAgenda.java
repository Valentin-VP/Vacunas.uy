package controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DtAgenda;
import datatypes.DtCiudadano;
import datatypes.DtReserva;
import datatypes.DtReservaCompleto;
import entities.Agenda;
import entities.Ciudadano;
import entities.Reserva;
import entities.Vacunatorio;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;
import exceptions.CupoInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IAgendaDAOLocal;
import interfaces.IAgendaDAORemote;
import persistence.AgendaID;

/**
 * Session Bean implementation class ControladorAgenda
 */
@Stateless
@LocalBean
public class ControladorAgenda implements IAgendaDAORemote, IAgendaDAOLocal {
	
	@PersistenceContext(name = "test")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public ControladorAgenda() {
        // TODO Auto-generated constructor stub
    }
    
    /* TODO: controlar que una reserva no esté ya en otra agenda. Lo mismo para el cupo.
     * buscar vac, agenda no existe => add cupos, add vac, vac.setAgenda, merge(vac), persist(agenda)
     */
    public void agregarAgenda(String vacunatorio, LocalDate fecha) throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
    	if (em.find(Agenda.class, new AgendaID(fecha, vacunatorio)) != null){
    		throw new AgendaRepetida("Ya existe una agenda en esa fecha para ese vacunatorio.");
    	}
    	Vacunatorio v = em.find(Vacunatorio.class, vacunatorio);
    	if (v!=null) {
        		/*if (existeAlgunaAgendaConEsaFecha(v.getAgenda(), fecha)) {
        			throw new AgendaRepetida("Ya existe una agenda para ese día.");
        		}*/
        		Agenda a = new Agenda(fecha);
        		a.setVacunatorio(v);
        		v.getAgenda().add(a);
        		/*
        		List<Cupo> listCupos= new ArrayList<Cupo>();
        		for (DtCupo dtc: cupos) {
        			Cupo c = em.find(Cupo.class, dtc.getIdCupo());
        			if (c!=null) {
        				listCupos.add(c);
        				c.getAgenda().getCupos().remove(c);//desvinculo el cupo de la otra agenda (por el lado de Agenda)
        				em.merge(c.getAgenda());
        				c.setAgenda(a);
        			}
        			else
        				throw new CupoInexistente("El cupo que se intentó adicionar no existe.");
    			}
        		
        		List<Reserva> listReservas = buscarReservasUsuarioEtapa(reservas);
        		List<Reserva> listReservas= new ArrayList<Reserva>();
        		for (DtReserva dtr: reservas) {
        			Reserva r = em.find(Reserva.class, dtr.getId());
        			if (r!=null) 
        				listReservas.add(r);
        			else
        				throw new ReservaInexistente("La reserva que se intentó agregar no existe.");
    			}
        		
        		a.setCupos(listCupos);
        		
        		a.setReservas(listReservas);
        		*/
        		
        		em.merge(v);
        		//em.persist(a);
        		//for (Cupo c: a.getCupos())
        		//	em.merge(c);
        		
    	}else {
    		throw new VacunatorioNoCargadoException("No existe un vacunatorio con ese ID.");
    	}
    	
    }
    
    /* TODO: controlar que una reserva no esté ya en otra agenda. Lo mismo para el cupo.
     * 
     */
    public void modificarAgenda(String vacunatorio, LocalDate fecha) throws AgendaInexistente, CupoInexistente, AgendaRepetida, VacunatorioNoCargadoException {
    	Agenda a = em.find(Agenda.class, new AgendaID(fecha, vacunatorio));
    	if (a == null){
    		throw new AgendaInexistente("No existe una agenda con esa fecha en ese vacunatorio.");
    	}
    	Vacunatorio v = em.find(Vacunatorio.class, vacunatorio);
    	if (v!=null) {
        		//if (existeAlgunaAgendaConEsaFecha(v.getAgenda(), fecha)) {
        		//	throw new AgendaRepetida("Ya existe una agenda para ese día.");
        		//}
        		/*
        		List<Cupo> listCupos= new ArrayList<Cupo>();
        		for (DtCupo dtc: cupos) {
        			Cupo c = em.find(Cupo.class, dtc.getIdCupo());
        			if (c!=null) {
        				listCupos.add(c);
        				c.getAgenda().getCupos().remove(c);//desvinculo el cupo de la otra agenda (por el lado de Agenda)
        				em.merge(c.getAgenda());
        				c.setAgenda(a);
        			}
        			else
        				throw new CupoInexistente("El cupo que se intentó adicionar no existe.");
    			}
        		
        		List<Reserva> listReservas = buscarReservasUsuarioEtapa(reservas);

        		a.setCupos(listCupos);
        		a.setReservas(listReservas);
        		*/
    			
        		em.merge(a);
        		//for (Cupo c: a.getCupos())
        		//	em.merge(c);
    	}else {
    		throw new VacunatorioNoCargadoException("No existe un vacunatorio con ese ID.");
    	}
    }
	
	public ArrayList<DtReserva> obtenerAgenda(String vacunatorio, LocalDate fecha) throws AgendaInexistente {
		Agenda temp = em.find(Agenda.class, new AgendaID(LocalDate.now(), vacunatorio));
		
		if (temp!=null) {
			/*List<DtCupo> dtc= new ArrayList<DtCupo>();
			for (Cupo c: temp.getCupos()) {
				dtc.add(new DtCupo(c.getIdCupo(), c.isOcupado(), c.getAgenda().getIdAgenda()));
			}*/
			ArrayList<DtReserva> dtr= new ArrayList<DtReserva>();
			for (Reserva r: temp.getReservas()) {
				dtr.add(r.getDtReserva());
			}
			//DtAgenda retorno = new DtAgenda(temp.getFecha(), dtr);

			return dtr;
		}else
			throw new AgendaInexistente("No hay una agenda en esa fecha en ese vacunatorio.");
	}
	
	public ArrayList<DtAgenda> listarAgendas(String vacunatorio)  throws AgendaInexistente, VacunatorioNoCargadoException{
		Vacunatorio v = em.find(Vacunatorio.class, vacunatorio);
		if (v != null){
			ArrayList<DtAgenda> retorno = new ArrayList<>();
			ArrayList<Agenda> result = (ArrayList<Agenda>) v.getAgenda();
			
			if (!result.isEmpty()) {
				for (Agenda a: result) {
					/*List<DtCupo> dtc= new ArrayList<DtCupo>();
					for (Cupo c: a.getCupos()) {
						dtc.add(new DtCupo(c.getIdCupo(), c.isOcupado(), c.getAgenda().getIdAgenda()));
					}*/
					List<DtReserva> dtr= new ArrayList<DtReserva>();
					for (Reserva r: a.getReservas()) {
						dtr.add(r.getDtReserva());
					}
					retorno.add(new DtAgenda(a.getFecha(), dtr));
				}
				return retorno;
			}else {
				throw new AgendaInexistente("No hay agendas.");
			}
    	}else
    		throw new VacunatorioNoCargadoException("No existe ese vacunatorio.");
	}
	
	public ArrayList<DtReservaCompleto> obtenerAgendaSoap(String vacunatorio, LocalDate fecha) throws AgendaInexistente {
		Agenda temp = em.find(Agenda.class, new AgendaID(LocalDate.now(), vacunatorio));
		
		if (temp!=null) {
			/*List<DtCupo> dtc= new ArrayList<DtCupo>();
			for (Cupo c: temp.getCupos()) {
				dtc.add(new DtCupo(c.getIdCupo(), c.isOcupado(), c.getAgenda().getIdAgenda()));
			}*/
			ArrayList<DtReservaCompleto> dtr= new ArrayList<DtReservaCompleto>();
			for (Reserva r: temp.getReservas()) {
				dtr.add(r.getDtReservaCompleto());
			}
			//DtAgenda retorno = new DtAgenda(temp.getFecha(), dtr);

			return dtr;
		}else
			throw new AgendaInexistente("No hay una agenda en esa fecha en ese vacunatorio.");
	}
	
	public void eliminarCuposAsociados(int idAgenda) throws AgendaInexistente {
		/*Agenda temp = em.find(Agenda.class, idAgenda);
		if (temp!=null) {
			for (Cupo c: temp.getCupos()) {
				em.remove(c);
			}
			temp.setCupos(new ArrayList<Cupo>());
			em.merge(temp);
			
		}else
			throw new AgendaInexistente("No hay una agenda con ese ID.");*/
	}
	/*
	private Agenda getAgendaEnVacunatorio(ArrayList<Agenda> lista, int id) { // que es esto nico
		for (Agenda a: lista) {
			if (a.equals(em.find(Agenda.class, id)))
				return a;
		}
		return null;
	}
	
	private boolean existeAlgunaAgendaConEsaFecha(List<Agenda> lista, LocalDate fecha) {
		for (Agenda a: lista) {
			if (a.getFecha().isEqual(fecha))
				return true;
		}
		return false;
	}
	
	private ArrayList<Reserva> buscarReservasUsuarioEtapa(ArrayList<DtReserva> lista){
		ArrayList<Reserva> retorno = new ArrayList<>();
		for (DtReserva dtr: lista) {
			
			Ciudadano c = em.find(Ciudadano.class, dtr.getCiudadano().getIdUsuario());
			if (c!=null) {
				ArrayList<Reserva> temp = (ArrayList<Reserva>) c.getReservas();
				for (Reserva r: temp) {
					if (r.getEtapa().getId() == dtr.getEtapa())
						retorno.add(r);
				}
			}
		}
		return retorno;
	}
	*/
	private DtCiudadano getDtUsuario(Ciudadano u) {
		if (u!=null)
			return new DtCiudadano(
					u.getIdUsuario(), u.getNombre(), u.getApellido(), u.getFechaNac(), u.getEmail(), u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado());
		else
			return null;
	}
}
