package controllers;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtCupo;
import entities.Agenda;
import entities.Cupo;
import exceptions.AgendaInexistente;
import exceptions.CupoInexistente;
import exceptions.CupoRepetido;
import interfaces.ICupoDAOLocal;
import interfaces.ICupoDAORemote;

/**
 * Session Bean implementation class ControladorCupo
 */
@Stateless
@LocalBean
public class ControladorCupo implements ICupoDAORemote, ICupoDAOLocal {
	
	@PersistenceContext(name = "test")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public ControladorCupo() {
        // TODO Auto-generated constructor stub
    }

    
    public void agregarCupo(int idCupo, int idAgenda, boolean ocupado) throws CupoRepetido, AgendaInexistente {
    	if (em.find(Cupo.class, idCupo) != null)
    		throw new CupoRepetido("Ya existe un cupo con ese ID.");
    	else {
    		Agenda a = em.find(Agenda.class, idAgenda);
    		if (a==null)
    			throw new AgendaInexistente("No existe una agenda con ese ID.");
    		else {
    			Cupo c = new Cupo(idCupo, ocupado, a);
    			a.getCupos().add(c);
    			em.merge(a);
    			em.persist(c);
    		}
    	}
    }
    
    //TODO: modificar agenda podria llegar a ser "opcional"
    public void modificarCupo(int idCupo, int idAgenda, boolean ocupado) throws AgendaInexistente, CupoInexistente {
    	Cupo c = em.find(Cupo.class, idCupo);
    	if (c == null)
    		throw new CupoInexistente("No existe un cupo con ese ID.");
    	else {
    		Agenda a = em.find(Agenda.class, idAgenda);
    		if (a==null)
    			throw new AgendaInexistente("No existe una agenda con ese ID.");
    		else {
    			c.setOcupado(ocupado);
    			c.getAgenda().getCupos().remove(c);
    			em.merge(c.getAgenda());
    			c.setAgenda(a);
    			a.getCupos().add(c);
    			em.merge(a);
    			em.merge(c);
    		}
    	}
    }
    
    public void eliminarCupo(int idCupo) throws CupoInexistente, AgendaInexistente {
    	Cupo c = em.find(Cupo.class, idCupo);
    	if (c == null)
    		throw new CupoInexistente("No existe un cupo con ese ID.");
    	else {
    		Agenda a = c.getAgenda();
    		if (a==null)
    			throw new AgendaInexistente("No existe una agenda relacionada con ese cupo"); //no deberia aparecer
    		else {
    			a.getCupos().remove(c);
    			em.merge(a);
    			em.remove(c);
    		}
    	}
    }
    
    public DtCupo obtenerCupo(int idCupo) throws CupoInexistente {
    	DtCupo retorno;
    	Cupo c = em.find(Cupo.class, idCupo);
    	if (c==null)
    		throw new CupoInexistente("No existe un cupo con ese ID.");
    	else {
    		retorno = new DtCupo(c.getIdCupo(), c.isOcupado(), c.getAgenda().getIdAgenda());
    		return retorno;
    	}
    }
    
    public ArrayList<DtCupo> listarCupos() throws CupoInexistente{
    	Query query = em.createQuery("SELECT c FROM Cupo c");
		@SuppressWarnings("unchecked")
		ArrayList<Cupo> result = (ArrayList<Cupo>) query.getResultList();
		ArrayList<DtCupo> retorno = new ArrayList<>();
		if (result!=null) {
			for (Cupo c: result) {
				retorno.add(new DtCupo(c.getIdCupo(), c.isOcupado(), c.getAgenda().getIdAgenda()));
			}
			return retorno;
		}else
			throw new CupoInexistente("No hay cupos en el sistema.");
    }
}
