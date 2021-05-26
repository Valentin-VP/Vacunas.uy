package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtEnfermedad;
import entities.Enfermedad;
import entities.Etapa;
import entities.PlanVacunacion;
import entities.Vacuna;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;
import interfaces.IEnfermedadLocal;
import interfaces.IEnfermedadRemote;

@Stateless
public class ControladorEnfermedad implements IEnfermedadLocal, IEnfermedadRemote {

	public ControladorEnfermedad() {
		
	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarEnfermedad(String nombre) throws EnfermedadRepetida {
		if(em.find(Enfermedad.class, nombre) == null) {
			Enfermedad enf = new Enfermedad(nombre);
			em.persist(enf);
		}else {
			throw new EnfermedadRepetida("Ya existe una enfermedad con ese nombre");
		}
	}
	
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente {
		Query query = em.createQuery("SELECT e FROM Enfermedad e ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<Enfermedad> enfermedades = query.getResultList();
		if(!enfermedades.isEmpty()) {
			ArrayList<DtEnfermedad> dtEnfs = new ArrayList<>();
	    	for(Enfermedad e: enfermedades) {
	    		dtEnfs.add(new DtEnfermedad(e.getNombre()));	
	    	}
			return dtEnfs;
		}else {
			throw new EnfermedadInexistente("No existen Enfermedades registradas");
		}
	}
	
	public DtEnfermedad obtenerEnfermedad(String nombre) throws EnfermedadInexistente {
		Enfermedad enf = em.find(Enfermedad.class, nombre);
		if(enf != null) {
			return new DtEnfermedad(enf.getNombre());
		}else {
			throw new EnfermedadInexistente("No existe una Enfermedad con ese nombre");
		}
	}
	
	public void eliminarEnfermedad(String nombre) throws EnfermedadInexistente, AccionInvalida {
		Enfermedad enf = em.find(Enfermedad.class, nombre);
		if(enf != null) {
			Query queryP = em.createQuery("SELECT p FROM PlanVacunacion p ORDER BY nombre ASC");
			Query queryV = em.createQuery("SELECT v FROM Vacuna v ORDER BY nombre ASC");
			@SuppressWarnings("unchecked")
			List<PlanVacunacion> planes = queryP.getResultList();
			@SuppressWarnings("unchecked")
			List<Vacuna> vacunas = queryV.getResultList();
			for (PlanVacunacion pv: planes) {
				if (pv.getEnfermedad().equals(enf)) {
					throw new AccionInvalida("Hay un Plan de Vacunacion de ID " + pv.getId() + ": " + pv.getNombre() + " que esta asociado a esa enfermedad.");
				}
			}
			for (Vacuna v: vacunas) {
				if (v.getEnfermedad().equals(enf)) {
					throw new AccionInvalida("Hay una vacuna de ID y nombre " + v.getNombre() + " que esta asociada a esa enfermedad.");
				}
			}
			em.remove(enf);
		}else
			throw new EnfermedadInexistente("No existe una Enfermedad con ese nombre");
	}
}
