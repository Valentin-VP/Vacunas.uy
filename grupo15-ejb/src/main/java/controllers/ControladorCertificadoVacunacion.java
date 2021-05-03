package controllers;

import interfaces.ICertificadoVacunacionDAOLocal;
import interfaces.ICertificadoVacunacionDAORemote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtCertificadoVac;
import datatypes.DtConstancia;
import datatypes.DtReserva;
import entities.CertificadoVacunacion;
import entities.ConstanciaVacuna;
import exceptions.CertificadoInexistente;

/**
 * Session Bean implementation class ControladorCertificadoVacunacion
 */
@Stateless
@LocalBean
public class ControladorCertificadoVacunacion implements ICertificadoVacunacionDAORemote, ICertificadoVacunacionDAOLocal {
	@PersistenceContext(name = "test")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public ControladorCertificadoVacunacion() {
        // TODO Auto-generated constructor stub
    }

    /*public void agregarCertificadoVacunacion(int id, ArrayList<DtConstancia> constancias) throws CertificadoRepetido {
    	if (getCertificado(id) == null) {
    		//DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss Z", new Locale("us"));
    		Agenda r = new Agenda(id, fecha);
    		List<Cupo> listCupos= new ArrayList<Cupo>();
    		for (DtCupo dtc: cupos) {
    			listCupos.add(new Cupo(dtc.getIdCupo(), dtc.isOcupado()));
			}
    		r.setCupos(listCupos);
    		em.persist(r);
    	}else {
    		throw new CertificadoRepetido("Ya existe una agenda con ese ID.");
    	}
    }*/
	
	public DtCertificadoVac obtenerCertificadoVacunacion(int id) throws CertificadoInexistente {
		CertificadoVacunacion temp = getCertificado(id);
		//TODO:DtReserva esta incompleto, falta Etapa
		if (temp!=null) {
			ArrayList<DtConstancia> dtc= new ArrayList<DtConstancia>();
			for (ConstanciaVacuna c: temp.getConstancias()) {
				dtc.add(new DtConstancia(c.getIdConstVac(), c.getPeriodoInmunidad(), c.getDosisRecibidas(), c.getFechaUltimaDosis(), c.getVacuna(), 
						new DtReserva(c.getReserva().getIdReserva(), c.getReserva().getEstado(), c.getReserva().getNombreUser(), c.getReserva().getFechaRegistro())));
			}
			DtCertificadoVac retorno = new DtCertificadoVac(temp.getIdCert(), dtc);

			return retorno;
		}else
			throw new CertificadoInexistente("No hay una agenda con ese ID.");
	}
	
	private CertificadoVacunacion getCertificado(int id) {
		CertificadoVacunacion cv = em.find(CertificadoVacunacion.class, id);
		return cv;
	}
}
