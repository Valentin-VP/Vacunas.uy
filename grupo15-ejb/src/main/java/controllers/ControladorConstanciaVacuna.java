package controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtCiudadano;
import datatypes.DtConstancia;
import datatypes.DtReserva;
import entities.CertificadoVacunacion;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Reserva;
import entities.Usuario;
import entities.Vacuna;
import exceptions.CertificadoInexistente;
import exceptions.ConstanciaInexistente;
import exceptions.ReservaInexistente;
import exceptions.UsuarioExistente;
import interfaces.IConstanciaVacunaDAOLocal;
import interfaces.IConstanciaVacunaDAORemote;

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
    
    public void agregarConstanciaVacuna(String vacuna, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis, int idUser, int idEtapa) throws UsuarioExistente, ReservaInexistente, CertificadoInexistente {
    	Ciudadano u = em.find(Ciudadano.class, idUser);
    	System.out.println("##################################");
    	if (u==null)
    		throw new UsuarioExistente("No existe ese usuario.");
    	else {
    		System.out.println("##################################");
    		Reserva r = buscarReservaUsuarioEtapa(idUser, idEtapa);
    		System.out.println("##################################");
    		if (r==null) {
    			throw new ReservaInexistente("No existe una reserva para ese usuario y esa etapa.");
    		}else {
    			CertificadoVacunacion cert = u.getCertificado();
    			System.out.println("##################################");
    			if (cert==null) {
    				throw new CertificadoInexistente("No existe un certificado para ese usuario.");
    			}else {
    				System.out.println("##################################");
    				ConstanciaVacuna cv = new ConstanciaVacuna(periodoInmunidad, dosisRecibidas, fechaUltimaDosis, vacuna, r);
    				System.out.println("##################################");
    				cert.getConstancias().add(cv);
    				System.out.println("##################################");
    				em.persist(cert);
    				em.persist(cv);
    			}
    			
    		}
    			
    	}
    }

    public void modificarConstanciaVacuna(int idConst, String vacuna, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis, int idUser, int idEtapa) throws UsuarioExistente, ReservaInexistente, CertificadoInexistente, ConstanciaInexistente {
    	ConstanciaVacuna cv = em.find(ConstanciaVacuna.class, idConst);
    	if (cv==null)
    		throw new ConstanciaInexistente("No existe una constancia con ese ID.");
    	else {
    		Ciudadano u = em.find(Ciudadano.class, idUser);
	    	if (u==null)
	    		throw new UsuarioExistente("No existe ese usuario.");
	    	else{
	    		Reserva r = buscarReservaUsuarioEtapa(idUser, idEtapa);
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
    				r.getDtReserva());
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
						r.getDtReserva()));
			}
			return retorno;
		}else {
			throw new ConstanciaInexistente("No hay constancias.");
		}
    	 
    }
    
	private Reserva buscarReservaUsuarioEtapa(int idUser, int idEtapa){
		Ciudadano c = em.find(Ciudadano.class, idUser);
		if (c!=null) {
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			List<Reserva> temp = c.getReservas();
			for (Reserva r: temp) {
				if (r.getEtapa().getId() == idEtapa)
					return r;
			}
		}
		return null;
	}
    
	private DtCiudadano getDtUsuario(Ciudadano u) {
		if (u!=null)
			return new DtCiudadano(
					u.getIdUsuario(), u.getNombre(), u.getApellido(), u.getFechaNac(), u.getEmail(), u.getDireccion(), u.getSexo(), u.getTipoSector(), u.isAutenticado());
		else
			return null;
	}
	
	//retorna el numero de constancias en este periodo no las constancias
	public int listarConstanciasPeriodo(int dias) {
		Query query = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE fechaUltimaDosis BETWEEN :start AND :end");
		query.setParameter("start", LocalDate.now().minusDays(dias));
		query.setParameter("end", LocalDate.now());
		
		ArrayList<ConstanciaVacuna> result = (ArrayList<ConstanciaVacuna>) query.getResultList();
		int retorno = result.size();
		System.out.println(retorno);
		return retorno;
	}
	
	//retorna el numero de constancias en este periodo para una enfermedad no las constancias
	public int listarConstanciasPeriodoEnfermedad(int dias, String enfermedad) {
		ArrayList<ConstanciaVacuna> constancias = new ArrayList<ConstanciaVacuna>();
		
		//primero obtengo las vacunas de la enfermedad
		Query query1 = em.createQuery("SELECT v FROM Vacuna v WHERE enfermedad_nombre = :enf");
		query1.setParameter("enf", enfermedad);
		@SuppressWarnings("unchecked")
		ArrayList<Vacuna> vacunas = (ArrayList<Vacuna>) query1.getResultList();
		
		for(int i=0; i<vacunas.size(); i++) {
			Query query2 = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE vacuna = :vac AND fechaUltimaDosis BETWEEN :start AND :end");
			query2.setParameter("vac", vacunas.get(i).getNombre());
			query2.setParameter("start", LocalDate.now().minusDays(dias));
			query2.setParameter("end", LocalDate.now());
			ArrayList<ConstanciaVacuna> result = (ArrayList<ConstanciaVacuna>) query2.getResultList();
			constancias.addAll(result);
		}
		
			return constancias.size();
	}
	
	public Map<String, String> listarConstanciaPorVacuna(){
		Map<String, String> constancias = new HashMap<>(); 
		Query query1 = em.createQuery("SELECT nombre FROM Vacuna");
		@SuppressWarnings("unchecked")
		ArrayList<String> vacunas = (ArrayList<String>)query1.getResultList();
		for(String v: vacunas) {
			Query query2 = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE nombre = :vac");
			query2.setParameter("vac", v);
			constancias.put(v, String.valueOf(query2.getResultList().size()));
		}
		return constancias;
	}
	
}
