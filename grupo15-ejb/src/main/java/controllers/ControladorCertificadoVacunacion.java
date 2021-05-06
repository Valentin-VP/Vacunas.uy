package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DtCertificadoVac;
import datatypes.DtCiudadano;
import datatypes.DtConstancia;
import datatypes.DtReserva;
import entities.CertificadoVacunacion;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Usuario;
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
    	Usuario u = em.find(Usuario.class, usuario);
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
        		em.persist(u);
        		em.persist(cv);
        	}else {
        		throw new CertificadoRepetido("Ya existe un certificado para ese usuario.");
        	}
    	}
    }
    
    public void modificarCertificadoVacunacion(int usuario, ArrayList<DtConstancia> constancias) throws CertificadoRepetido, CertificadoInexistente, UsuarioExistente, ConstanciaInexistente {
    	Usuario u = em.find(Usuario.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("No existe ese usuario.");
    	}else {
    		CertificadoVacunacion cv = u.getCertificado();
    		if (cv != null) {
        		
        		List<ConstanciaVacuna> listConstancias= new ArrayList<ConstanciaVacuna>();
        		for (DtConstancia dtc: constancias) {
        			ConstanciaVacuna c = em.find(ConstanciaVacuna.class, dtc.getIdConstVac());
        			if (c!=null) 
        				listConstancias.add(c);
        			else
        				throw new ConstanciaInexistente("La constancia que se intentó agregar no existe.");
    			}
        		cv.setConstancias(listConstancias);
        		em.merge(cv);
        	}else {
        		throw new CertificadoInexistente("No hay un certificado para ese usuario.");
        	}
    	}
    }
	
	public DtCertificadoVac obtenerCertificadoVacunacion(int usuario) throws CertificadoInexistente, UsuarioExistente {
		Usuario u = em.find(Usuario.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("No existe ese usuario.");
    	}else {
    		CertificadoVacunacion temp = u.getCertificado();
    		if (temp != null) {
    			ArrayList<DtConstancia> dtc= new ArrayList<DtConstancia>();
    			for (ConstanciaVacuna c: temp.getConstancias()) {
    				dtc.add(new DtConstancia(c.getIdConstVac(), c.getPeriodoInmunidad(), c.getDosisRecibidas(), c.getFechaUltimaDosis(), c.getVacuna(), 
    						new DtReserva(c.getReserva().getEstado(), getDtUsuario(c.getReserva().getCiudadano()), c.getReserva().getFechaRegistro(), c.getReserva().getPuesto().getId(), c.getReserva().getPuesto().getVacunatorio().getNombre(),
    								c.getReserva().getEtapa().toDtEtapa().getFechaInicio(), c.getReserva().getEtapa().toDtEtapa().getFechaFin(), c.getReserva().getEtapa().toDtEtapa().getDtPvac().getNombre(), c.getReserva().getEtapa().getId())));
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
