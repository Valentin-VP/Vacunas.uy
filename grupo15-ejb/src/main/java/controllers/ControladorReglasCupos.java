package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import interfaces.IControladorReglasCuposLocal;
import interfaces.IControladorReglasCuposRemote;
import datatypes.DtHora;
import datatypes.DtReglasCupos;
import entities.ReglasCupos;
import entities.Vacunatorio;

import exceptions.ReglasCuposCargadoException;
import exceptions.ReglasCuposNoCargadoException;
import exceptions.ReglasCuposNoCargadosException;


@Stateless
public class ControladorReglasCupos implements IControladorReglasCuposLocal, IControladorReglasCuposRemote {
	@PersistenceContext (name="test")
	private EntityManager em;
	

public void agregarReglasCupos(String id, Date fecha, Integer duracionTurno, Vacunatorio vacunatorio, DtHora horaApertura,
		DtHora horaCierre) throws ReglasCuposCargadoException {

	
	ReglasCupos reglas= em.find(ReglasCupos.class, id);
	
	if (reglas==null) {
		ReglasCupos reglasNew= new ReglasCupos(id, fecha, duracionTurno, vacunatorio, horaApertura, horaCierre);
	
		em.persist(reglasNew);
	
	}else {
	throw new ReglasCuposCargadoException("La regla de cupo "+ id +" ya existe en el sistema\n");
		 
	}
		
		
	}

public DtReglasCupos obtenerReglasCupos(String id) throws ReglasCuposNoCargadoException {
	
	
	ReglasCupos regla= em.find(ReglasCupos.class, id);
	
	if (regla==null) {
	
	throw new ReglasCuposNoCargadoException("La regla "+ id +" no existe en el sistema\n");
		 
	}
	else {
		DtReglasCupos dtRegCup = new DtReglasCupos(regla.getId(), regla.getFecha(),regla.getDuracionTurno(), regla.getVacunatorio(), regla.getHoraApertura(), regla.getHoraCierre());
				return dtRegCup;
	}
	
}
public ArrayList<DtReglasCupos> listarReglasCupos()throws ReglasCuposNoCargadosException {


	
	Query query = em.createQuery("SELECT v FROM ReglasCupos v");
	List<ReglasCupos> aux = query.getResultList();
	ArrayList<DtReglasCupos> reg= new ArrayList<DtReglasCupos>();
	
	for(ReglasCupos r: aux ) {
		
		DtReglasCupos dtRegCup = new DtReglasCupos(r.getId(), r.getFecha(),r.getDuracionTurno(), r.getVacunatorio(), r.getHoraApertura(), r.getHoraCierre());
		reg.add(dtRegCup);
	}
	if(aux.isEmpty()) {
throw new ReglasCuposNoCargadosException("No existen reglas en el sistema\n");
	}
	else {
		
		return reg;
	}
	
}

public void modificarReglasCupos(DtReglasCupos dtRegCup) throws ReglasCuposNoCargadoException {
	
	
	ReglasCupos reg= em.find(ReglasCupos.class, dtRegCup.getId());
	
	reg.setFecha(dtRegCup.getFecha());
	reg.setVacunatorio(dtRegCup.getVacunatorio());
	reg.setDuracionTurno(dtRegCup.getDuracionTurno());
	reg.setHoraApertura(dtRegCup.getHoraApertura());
	reg.setHoraCierre(dtRegCup.getHoraCierre());
	em.persist(reg);
				
	}

public void eliminarReglasCupos(String id) throws ReglasCuposNoCargadoException {
	
	
	ReglasCupos reg= em.find(ReglasCupos.class, id);
	
	if (reg==null) {
	
	throw new ReglasCuposNoCargadoException("La regla "+ id +" no existe en el sistema\n");
		 
	}
	else {
		em.remove(reg);
	}
	
}

	
}
