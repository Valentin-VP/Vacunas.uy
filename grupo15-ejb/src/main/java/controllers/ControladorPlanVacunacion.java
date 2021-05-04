package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtPlanVacunacion;
import entities.Etapa;
import entities.PlanVacunacion;
import exceptions.PlanVacunacionInexistente;
import exceptions.PlanVacunacionRepetido;
import interfaces.IPlanVacunacionLocal;
import interfaces.IPlanVacunacionRemote;

@Stateless
public class ControladorPlanVacunacion implements IPlanVacunacionLocal, IPlanVacunacionRemote{

	public ControladorPlanVacunacion() {

	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarPlanVacunacion(int id, String nombre, String descripcion) throws PlanVacunacionRepetido{
		if(em.find(PlanVacunacion.class, id) == null) {
			PlanVacunacion pV = new PlanVacunacion(id, nombre, descripcion);
			em.persist(pV);
		}else
			throw new PlanVacunacionRepetido("Ya existe un plan de vacunacion con esa id");
	}
	
	public ArrayList<DtPlanVacunacion> listarPlanesVacunacion() throws PlanVacunacionInexistente{
		Query query = em.createQuery("SELECT p FROM PlanVacunacion p ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<PlanVacunacion> pVacs = query.getResultList();
		if(!pVacs.isEmpty()) {
			ArrayList<DtPlanVacunacion> dtPlanVacs = new ArrayList<>();
	    	for(PlanVacunacion pV: pVacs) {
	    		dtPlanVacs.add(pV.toDtPlanVacunacion());	
	    	}
			return dtPlanVacs;
		}else {
			throw new PlanVacunacionInexistente("No existen planes de vacunacion registrados");
		}
	}
	
	public DtPlanVacunacion obtenerVacuna(int id) throws PlanVacunacionInexistente{
		PlanVacunacion pV = em.find(PlanVacunacion.class, id);
		if(pV != null) {
			return pV.toDtPlanVacunacion();
		}else {
			throw new PlanVacunacionInexistente("No existe un plan de vacunacion con esa id");
		}
	}
	
	public void eliminarPlanVacunacion(int id) throws PlanVacunacionInexistente { //controlar si al eliminar un plan, elimina las etapas relacionadas a el automaticamente
		PlanVacunacion pV = em.find(PlanVacunacion.class, id);
		if(pV != null) {
			List<Etapa> etapas = pV.getEtapas();
			for(Etapa e: etapas) { //Este for elimina las etapas de la BD en el PlanVacunacion
				em.remove(e);
			}
			em.remove(pV);
		}else
			throw new PlanVacunacionInexistente("No existe unplan vacunacion con esa id");
		
	}
	
	public void modificarPlanVacunacion(int id, String nombre, String descripcion, List<Etapa> etapas) throws PlanVacunacionInexistente {
		PlanVacunacion pV = em.find(PlanVacunacion.class, id);
		if(pV != null) {
			pV.setNombre(nombre);
			pV.setDescripcion(descripcion);
			pV.setEtapas(etapas);
		}else
			throw new PlanVacunacionInexistente("No existe unplan vacunacion con esa id");
	}
	
}
