package controllers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import interfaces.IControladorVacunatorioLocal;
import interfaces.IControladorVacunatorioRemote;
import datatypes.DtDireccion;
import entities.ReglasCupos;
import entities.Vacunatorio;
import datatypes.DtVacunatorio;
import exceptions.ReglasCuposCargadoException;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Stateless
public class ControladorVacunatorio implements IControladorVacunatorioLocal, IControladorVacunatorioRemote {
	@PersistenceContext(name = "test")
	private EntityManager em;

	public void agregarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud,
			Float longitud, String url) throws VacunatorioCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {
			Vacunatorio vacNew = new Vacunatorio(id, nombre, dtDir, telefono, latitud, longitud, url);

			em.persist(vacNew);

		} else {
			throw new VacunatorioCargadoException("El vacunatorio " + id + " ya existe en el sistema\n");

		}
	}
	
	public void setURLtoVacunatorio(String id, String url) throws VacunatorioNoCargadoException {
		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema");
		}else {
			vac.setUrl(url);
			em.merge(vac);
		}
	}

	public void agregarReglasCupos(String idVac, String id, Integer duracionTurno, LocalTime horaApertura,
			LocalTime horaCierre) throws VacunatorioNoCargadoException, ReglasCuposCargadoException {
		Vacunatorio vac = em.find(Vacunatorio.class, idVac);

		if (vac == null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema");
		} else {
			if (vac.getReglasCupos() != null) {
				throw new ReglasCuposCargadoException("La regla de cupo ya existe en el sistema");
			} else {
				ReglasCupos reglasNew = new ReglasCupos(id, duracionTurno, horaApertura, horaCierre);
				vac.setReglasCupos(reglasNew);
				em.merge(vac);
				em.persist(reglasNew);
			}
		}
	}

	public DtVacunatorio obtenerVacunatorio(String id) throws VacunatorioNoCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {

			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema\n");

		} else {
			DtVacunatorio dtVac = new DtVacunatorio(vac.getId(), vac.getNombre(), vac.getDtDir(), vac.getTelefono(),
					vac.getLatitud(), vac.getLongitud(), vac.getUrl());
			return dtVac;
		}

	}

	public ArrayList<DtVacunatorio> listarVacunatorio() throws VacunatoriosNoCargadosException {

		Query query = em.createQuery("SELECT v FROM Vacunatorio v");
		List<Vacunatorio> aux = query.getResultList();
		ArrayList<DtVacunatorio> vac = new ArrayList<DtVacunatorio>();

		for (Vacunatorio v : aux) {

			DtVacunatorio dtVac = new DtVacunatorio(v.getId(), v.getNombre(), v.getDtDir(), v.getTelefono(),
					v.getLatitud(), v.getLongitud(), v.getUrl());
			vac.add(dtVac);
		}
		if (aux.isEmpty()) {
			throw new VacunatoriosNoCargadosException("No existen vacunatorios en el sistema\n");
		} else {

			return vac;
		}

	}

	public void modificarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud,
			Float longitud, String url) throws VacunatorioNoCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);
		if (vac==null) {
			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema\n");
		}
		vac.setNombre(nombre);
		vac.setDtDir(dtDir);
		vac.setLatitud(latitud);
		vac.setLongitud(longitud);
		vac.setTelefono(telefono);
		vac.setUrl(url);
		em.merge(vac);
	}

	public void eliminarVacunatorio(String id) throws VacunatorioNoCargadoException {

		Vacunatorio vac = em.find(Vacunatorio.class, id);

		if (vac == null) {

			throw new VacunatorioNoCargadoException("El vacunatorio " + id + " no existe en el sistema\n");

		} else {
			em.remove(vac);
		}

	}

}
