package controllers;

import java.time.LocalDate;
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
import entities.Enfermedad;
import entities.Etapa;
import entities.Laboratorio;
import entities.LoteDosis;
import entities.PlanVacunacion;
import entities.Stock;
import entities.Vacuna;
import entities.Vacunatorio;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.LaboratorioInexistente;
import exceptions.VacunaInexistente;
import exceptions.VacunaRepetida;
import interfaces.IControladorVacunaLocal;
import interfaces.IControladorVacunaRemote;



@Stateless
public class ControladorVacuna implements IControladorVacunaLocal, IControladorVacunaRemote {

	public ControladorVacuna() {
		super();
	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarVacuna(String nombre, int cantDosis, int tiempoEntreDosis, int expira, String laboratorio, String enfermedad) throws VacunaRepetida , LaboratorioInexistente, EnfermedadInexistente{
		if(em.find(Vacuna.class, nombre) == null) {
			//Calendar result = new GregorianCalendar();
            //result.set(anio, mes, dia, 0, 0, 0);
            //LocalDate f = LocalDate.of(anio, mes, dia);
            Laboratorio lab = em.find(Laboratorio.class, laboratorio);
            if(lab == null) //controla que exista el laboratorio
            	throw new LaboratorioInexistente("No existe una laboratorio con ese nombre");
            Enfermedad enf = em.find(Enfermedad.class, enfermedad);
            if(enf == null) //controla que exista la enfermedad
            	throw new EnfermedadInexistente("No existe una enfermedad con ese nombre");
            Vacuna vac = new Vacuna(nombre, cantDosis, expira, tiempoEntreDosis, lab, enf);
			em.persist(vac);
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
	    		dtVacs.add(v.toDtVacuna());	
	    	}
			return dtVacs;
		}else {
			throw new VacunaInexistente("No existen Vacunas registradas");
		}
		
	}
	
	public DtVacuna obtenerVacuna(String nombre) throws VacunaInexistente{
		Vacuna vac = em.find(Vacuna.class, nombre);
		if(vac != null) {
			return vac.toDtVacuna();
		}else {
			throw new VacunaInexistente("No existe una vacuna con ese nombre");
		}
	}
	
	public void eliminarVacuna(String nombre) throws VacunaInexistente, AccionInvalida {
		Vacuna vac = em.find(Vacuna.class, nombre);
		if(vac != null) {
			Query queryE = em.createQuery("SELECT e FROM Etapa e ORDER BY id ASC");
			Query queryV = em.createQuery("SELECT v FROM Vacunatorio v ORDER BY id ASC");
			@SuppressWarnings("unchecked")
			List<Etapa> etapas = queryE.getResultList();
			@SuppressWarnings("unchecked")
			List<Vacunatorio> vacunatorios = queryV.getResultList();
			for (Etapa e: etapas) {
				if (e.getVacuna().equals(vac)) {
					throw new AccionInvalida("Hay una etapa de ID '" + e.getId() + "' que esta asociada a esa vacuna.");
				}
			}
			for (Vacunatorio v: vacunatorios) {
				for (Stock s: v.getStock()) {
					if (s.getVacuna().equals(vac)) {
						throw new AccionInvalida("Hay un vacunatorio de ID '" + v.getId() + "' y nombre '" + v.getNombre() + "' que esta asociado a esa vacuna.");
					}
				}
				for (LoteDosis ld: v.getLote()) {
					if (ld.getVacuna().equals(vac)) {
						throw new AccionInvalida("Hay un vacunatorio de ID '" + v.getId() + "' y nombre '" + v.getNombre() + "' que esta asociado a esa vacuna.");
					}
				}
			}
			em.remove(vac);
		}else
			throw new VacunaInexistente("No existe una vacuna con ese nombre");
	}
	
	public void modificarVacuna(String nombre, int cantDosis, int expira, int tiempoEntreDosis, String laboratorio, String enfermedad) throws VacunaInexistente, LaboratorioInexistente, EnfermedadInexistente{
		Vacuna vac = em.find(Vacuna.class, nombre);
		if(vac != null) {
			vac.setCantDosis(cantDosis);
			vac.setExpira(expira);
			vac.setTiempoEntreDosis(tiempoEntreDosis);
			Laboratorio lab = em.find(Laboratorio.class, laboratorio);
			if(lab != null)
				vac.setLaboratorio(lab);
			else
				throw new LaboratorioInexistente("No existe un laboratorio con ese nombre");
			Enfermedad enf = em.find(Enfermedad.class, enfermedad);
			if(enf != null)
				vac.setEnfermedad(enf);
			else
				throw new EnfermedadInexistente("No existe una enfermedad con ese nombre");
			em.persist(vac);
		}else
			throw new VacunaInexistente("No existe una vacuna con ese nombre");
	}
}
