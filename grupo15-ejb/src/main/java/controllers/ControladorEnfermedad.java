package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtEnfermedad;
import entities.Enfermedad;
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
	
	public DtEnfermedad obtenerLaboratorio(String nombre) throws EnfermedadInexistente {
		Enfermedad enf = em.find(Enfermedad.class, nombre);
		if(enf != null) {
			return new DtEnfermedad(enf.getNombre());
		}else {
			throw new EnfermedadInexistente("No existe una Enfermedad con ese nombre");
		}
	}
}
