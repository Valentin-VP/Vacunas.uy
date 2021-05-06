package controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtAgenda;
import datatypes.DtCiudadano;
import datatypes.DtCupo;
import datatypes.DtReserva;
import entities.Agenda;
import entities.Ciudadano;
import entities.Cupo;
import entities.Reserva;
import entities.Vacunatorio;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;
import exceptions.CupoInexistente;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IAgendaDAOLocal;
import interfaces.IAgendaDAORemote;

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
    public void agregarAgenda(int id, String vacunatorio, LocalDate fecha, ArrayList<DtCupo> cupos, ArrayList<DtReserva> reservas) throws AgendaRepetida, CupoInexistente, VacunatorioNoCargadoException {
    	if (em.find(Agenda.class, id) != null){
    		throw new AgendaRepetida("Ya existe una agenda con ese ID.");
    	}
    	Vacunatorio v = em.find(Vacunatorio.class, vacunatorio);
    	if (v!=null) {
        		if (existeAlgunaAgendaConEsaFecha(v.getAgenda(), fecha)) {
        			throw new AgendaRepetida("Ya existe una agenda para ese día.");
        		}
        		Agenda a = new Agenda(id, fecha);
        		
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
        		/*List<Reserva> listReservas= new ArrayList<Reserva>();
        		for (DtReserva dtr: reservas) {
        			Reserva r = em.find(Reserva.class, dtr.getId());
        			if (r!=null) 
        				listReservas.add(r);
        			else
        				throw new ReservaInexistente("La reserva que se intentó agregar no existe.");
    			}*/
        		
        		a.setCupos(listCupos);
        		
        		a.setReservas(listReservas);
        		em.persist(a);
        		for (Cupo c: a.getCupos())
        			em.merge(c);
        		
    	}else {
    		throw new VacunatorioNoCargadoException("No existe un vacunatorio con ese ID.");
    	}
    	
    }
    
    /* TODO: controlar que una reserva no esté ya en otra agenda. Lo mismo para el cupo.
     * 
     */
    public void modificarAgenda(int id, String vacunatorio, LocalDate fecha, ArrayList<DtCupo> cupos, ArrayList<DtReserva> reservas) throws AgendaInexistente, CupoInexistente, AgendaRepetida, VacunatorioNoCargadoException {
    	Agenda a = em.find(Agenda.class, id);
    	if (a == null){
    		throw new AgendaInexistente("No existe una agenda con ese ID.");
    	}
    	Vacunatorio v = em.find(Vacunatorio.class, vacunatorio);
    	if (v!=null) {
        		if (existeAlgunaAgendaConEsaFecha(v.getAgenda(), fecha)) {
        			throw new AgendaRepetida("Ya existe una agenda para ese día.");
        		}
        		
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
        		/*List<Reserva> listReservas= new ArrayList<Reserva>();
        		for (DtReserva dtr: reservas) {
        			Reserva r = em.find(Reserva.class, dtr.getId());
        			if (r!=null) 
        				listReservas.add(r);
        			else
        				throw new ReservaInexistente("La reserva que se intentó agregar no existe.");
    			}*/
        		
        		a.setCupos(listCupos);
        		a.setReservas(listReservas);
        		em.merge(a);
        		for (Cupo c: a.getCupos())
        			em.merge(c);
    	}else {
    		throw new VacunatorioNoCargadoException("No existe un vacunatorio con ese ID.");
    	}
    }
	
	public DtAgenda obtenerAgenda(int id) throws AgendaInexistente {
		Agenda temp = em.find(Agenda.class, id);
		
		if (temp!=null) {
			List<DtCupo> dtc= new ArrayList<DtCupo>();
			for (Cupo c: temp.getCupos()) {
				dtc.add(new DtCupo(c.getIdCupo(), c.isOcupado(), c.getAgenda().getIdAgenda()));
			}
			List<DtReserva> dtr= new ArrayList<DtReserva>();
			for (Reserva r: temp.getReservas()) {
				dtr.add(new DtReserva(r.getEstado(), getDtUsuario(r.getCiudadano()), r.getFechaRegistro(), r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
						r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(), r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId()));
			}
			DtAgenda retorno = new DtAgenda(temp.getIdAgenda(), temp.getFecha(), dtc, dtr);

			return retorno;
		}else
			throw new AgendaInexistente("No hay una agenda con ese ID.");
	}
	
	public ArrayList<DtAgenda> listarAgendas()  throws AgendaInexistente{
		Query query = em.createQuery("SELECT a FROM Agenda a");
		@SuppressWarnings("unchecked")
		ArrayList<Agenda> result = (ArrayList<Agenda>) query.getResultList();
		ArrayList<DtAgenda> retorno = new ArrayList<>();
		if (result!=null) {
			for (Agenda a: result) {
				List<DtCupo> dtc= new ArrayList<DtCupo>();
				for (Cupo c: a.getCupos()) {
					dtc.add(new DtCupo(c.getIdCupo(), c.isOcupado(), c.getAgenda().getIdAgenda()));
				}
				List<DtReserva> dtr= new ArrayList<DtReserva>();
				for (Reserva r: a.getReservas()) {
					dtr.add(new DtReserva(r.getEstado(), getDtUsuario(r.getCiudadano()), r.getFechaRegistro(), r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
							r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(), r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId()));
				}
				retorno.add(new DtAgenda(a.getIdAgenda(), a.getFecha(), dtc, dtr));
			}
			return retorno;
		}else {
			throw new AgendaInexistente("No hay agendas.");
		}
	}
	
	public void eliminarCuposAsociados(int idAgenda) throws AgendaInexistente {
		Agenda temp = em.find(Agenda.class, idAgenda);
		if (temp!=null) {
			for (Cupo c: temp.getCupos()) {
				em.remove(c);
			}
			temp.setCupos(new ArrayList<Cupo>());
			em.merge(temp);
			
		}else
			throw new AgendaInexistente("No hay una agenda con ese ID.");
	}
	
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
	
	private DtCiudadano getDtUsuario(Ciudadano u) {
		if (u!=null)
			return new DtCiudadano(
					u.getIdUsuario(), u.getNombre(), u.getApellido(), u.getFechaNac(), u.getEmail(), u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado());
		else
			return null;
	}
}
