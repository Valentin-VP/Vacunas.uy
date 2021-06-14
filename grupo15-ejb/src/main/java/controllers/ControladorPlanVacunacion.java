package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtPlanFecha;
import datatypes.DtPlanVacunacion;
import entities.Enfermedad;
import entities.Etapa;
import entities.PlanVacunacion;
import entities.Reserva;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.PlanVacunacionInexistente;
import interfaces.IPlanVacunacionLocal;
import interfaces.IPlanVacunacionRemote;

@Stateless
public class ControladorPlanVacunacion implements IPlanVacunacionLocal, IPlanVacunacionRemote{

	public ControladorPlanVacunacion() {

	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarPlanVacunacion(String nombre, String descripcion, String idEnfermedad) throws EnfermedadInexistente{
		Enfermedad e = em.find(Enfermedad.class, nombre);
		if (e==null) {
			throw new EnfermedadInexistente("No existe esa enfermedad.");
		}else {
			PlanVacunacion pV = new PlanVacunacion(nombre, descripcion);
			pV.setEnfermedad(e);
			em.persist(pV);
		}
	}
	/*
	public void agregarEnfermedadPlan(int id, String nombre) throws PlanVacunacionInexistente, EnfermedadInexistente, AccionInvalida {
		PlanVacunacion pv = em.find(PlanVacunacion.class, id);
		if (pv==null) {
			throw new PlanVacunacionInexistente("No existe ese plan de vacunacion.");
		}else {
			Enfermedad e = em.find(Enfermedad.class, nombre);
			if (e==null) {
				throw new EnfermedadInexistente("No existe esa enfermedad.");
			}else {
				Query queryR = em.createQuery("SELECT r FROM Reserva r");
				@SuppressWarnings("unchecked")
				List<Reserva> reservas = queryR.getResultList();
				for(Etapa et: pv.getEtapas()) { 
					for (Reserva r: reservas) {
						if (r.getEtapa().equals(et)) {
							throw new AccionInvalida("El usuario de cedula '" +r.getCiudadano().getIdUsuario() + "' tiene una reserva asociada a la etapa '" + et.getId() + "' de ese plan.");
						}
					}
					if (!et.getVacuna().getEnfermedad().equals(e)) {
						throw new AccionInvalida("La etapa '" + et.getId() + "' de ese plan tiene una vacuna que no inmuniza a esa enfermedad.");
					}
				}
				//if (pv.getEnfermedad()!=null)
				//	throw new AccionInvalida("El plan tiene la enfermedad '" + pv.getEnfermedad().getNombre() + "' asociada.");
				pv.setEnfermedad(e);
				em.merge(pv);
			}
		}
		
	}
	*/
	
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
	
	public DtPlanVacunacion obtenerPlanVacunacion(int id) throws PlanVacunacionInexistente{
		PlanVacunacion pV = em.find(PlanVacunacion.class, id);
		if(pV != null) {
			return pV.toDtPlanVacunacion();
		}else {
			throw new PlanVacunacionInexistente("No existe un plan de vacunacion con esa id");
		}
	}
	
	public void eliminarPlanVacunacion(int id) throws PlanVacunacionInexistente, AccionInvalida { //controlar si al eliminar un plan, elimina las etapas relacionadas a el automaticamente
		PlanVacunacion pV = em.find(PlanVacunacion.class, id);
		if(pV != null) {
			List<Etapa> etapas = pV.getEtapas();
			
			Query queryR = em.createQuery("SELECT r FROM Reserva r");
			@SuppressWarnings("unchecked")
			List<Reserva> reservas = queryR.getResultList();
			for(Etapa e: etapas) { 
				for (Reserva r: reservas) {
					if (r.getEtapa().equals(e)) {
						throw new AccionInvalida("El usuario de cedula '" +r.getCiudadano().getIdUsuario() + "' tiene una reserva asociada a la etapa '" + e.getId() + "' de ese plan.");
					}
				}
			}
			for(Etapa e: etapas) { //Este for elimina las etapas de la BD en el PlanVacunacion
				//pV.getEtapas().remove(e);
				//em.merge(pV);
				em.remove(e);
			}
			em.remove(pV);
		}else
			throw new PlanVacunacionInexistente("No existe un plan de vacunacion con esa id");
		
	}
	
	public void modificarPlanVacunacion(int id, String nombre, String descripcion, List<Etapa> etapas) throws PlanVacunacionInexistente {
		PlanVacunacion pV = em.find(PlanVacunacion.class, id);
		if(pV != null) {
			pV.setNombre(nombre);
			pV.setDescripcion(descripcion);
			em.merge(pV);
			//pV.setEtapas(etapas);
		}else
			throw new PlanVacunacionInexistente("No existe un plan de vacunacion con esa id");
	}
	
	public ArrayList<DtPlanFecha> listarAgendasAbiertas() throws PlanVacunacionInexistente{
		Query query = em.createQuery("SELECT p FROM PlanVacunacion p ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<PlanVacunacion> pVacs = query.getResultList();
		if(!pVacs.isEmpty()) {
			ArrayList<DtPlanFecha> retorno = new ArrayList<>();
	    	for(PlanVacunacion pV: pVacs) {
	    		LocalDate fIni;
    			LocalDate fFin;
	    		List<Etapa> etapas = pV.getEtapas();
	    		if (!etapas.isEmpty()) {
	    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    			fIni = etapas.get(0).getFechaInicio();
	    			fFin = etapas.get(0).getFechaFin();
		    		for (Etapa e: etapas) {
		    			if (e.getFechaInicio().isBefore(fIni))
		    				fIni = e.getFechaInicio();
		    			if (e.getFechaFin().isAfter(fFin))
		    				fFin = e.getFechaFin();
		    		}
		    		if (fIni.isBefore(LocalDate.now()) && fFin.isAfter(LocalDate.now())) {
		    			retorno.add(new DtPlanFecha(pV.getNombre(), pV.getDescripcion(), fIni.format(formatter) + " - " + fFin.format(formatter), pV.getEnfermedad().getNombre()));
		    		}
	    		}else {
	    			throw new PlanVacunacionInexistente("No existen agendas abiertas.");
	    		}
	    	}
    		if (retorno.isEmpty())
    			throw new PlanVacunacionInexistente("No existen agendas abiertas.");
    		else 
    			return retorno;
		}else {
			throw new PlanVacunacionInexistente("No existen agendas abiertas.");
		}
	}
	
	public ArrayList<DtPlanFecha> listarAgendasProximas() throws PlanVacunacionInexistente{
		Query query = em.createQuery("SELECT p FROM PlanVacunacion p ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<PlanVacunacion> pVacs = query.getResultList();
		if(!pVacs.isEmpty()) {
			ArrayList<DtPlanFecha> retorno = new ArrayList<>();
	    	for(PlanVacunacion pV: pVacs) {
	    		LocalDate fIni;
    			LocalDate fFin;
	    		List<Etapa> etapas = pV.getEtapas();
	    		if (!etapas.isEmpty()) {
	    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    			fIni = etapas.get(0).getFechaInicio();
	    			fFin = etapas.get(0).getFechaFin();
		    		for (Etapa e: etapas) {
		    			if (e.getFechaInicio().isBefore(fIni))
		    				fIni = e.getFechaInicio();
		    			if (e.getFechaFin().isAfter(fFin))
		    				fFin = e.getFechaFin();
		    		}
		    		if (fIni.isBefore(LocalDate.now().plusDays(30)) && fIni.isAfter(LocalDate.now()) && fFin.isAfter(fIni)) {
		    			retorno.add(new DtPlanFecha(pV.getNombre(), pV.getDescripcion(), fIni.format(formatter) + " - " + fFin.format(formatter), pV.getEnfermedad().getNombre()));
		    		}
	    		}else {
	    			throw new PlanVacunacionInexistente("No existen agendas proximas.");
	    		}
	    	}
    		if (retorno.isEmpty())
    			throw new PlanVacunacionInexistente("No existen agendas proximas.");
    		else 
    			return retorno;
		}else {
			throw new PlanVacunacionInexistente("No existen agendas proximas.");
		}
	}
}
