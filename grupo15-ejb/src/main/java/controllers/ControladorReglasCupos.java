package controllers;

import java.time.LocalTime;
import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DtReglasCupos;
import entities.ReglasCupos;
import exceptions.ReglasCuposCargadoException;
import exceptions.ReglasCuposNoCargadoException;
import exceptions.ReglasCuposNoCargadosException;
import interfaces.IControladorReglasCuposLocal;
import interfaces.IControladorReglasCuposRemote;

@Stateless
public class ControladorReglasCupos implements IControladorReglasCuposLocal, IControladorReglasCuposRemote {
	@PersistenceContext(name = "test")
	private EntityManager em;

	public void agregarReglasCupos(String id, Integer duracionTurno, LocalTime horaApertura, LocalTime horaCierre)
			throws ReglasCuposCargadoException {

		ReglasCupos reglas = em.find(ReglasCupos.class, id);

		if (reglas == null) {
			// ReglasCupos reglasNew= new ReglasCupos(id, fecha, duracionTurno,
			// horaApertura, horaCierre);
			ReglasCupos reglasNew = new ReglasCupos(id, duracionTurno, horaApertura, horaCierre);
			em.persist(reglasNew);

		} else {
			throw new ReglasCuposCargadoException("La regla de cupo " + id + " ya existe en el sistema\n");

		}

	}

	public DtReglasCupos obtenerReglasCupos(String id) throws ReglasCuposNoCargadoException {
		/*
		 * 
		 * ReglasCupos regla= em.find(ReglasCupos.class, id);
		 * 
		 * if (regla==null) {
		 * 
		 * throw new ReglasCuposNoCargadoException("La regla "+ id
		 * +" no existe en el sistema\n");
		 * 
		 * } else { DtReglasCupos dtRegCup = new DtReglasCupos(regla.getId(),
		 * regla.getDuracionTurno(), regla.getHoraApertura(), regla.getHoraCierre());
		 * return dtRegCup; }
		 */
		return null;
	}

	public ArrayList<DtReglasCupos> listarReglasCupos() throws ReglasCuposNoCargadosException {

		/*
		 * 
		 * Query query = em.createQuery("SELECT v FROM ReglasCupos v");
		 * List<ReglasCupos> aux = query.getResultList(); ArrayList<DtReglasCupos> reg=
		 * new ArrayList<DtReglasCupos>();
		 * 
		 * for(ReglasCupos r: aux ) {
		 * 
		 * DtReglasCupos dtRegCup = new DtReglasCupos(r.getId(),r.getDuracionTurno(),
		 * r.getHoraApertura(), r.getHoraCierre()); reg.add(dtRegCup); }
		 * if(aux.isEmpty()) { throw new
		 * ReglasCuposNoCargadosException("No existen reglas en el sistema\n"); } else {
		 * 
		 * return reg; }
		 */
		return null;
	}

	public void modificarReglasCupos(DtReglasCupos dtRegCup) throws ReglasCuposNoCargadoException {

		/*
		 * ReglasCupos reg= em.find(ReglasCupos.class, dtRegCup.getId());
		 * 
		 * reg.setDuracionTurno(dtRegCup.getDuracionTurno());
		 * reg.setHoraApertura(dtRegCup.getHoraApertura());
		 * reg.setHoraCierre(dtRegCup.getHoraCierre()); em.persist(reg);
		 */

	}

	public void eliminarReglasCupos(String id) throws ReglasCuposNoCargadoException {

		/*
		 * ReglasCupos reg= em.find(ReglasCupos.class, id);
		 * 
		 * if (reg==null) {
		 * 
		 * throw new ReglasCuposNoCargadoException("La regla "+ id
		 * +" no existe en el sistema\n");
		 * 
		 * } else { em.remove(reg); }
		 */
	}

}
