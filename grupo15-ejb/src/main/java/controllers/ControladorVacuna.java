package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


import datatypes.DtVacuna;
import entities.Laboratorio;
import entities.Vacuna;
import exceptions.LaboratorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;
import interfaces.IControladorVacunaLocal;
import interfaces.IControladorVacunaRemoto;



@Stateless
public class ControladorVacuna implements IControladorVacunaLocal, IControladorVacunaRemoto {

	public ControladorVacuna() {
		super();
	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarVacuna(String nombre, Integer cantDosis, int dia, int mes, int anio, String laboratorio) throws VacunaRepetida , LaboratorioInexistente{
		if(em.find(Vacuna.class, nombre) == null) {
			Calendar result = new GregorianCalendar();
            result.set(anio, mes, dia, 0, 0, 0);
            Date f = result.getTime();
            Laboratorio lab = em.find(Laboratorio.class, laboratorio);
            if(lab != null) { //controla que exista el laboratorio
				Vacuna vac = new Vacuna(nombre, cantDosis, f, lab);
				em.persist(vac);
            }else
            	throw new LaboratorioInexistente("No existe una laboratorio con ese nombre");
		}else {
			throw new VacunaRepetida("Ya existe una Vacuna con ese nombre");
		}
	}
	
	public ArrayList<DtVacuna> listarVacunas()throws VacunaInexistente{
		Query query = em.createQuery("SELECT v FROM Vacuna v ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<Vacuna> vacs = query.getResultList();
		if(!vacs.isEmpty()) {
			ArrayList<DtVacuna> dtVacs = new ArrayList<>();
	    	for(Vacuna v: vacs) {
	    		dtVacs.add(new DtVacuna(v.getNombre(), v.getCantDosis(), v.getExpira()));	
	    	}
			return dtVacs;
		}else {
			throw new VacunaInexistente("No existen Vacunas registradas");
		}
		
	}
	
	public DtVacuna obtenerVacuna(String nombre) throws VacunaInexistente{
		Vacuna vac = em.find(Vacuna.class, nombre);
		if(vac != null) {
			return new DtVacuna(vac.getNombre(), vac.getCantDosis(), vac.getExpira());
		}else {
			throw new VacunaInexistente("No existe una vacuna con ese nombre");
		}
	}
}
