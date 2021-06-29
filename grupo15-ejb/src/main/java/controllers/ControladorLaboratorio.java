package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtLaboratorio;
import entities.Laboratorio;
import entities.Vacuna;
import exceptions.AccionInvalida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;
import interfaces.ILaboratorioLocal;
import interfaces.ILaboratorioRemote;

@Stateless
public class ControladorLaboratorio implements ILaboratorioLocal, ILaboratorioRemote{
	
	public ControladorLaboratorio() {
		super();
	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarLaboratorio(String nombre) throws LaboratorioRepetido {
		if(em.find(Laboratorio.class, nombre) == null) {
			Laboratorio lab = new Laboratorio(nombre);
			em.persist(lab);
		}else {
			throw new LaboratorioRepetido("Ya existe un laboratorio con ese nombre");
		}
	}
	
	public ArrayList<DtLaboratorio> listarLaboratorios() throws LaboratorioInexistente{
		Query query = em.createQuery("SELECT l FROM Laboratorio l ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<Laboratorio> labs = query.getResultList();
		if(!labs.isEmpty()) {
			ArrayList<DtLaboratorio> dtLabs = new ArrayList<>();
	    	for(Laboratorio l: labs) {
	    		dtLabs.add(new DtLaboratorio(l.getNombre()));	
	    	}
			return dtLabs;
		}else {
			throw new LaboratorioInexistente("No existen Laboratorios registrados");
		}
	}
	
	public DtLaboratorio obtenerLaboratorio(String nombre) throws LaboratorioInexistente{
		Laboratorio lab = em.find(Laboratorio.class, nombre);
		if(lab != null) {
			return new DtLaboratorio(lab.getNombre());
		}else 
			throw new LaboratorioInexistente("No existe un Laboratorio con ese nombre");
	}
	
	public void eliminarLaboratorio(String nombre) throws LaboratorioInexistente, AccionInvalida {
		Laboratorio lab = em.find(Laboratorio.class, nombre);
		if(lab != null) {
			Query queryV = em.createQuery("SELECT v FROM Vacuna v ORDER BY nombre ASC");
			@SuppressWarnings("unchecked")
			List<Vacuna> vacunas = queryV.getResultList();
			for (Vacuna v: vacunas) {
				if (v.getLaboratorio().equals(lab)) {
					throw new AccionInvalida("Hay una vacuna de ID y nombre '" + v.getNombre() + "' que esta asociada a ese laboratorio.");
				}
			}
			em.remove(lab);
		}else 
			throw new LaboratorioInexistente("No existe un Laboratorio con ese nombre");
	}
}
