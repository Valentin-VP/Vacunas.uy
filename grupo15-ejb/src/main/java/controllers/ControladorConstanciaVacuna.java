package controllers;

import interfaces.IConstanciaVacunaDAOLocal;
import interfaces.IConstanciaVacunaDAORemote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtAgenda;
import datatypes.DtConstancia;
import datatypes.DtCupo;
import datatypes.DtReserva;
import datatypes.DtUsuario;
import entities.Agenda;
import entities.CertificadoVacunacion;
import entities.ConstanciaVacuna;
import entities.Cupo;
import entities.Reserva;
import entities.Usuario;
import exceptions.AgendaInexistente;
import exceptions.CertificadoInexistente;
import exceptions.ConstanciaInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioExistente;

/**
 * Session Bean implementation class ControladorConstanciaVacuna
 */
@Stateless
@LocalBean
public class ControladorConstanciaVacuna implements IConstanciaVacunaDAORemote, IConstanciaVacunaDAOLocal {
	@PersistenceContext(name = "test")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public ControladorConstanciaVacuna() {
        // TODO Auto-generated constructor stub
    }
    
    public void agregarConstanciaVacuna(String vacuna, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis, DtReserva reserva) throws UsuarioExistente, ReservaInexistente, CertificadoInexistente {
    	Usuario u = em.find(Usuario.class, reserva.getUsuario().getIdUsuario());
    	if (u==null)
    		throw new UsuarioExistente("No existe ese usuario.");
    	else {
    		Reserva r = buscarReservaUsuarioEtapa(reserva);
    		if (r==null) {
    			throw new ReservaInexistente("No existe una reserva para ese usuario y esa etapa.");
    		}else {
    			CertificadoVacunacion cert = u.getCertificado();
    			if (cert==null) {
    				throw new CertificadoInexistente("No existe un certificado para ese usuario.");
    			}else {
    				ConstanciaVacuna cv = new ConstanciaVacuna(periodoInmunidad, dosisRecibidas, fechaUltimaDosis, vacuna, r);
    				cert.getConstancias().add(cv);
    				em.persist(cert);
    				em.persist(cv);
    			}
    			
    		}
    			
    	}
    }

    public void modificarConstanciaVacuna(int idConst, String vacuna, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis, DtReserva reserva) throws UsuarioExistente, ReservaInexistente, CertificadoInexistente, ConstanciaInexistente {
    	ConstanciaVacuna cv = em.find(ConstanciaVacuna.class, idConst);
    	if (cv==null)
    		throw new ConstanciaInexistente("No existe una constancia con ese ID.");
    	else {
	    	Usuario u = em.find(Usuario.class, reserva.getUsuario().getIdUsuario());
	    	if (u==null)
	    		throw new UsuarioExistente("No existe ese usuario.");
	    	else{
	    		Reserva r = buscarReservaUsuarioEtapa(reserva);
	    		if (r==null) {
	    			throw new ReservaInexistente("No existe una reserva para ese usuario y esa etapa.");
	    		}else {
	    			CertificadoVacunacion cert = u.getCertificado();
	    			if (cert==null) {
	    				throw new CertificadoInexistente("No existe un certificado para ese usuario.");
	    			}else {
	    				cv.setVacuna(vacuna);
	    				cv.setPeriodoInmunidad(periodoInmunidad);
	    				cv.setDosisRecibidas(dosisRecibidas);
	    				cv.setFechaUltimaDosis(fechaUltimaDosis);
	    				
	    				em.merge(cv);
	    			}
	    			
	    		}
		    			
		    }
    	}
    }
    
    public DtConstancia obtenerConstancia(int idConst) throws ConstanciaInexistente {
    	DtConstancia retorno;
    	ConstanciaVacuna temp = em.find(ConstanciaVacuna.class, idConst);
    	if (temp==null)
    		throw new ConstanciaInexistente("No existe una constancia con ese ID.");
    	else {
    		Reserva r = temp.getReserva();
    		retorno = new DtConstancia(temp.getIdConstVac(), temp.getDosisRecibidas(), temp.getPeriodoInmunidad(), temp.getFechaUltimaDosis(),  temp.getVacuna(),
    				new DtReserva(r.getEstado(), getDtUsuario(r.getUsuario()), r.getFechaRegistro(), r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
        					r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(), r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId()));
    		return retorno;
    	}
    }
    
    public ArrayList<DtConstancia> listarConstancias() throws ConstanciaInexistente{
    	 ArrayList<DtConstancia> retorno = new ArrayList<>();
    	 Query query = em.createQuery("SELECT cv FROM constanciavacuna cv");
 		@SuppressWarnings("unchecked")
 		ArrayList<ConstanciaVacuna> result = (ArrayList<ConstanciaVacuna>) query.getResultList();
 		if (result!=null) {
 			
			for (ConstanciaVacuna cv: result) {
				
				Reserva r = cv.getReserva();
				retorno.add(new DtConstancia(cv.getIdConstVac(), cv.getDosisRecibidas(), cv.getPeriodoInmunidad(), cv.getFechaUltimaDosis(),  cv.getVacuna(),
	    				new DtReserva(r.getEstado(), getDtUsuario(r.getUsuario()), r.getFechaRegistro(), r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
	        					r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(), r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId())));
			}
			return retorno;
		}else {
			throw new ConstanciaInexistente("No hay constancias.");
		}
    	 
    }
    
	private Reserva buscarReservaUsuarioEtapa(DtReserva res){
		Usuario u = em.find(Usuario.class, res.getUsuario().getIdUsuario());
		if (u!=null) {
			ArrayList<Reserva> temp = (ArrayList<Reserva>) u.getReservas();
			for (Reserva r: temp) {
				if (r.getEtapa().getId() == res.getEtapa())
					return r;
			}
		}
		return null;
	}
    
	private DtUsuario getDtUsuario(Usuario u) {
		if (u!=null)
			return new DtUsuario(u.getNombre(), u.getApellido(), u.getFechaNac(), u.getIdUsuario(), u.getEmail(), u.getDireccion(), u.getSexo());
		else
			return null;
	}
}
