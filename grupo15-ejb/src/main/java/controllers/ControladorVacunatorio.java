package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import interfaces.IControladorVacunatorioLocal;
import interfaces.IControladorVacunatorioRemote;
import datatypes.DtDireccion;
import entities.Vacunatorio;
import datatypes.DtVacunatorio;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Stateless
public class ControladorVacunatorio implements IControladorVacunatorioLocal, IControladorVacunatorioRemote {
	@PersistenceContext (name="test")
	private EntityManager em;
	

public void agregarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud) throws VacunatorioCargadoException {

	
	Vacunatorio vac= em.find(Vacunatorio.class, id);
	
	if (vac==null) {
		Vacunatorio vacNew= new Vacunatorio(id, nombre, dtDir, telefono, latitud, longitud);
	
		em.persist(vacNew);
	
	}else {
	throw new VacunatorioCargadoException("El vacunatorio "+ id +" ya existe en el sistema\n");
		 
	}
		
		
	}

public DtVacunatorio obtenerVacunatorio(String id) throws VacunatorioNoCargadoException {
	
	
	Vacunatorio vac= em.find(Vacunatorio.class, id);
	
	if (vac==null) {
	
	throw new VacunatorioNoCargadoException("El vacunatorio "+ id +" no existe en el sistema\n");
		 
	}
	else {
		DtVacunatorio dtVac = new DtVacunatorio(vac.getId(),vac.getNombre(),vac.getDtDir(), vac.getTelefono(),vac.getLatitud(),vac.getLongitud());
				return dtVac;
	}
	
}
public ArrayList<DtVacunatorio> listarVacunatorio()throws VacunatoriosNoCargadosException {


	
	Query query = em.createQuery("SELECT v FROM Vacunatorio v");
	List<Vacunatorio> aux = query.getResultList();
	ArrayList<DtVacunatorio> vac= new ArrayList<DtVacunatorio>();
	
	for(Vacunatorio v: aux ) {
		
		DtVacunatorio dtVac = new DtVacunatorio(v.getId(),v.getNombre(), v.getDtDir(),v.getTelefono(),v.getLatitud(),v.getLongitud());
		vac.add(dtVac);
	}
	if(aux.isEmpty()) {
throw new VacunatoriosNoCargadosException("No existen vacunatorios en el sistema\n");
	}
	else {
		
		return vac;
	}
	
}

public void modificarVacunatorio(DtVacunatorio dtVac) throws VacunatorioNoCargadoException {
	
	
	Vacunatorio vac= em.find(Vacunatorio.class, dtVac.getId());
	
	vac.setNombre(dtVac.getNombre());
	vac.setDtDir(dtVac.getDtDir());
	vac.setLatitud(dtVac.getLatitud());
	vac.setLongitud(dtVac.getLatitud());
	vac.setTelefono(dtVac.getTelefono());
	em.persist(vac);
				
	}

public void eliminarVacunatorio(String id) throws VacunatorioNoCargadoException {
	
	
	Vacunatorio vac= em.find(Vacunatorio.class, id);
	
	if (vac==null) {
	
	throw new VacunatorioNoCargadoException("El vacunatorio "+ id +" no existe en el sistema\n");
		 
	}
	else {
		em.remove(vac);
	}
	
}
	
}
