package controllers;

import java.time.LocalDate;
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
import entities.Reserva;
import entities.Vacuna;
import exceptions.AccionInvalida;
import exceptions.EtapaInexistente;
import exceptions.EtapaRepetida;
import exceptions.PlanVacunacionInexistente;
import exceptions.VacunaInexistente;
import interfaces.IEtapaLocal;
import interfaces.IEtapaRemote;
import persistence.EtapaID;

@Stateless
public class ControladorEtapa implements IEtapaLocal, IEtapaRemote{

	public ControladorEtapa() {
		
	}
	
	@PersistenceContext(name = "test")
	private EntityManager em;
	
	public void agregarEtapa(LocalDate fIni, LocalDate fFin, String cond, int idPlan, String nombreVacuna) throws EtapaRepetida, PlanVacunacionInexistente, VacunaInexistente, AccionInvalida {
			PlanVacunacion pV = em.find(PlanVacunacion.class, idPlan);
			if(pV != null) {
				Etapa etapa = new Etapa(fIni, fFin, cond, pV);
				Vacuna v = em.find(Vacuna.class, nombreVacuna);
				if (v==null)
					throw new VacunaInexistente("No existe esa vacuna");
				if (!v.getEnfermedad().equals(pV.getEnfermedad()))
					throw new AccionInvalida("Esa vacuna no inmuniza '" + pV.getEnfermedad().getNombre() + "'.");
				etapa.setVacuna(v);
				em.persist(etapa);
				pV.addEtapa(etapa);
				 em.persist(pV);
			}else
				throw new PlanVacunacionInexistente("No existe un plan de vacunacion con ese id");
		
	}
	
	public ArrayList<DtEtapa> listarEtapas() throws EtapaInexistente{
		Query query = em.createQuery("SELECT e FROM Etapa e ORDER BY id ASC");
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
	
	public DtEtapa obtenerEtapa(int id, int idPlan) throws EtapaInexistente {
		Etapa etapa = em.find(Etapa.class, new EtapaID(id, idPlan));
		if(etapa != null) {
			return etapa.toDtEtapa();
		}else
			throw new EtapaInexistente("No existe una etapa con ese id");
	}
	
	public void eliminarEtapa(int id, int idPlan) throws EtapaInexistente, AccionInvalida {
		Etapa etapa = em.find(Etapa.class, new EtapaID(id, idPlan));
		if(etapa != null) {
			Query queryP = em.createQuery("SELECT p FROM PlanVacunacion p ORDER BY nombre ASC");
			Query queryR = em.createQuery("SELECT r FROM Reserva r");
			@SuppressWarnings("unchecked")
			List<PlanVacunacion> planes = queryP.getResultList();
			@SuppressWarnings("unchecked")
			List<Reserva> reservas = queryR.getResultList();
			for (Reserva r: reservas) {
				if (r.getEtapa().equals(etapa)) {
					throw new AccionInvalida("El usuario de cedula '" +r.getCiudadano().getIdUsuario() + "' tiene una reserva asociada a esa etapa..");
				}
			}
			for (PlanVacunacion pv: planes) {
				pv.getEtapas().remove(etapa);
				/*for (Etapa e: pv.getEtapas()) {
					if (e.equals(etapa)) {
						throw new AccionInvalida("Hay un Plan de Vacunacion de ID '" + pv.getId() + "': " + pv.getNombre() + " que esta asociado a esa etapa.");
					}
				}*/
			}
			em.remove(etapa);
		}else
			throw new EtapaInexistente("No existe una etapa con ese id");
	}
	
	public void modificarEtapa(int id, int plan, LocalDate fechaInicio, LocalDate fechaFin, String condicion)throws EtapaInexistente {
		Etapa etapa = em.find(Etapa.class, new EtapaID(id, plan));
		if(etapa != null) {
			etapa.setFechaInicio(fechaInicio);
			etapa.setFechaFin(fechaFin);
			etapa.setCondicion(condicion);
			em.merge(etapa);
		}else
			throw new EtapaInexistente("No existe un plan con esa id: " + id);
	}
}
