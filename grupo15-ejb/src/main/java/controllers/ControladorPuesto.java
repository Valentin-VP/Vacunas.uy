package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtPuesto;
import datatypes.DtVacunatorio;
import entities.Puesto;
import entities.Vacunatorio;
import exceptions.PuestoCargadoException;
import exceptions.PuestoNoCargadoException;
import exceptions.PuestoNoCargadosException;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorPuestoLocal;
import interfaces.IControladorPuestoRemote;
import persistence.PuestoID;

@Stateless
public class ControladorPuesto implements IControladorPuestoLocal, IControladorPuestoRemote {
	@PersistenceContext(name = "test")
	private EntityManager em;

	public void agregarPuesto(String id, String vacunatorio) throws PuestoCargadoException, VacunatorioNoCargadoException {

		Puesto puesto = em.find(Puesto.class, new PuestoID(id, vacunatorio));
		

		if (puesto == null) {
			
			Vacunatorio v = em.find(Vacunatorio.class, vacunatorio);
			if (v == null) {
    			throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
    		}else {
				Puesto puestoNew = new Puesto(id, v);
	
				em.persist(puestoNew);
    		}
		} else {
			throw new PuestoCargadoException("El puesto " + id + " ya existe en el sistema\n");

		}

	}

	public String obtenerPuesto(String id, String vac) throws PuestoNoCargadoException, VacunatorioNoCargadoException {
/*
		Puesto puesto = em.find(Puesto.class, id);

		if (puesto == null) {

			throw new PuestoNoCargadoException("El puesto " + id + " no existe en el sistema\n");

		} else {
			
			Vacunatorio v = em.find(Vacunatorio.class, vac);
			if (v == null) {
    			throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
    		}else {
    			
    			//DtPuesto dtPuesto = new DtPuesto(puesto.getId(), getDtVacunatorio(v));
    			return puesto.getId();
    		}
			
		}
*/return null;
	}

	public ArrayList<String> listarPuestos(String idVac) throws PuestoNoCargadosException {

		Query query = em.createQuery("SELECT v FROM Puesto v");
		List<Puesto> aux = query.getResultList();
		ArrayList<String> puestos = new ArrayList<String>();

		for (Puesto p : aux) {
			if (p.getVacunatorio().getId().equals(idVac)) {
				//DtPuesto dtPuesto = new DtPuesto(p.getId(), getDtVacunatorio(p.getVacunatorio()));
				puestos.add(p.getId());
			}
		}
		if (aux.isEmpty()) {
			throw new PuestoNoCargadosException("No existen puestos en el sistema\n");
		} else {

			return puestos;
		}

	}

	public void modificarPuesto(DtPuesto dtPuesto) throws PuestoNoCargadoException {
/*
		Puesto puesto = em.find(Puesto.class, dtPuesto.getId());

		puesto.setId(dtPuesto.getId());
		puesto.setVacunatorio(puesto.getVacunatorio());*/

	}

	public void eliminarPuesto(String id) throws PuestoNoCargadoException {
/*
		Puesto puesto = em.find(Puesto.class, id);

		if (puesto == null) {

			throw new PuestoNoCargadoException("El puesto " + id + " no existe en el sistema\n");

		} else {
			em.remove(puesto);
		}
*/
	}

	private DtVacunatorio getDtVacunatorio(Vacunatorio v) {
		return new DtVacunatorio(v.getId(), v.getNombre(), v.getDtDir(), v.getTelefono(), v.getLatitud(), v.getLongitud());
	}
}
