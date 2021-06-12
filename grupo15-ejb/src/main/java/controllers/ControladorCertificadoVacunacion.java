package controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtCertificadoVac;
import datatypes.DtCiudadano;
import datatypes.DtConstancia;
import datatypes.DtReserva;
import entities.CertificadoVacunacion;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Usuario;
import entities.Vacuna;
import exceptions.CertificadoInexistente;
import exceptions.CertificadoRepetido;
import exceptions.ConstanciaInexistente;
import exceptions.UsuarioExistente;
import interfaces.ICertificadoVacunacionDAOLocal;
import interfaces.ICertificadoVacunacionDAORemote;

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

    public void agregarCertificadoVacunacion(int usuario, ArrayList<DtConstancia> constancias) throws CertificadoRepetido, ConstanciaInexistente, UsuarioExistente {
    	Ciudadano u = em.find(Ciudadano.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("No existe ese usuario.");
    	}else {
    		if (u.getCertificado() == null) {
        		//DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss Z", new Locale("us"));
        		CertificadoVacunacion cv = new CertificadoVacunacion();
        		
        		List<ConstanciaVacuna> listConstancias= new ArrayList<ConstanciaVacuna>();
        		for (DtConstancia dtc: constancias) {
        			ConstanciaVacuna c = em.find(ConstanciaVacuna.class, dtc.getIdConstVac());
        			if (c!=null) 
        				listConstancias.add(c);
        			else
        				throw new ConstanciaInexistente("La constancia que se intentó agregar no existe.");
    			}
        		cv.setConstancias(listConstancias);
        		u.setCertificado(cv);
        		em.merge(u);
        		em.merge(cv);
        	}else {
        		throw new CertificadoRepetido("Ya existe un certificado para ese usuario.");
        	}
    	}
    }
    
//    public void modificarCertificadoVacunacion(int usuario, ArrayList<DtConstancia> constancias) throws CertificadoRepetido, CertificadoInexistente, UsuarioExistente, ConstanciaInexistente {
//    	Ciudadano u = em.find(Ciudadano.class, usuario);
//    	if (u==null) {
//    		throw new UsuarioExistente("No existe ese usuario.");
//   	}else {
//    		CertificadoVacunacion cv = u.getCertificado();
//    		if (cv != null) {
//        		
//        		List<ConstanciaVacuna> listConstancias= new ArrayList<ConstanciaVacuna>();
//        		for (DtConstancia dtc: constancias) {
//        			ConstanciaVacuna c = em.find(ConstanciaVacuna.class, dtc.getIdConstVac());
//        			if (c!=null) 
//        				listConstancias.add(c);
//        			else
//        				throw new ConstanciaInexistente("La constancia que se intentó agregar no existe.");
//    			}
//        		cv.setConstancias(listConstancias);
//        		em.merge(cv);
//        	}else {
//        		throw new CertificadoInexistente("No hay un certificado para ese usuario.");
//        	}
//    	}
//  }
	
	public DtCertificadoVac obtenerCertificadoVacunacion(int usuario) throws CertificadoInexistente, UsuarioExistente {
		Ciudadano u = em.find(Ciudadano.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("No existe ese usuario.");
    	}else {
    		CertificadoVacunacion temp = u.getCertificado();
    		if (temp != null) {
    			ArrayList<DtConstancia> dtc= new ArrayList<DtConstancia>();
    			for (ConstanciaVacuna c: temp.getConstancias()) {
    				String vac = c.getVacuna();//obtengo la vacuna para poder pasar la enfermedad
    				System.out.println(vac);
    				Query query = em.createQuery("SELECT v FROM Vacuna v WHERE nombre = :nom");
    				query.setParameter("nom", vac);
    				Vacuna v = (Vacuna) query.getSingleResult();
    				dtc.add(new DtConstancia(c.getIdConstVac(), c.getPeriodoInmunidad(), c.getDosisRecibidas(), c.getFechaUltimaDosis(), c.getVacuna(), 
    						c.getReserva().getDtReserva(), v.getNombre()));
    			}
    			DtCertificadoVac retorno = new DtCertificadoVac(temp.getIdCert(), dtc);

    			return retorno;
    		}else
    			throw new CertificadoInexistente("No hay un certificado para ese usuario.");
    	}
		//TODO:DtReserva esta incompleto, falta Etapa
		
	}
	
	private CertificadoVacunacion getCertificado(int id) {
		CertificadoVacunacion cv = em.find(CertificadoVacunacion.class, id);
		return cv;
	}
	
	private DtCiudadano getDtUsuario(Ciudadano u) {
		if (u!=null)
			return new DtCiudadano(
					u.getIdUsuario(), u.getNombre(), u.getApellido(), u.getFechaNac(), u.getEmail(), u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado());
		else
			return null;
	}
}
