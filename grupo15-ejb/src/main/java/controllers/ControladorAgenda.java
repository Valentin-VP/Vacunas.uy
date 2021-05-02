package controllers;

import interfaces.IAgendaDAOLocal;
import interfaces.IAgendaDAORemote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtAgenda;
import datatypes.DtCupo;
import datatypes.DtReserva;
import entities.Agenda;
import entities.Cupo;
import entities.Reserva;
import exceptions.AgendaInexistente;
import exceptions.AgendaRepetida;
import exceptions.ReservaInexistente;
import exceptions.ReservaRepetida;

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
    public void agregarAgenda(int id, LocalDate fecha, List<DtCupo> cupos, List<DtReserva> reservas) throws AgendaRepetida {
    	if (getAgenda(id) == null) {
    		if (getAgenda(id).getFecha().isEqual(fecha)) {
    			throw new AgendaRepetida("Ya existe una agenda para ese día.");
    		}
    		Agenda a = new Agenda(id, fecha);
    		List<Cupo> listCupos= new ArrayList<Cupo>();
    		for (DtCupo dtc: cupos) {
    			listCupos.add(new Cupo(dtc.getIdCupo(), dtc.isOcupado()));
			}
    		List<Reserva> listReservas= new ArrayList<Reserva>();
    		for (DtReserva dtr: reservas) {
    			listReservas.add(new Reserva(dtr.getId(), dtr.getUsuario(), dtr.getFecha(), dtr.getEstado()));
			}
    		
    		a.setCupos(listCupos);
    		a.setReservas(listReservas);
    		em.persist(a);
    	}else {
    		throw new AgendaRepetida("Ya existe una agenda con ese ID.");
    	}
    }
	
	public DtAgenda obtenerAgenda(int id) throws AgendaInexistente {
		Agenda temp = getAgenda(id);
		
		if (temp!=null) {
			List<DtCupo> dtc= new ArrayList<DtCupo>();
			for (Cupo c: temp.getCupos()) {
				dtc.add(new DtCupo(c.getIdCupo(), c.isOcupado()));
			}
			List<DtReserva> dtr= new ArrayList<DtReserva>();
			for (Reserva r: temp.getReservas()) {
				dtr.add(new DtReserva(r.getIdReserva(), r.getEstado(), r.getNombreUser(), r.getFechaRegistro()));
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
					dtc.add(new DtCupo(c.getIdCupo(), c.isOcupado()));
				}
				List<DtReserva> dtr= new ArrayList<DtReserva>();
				for (Reserva r: a.getReservas()) {
					dtr.add(new DtReserva(r.getIdReserva(), r.getEstado(), r.getNombreUser(), r.getFechaRegistro()));
				}
				retorno.add(new DtAgenda(a.getIdAgenda(), a.getFecha(), dtc, dtr));
			}
			return retorno;
		}else {
			throw new AgendaInexistente("No hay agendas.");
		}
	}
	
	private Agenda getAgenda(int id) {
		Agenda r = em.find(Agenda.class, id);
		return r;
	}
}
