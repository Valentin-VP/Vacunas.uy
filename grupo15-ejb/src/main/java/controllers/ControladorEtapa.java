package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtEtapa;
import entities.Etapa;
import entities.PlanVacunacion;
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.PlanVacunacionInexistente;

@Stateless
public class ControladorEtapa {

	public ControladorEtapa() {
		
	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarEtapa(int idEtapa, Date fIni, Date fFin, String cond, int idPlan, String nombreVacuna) throws EtapaRepetida, PlanVacunacionInexistente {
		if(em.find(Etapa.class, idEtapa) == null) {
			PlanVacunacion pV = em.find(PlanVacunacion.class, idPlan);
			if(pV != null) {
				Etapa etapa = new Etapa(idEtapa, fIni, fFin, cond, pV);
				em.persist(etapa);
				pV.addEtapa(etapa);
				 em.persist(pV);
			}else
				throw new PlanVacunacionInexistente("No existe un plan de vacunacion con ese id");
		}else
			throw new EtapaRepetida("Ya existe una etapa con ese id");
		
	}
	
	public ArrayList<DtEtapa> listarEtapas() throws EtapaInexistente{
		Query query = em.createQuery("SELECT e FROM Etapa e ORDER BY nombre ASC");
		@SuppressWarnings("unchecked")
		List<Etapa> etapas = query.getResultList();
		if(!etapas.isEmpty()) {
			ArrayList<DtEtapa> dtEtapas = new ArrayList<>();
	    	for(Etapa e: etapas) {
	    		dtEtapas.add(e.toDtEtapa());	
	    	}
			return dtEtapas;
		}else 
			throw new EtapaInexistente("No existen etapas en el sistema");
	}
	
	public DtEtapa obtenerEtapa(int id) throws EtapaInexistente {
		Etapa etapa = em.find(Etapa.class, id);
		if(etapa != null) {
			return etapa.toDtEtapa();
		}else
			throw new EtapaInexistente("No existe una etapa con ese id");
	}
	
	
}
