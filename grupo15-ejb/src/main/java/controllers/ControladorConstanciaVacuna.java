package controllers;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import entities.CertificadoVacunacion;
import entities.Ciudadano;
import entities.ConstanciaVacuna;
import entities.Reserva;
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
    	if (u==null)
    		throw new UsuarioExistente("No existe ese usuario.");
    	else {
    		Reserva r = buscarReservaUsuarioEtapa(idUser, idEtapa);
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
/*
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
    */
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
    	 Query query = em.createQuery("SELECT cv FROM ConstanciaVacuna cv");
 		@SuppressWarnings("unchecked")
 		ArrayList<ConstanciaVacuna> result = (ArrayList<ConstanciaVacuna>) query.getResultList();
 		System.out.println(result.size());
 		if (result!=null && !result.isEmpty()) {
 			
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
			List<Reserva> temp = c.getReservas();
			for (Reserva r: temp) {
				if (r.getEtapa().getId() == idEtapa)
					return r;
			}
		}
		return null;
	}
	
	//retorna el numero de constancias en este periodo no las constancias
	public int listarConstanciasPeriodo(int dias) {
		Query query = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE fechaUltimaDosis BETWEEN :start AND :end");
		query.setParameter("start", LocalDate.now().minusDays(dias));
		query.setParameter("end", LocalDate.now());
		
		ArrayList<ConstanciaVacuna> result = (ArrayList<ConstanciaVacuna>) query.getResultList();
		int retorno = result.size();
		return retorno;
	}
	
	//retorna las vacunas de una enfermedad
	@SuppressWarnings("unchecked")
	private ArrayList<Vacuna> getVacunasEnfermedad(String enfermedad){
		Query query1 = em.createQuery("SELECT v FROM Vacuna v WHERE enfermedad_nombre = :enf");
		query1.setParameter("enf", enfermedad);
		return (ArrayList<Vacuna>) query1.getResultList();
	}
	
	public Map<String, String> listarConstanciaPorVacuna(){
		Map<String, String> constancias = new HashMap<String,String>();
		Query query1 = em.createQuery("SELECT nombre FROM Vacuna");
		@SuppressWarnings("unchecked")
		ArrayList<String> vacunas = (ArrayList<String>)query1.getResultList();
		for(String v: vacunas) {
			System.out.println(v);
			Query query2 = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE vacuna = :vac");
			query2.setParameter("vac", v);
			constancias.put(v, String.valueOf(query2.getResultList().size()));
		}
		return constancias;
	}
	
	public Map<String, String> listarConstanciaPorEnfermedad(){
		Map<String, String> constancias = new HashMap<String,String>();
		Query query1 = em.createQuery("SELECT nombre FROM Enfermedad");
		@SuppressWarnings("unchecked")
		ArrayList<String> enfermedades = (ArrayList<String>)query1.getResultList();//obtengo las enfermedades
		for(String enf: enfermedades) {
			ArrayList<Vacuna> vacunas = getVacunasEnfermedad(enf);
			int vacunados = 0;
			for(Vacuna v: vacunas) {
				Query query2 = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE vacuna = :vac");
				query2.setParameter("vac", v.getNombre());
				vacunados = vacunados + query2.getResultList().size();
			}
			constancias.put(enf, String.valueOf(vacunados));
		}
		return constancias;
	}
	
	public Map<String, String> constanciasPorMes(){
		Map<String, String> constancias = new HashMap<String,String>();
		for(int i=1; i<=12; i++) {
			Query query = em.createQuery("SELECT p FROM ConstanciaVacuna p WHERE EXTRACT(MONTH FROM fechaultimadosis) = :mes");
			query.setParameter("mes", i);
			constancias.put(String.valueOf(i), String.valueOf(query.getResultList().size()));
		}
		return constancias;
	}
	
	public Map<String, String> constanciasPorSexo(){
		Map<String, String> constancias = new HashMap<String,String>();
		int masculino=0;
		int femenino=0;
		int otro=0;
		Query query = em.createNativeQuery("SELECT reserva_ciudadano_idusuario FROM ConstanciaVacuna");
		ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();
		System.out.println(result);
		for(Integer x: result) {
			Query query2 = em.createNativeQuery("SELECT sexo FROM Ciudadano WHERE idusuario = :ci");
			query2.setParameter("ci", x);
			ArrayList<Integer> sexo = (ArrayList<Integer>) query2.getResultList();
			switch(sexo.get(0)){
				case 0:
					femenino = femenino+1;
					break;
				case 1:
					masculino = masculino+1;
					break;
				case 2:
					otro = otro+1;
					break;
			}
		}
		constancias.put("femenino", String.valueOf(femenino));
		constancias.put("masculino", String.valueOf(masculino));
		constancias.put("otro", String.valueOf(otro));
		return constancias;
	}
	
	public Map<String,String> constanciasPorEdad(){
		Map<String, String> constancias = new HashMap<String,String>();
		int adolescentes = 0;
		int jovenes = 0;
		int adultos = 0;
		int mayores = 0;
		Query query = em.createNativeQuery("SELECT reserva_ciudadano_idusuario FROM ConstanciaVacuna");
		ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();
		for(Integer x: result) {
			Query query2 = em.createNativeQuery("SELECT fechanac FROM Ciudadano WHERE idusuario = :ci");
			query2.setParameter("ci", x);
			ArrayList<Date> fechanac = (ArrayList<Date>) query2.getResultList();
			for(Date f: fechanac) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String fechaInicio = dateFormat.format(f); 
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate nac = LocalDate.parse(fechaInicio, formatter);
				int edad = Period.between(nac, LocalDate.now()).getYears();
				if(edad<18)
					adolescentes = adolescentes+1;
				if(edad>=18 && edad<45)
					jovenes = jovenes+1;
				if(edad>=45 && edad<61)
					adultos = adultos+1;
				if(edad>=60)
					mayores = mayores+1;
			}	
		}
		constancias.put("adolescentes", String.valueOf(adolescentes));
		constancias.put("jovenes", String.valueOf(jovenes));
		constancias.put("adultos", String.valueOf(adultos));
		constancias.put("mayores", String.valueOf(mayores));
		System.out.println(constancias);
		return constancias;
	}
	
	
	//retorna el numero de constancias en este periodo para una vacuna no las constancias
	public int filtroPorVacuna(int dias, String vacuna) {
		Query query = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE vacuna = :vac AND fechaUltimaDosis BETWEEN :start AND :end");
		query.setParameter("start", LocalDate.now().minusDays(dias));
		query.setParameter("end", LocalDate.now());
		query.setParameter("vac", vacuna);
		
		ArrayList<ConstanciaVacuna> result = (ArrayList<ConstanciaVacuna>) query.getResultList();
		int retorno = result.size();
		System.out.println();
		return retorno;
	}
	
	
	//retorna el numero de constancias en este periodo para una enfermedad no las constancias
	public int filtroPorEnfermedad(int dias, String enfermedad) {
		ArrayList<ConstanciaVacuna> constancias = new ArrayList<ConstanciaVacuna>();
		//primero obtengo las vacunas de la enfermedad
		ArrayList<Vacuna> vacunas = getVacunasEnfermedad(enfermedad);
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
	
	//retorna el numero de constancias en este periodo para un plan
	public int filtroPorPlan(int dias, String plan) {
		Query query = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE reserva_etapa_planvacunacion_id = :plan AND fechaUltimaDosis BETWEEN :start AND :end");
		query.setParameter("start", LocalDate.now().minusDays(dias));
		query.setParameter("end", LocalDate.now());
		query.setParameter("plan", Integer.valueOf(plan));
		
		ArrayList<ConstanciaVacuna> constancias = (ArrayList<ConstanciaVacuna>) query.getResultList();
		return constancias.size();
	}
	
	public int filtroPorPlanYVacuna(int dias, String plan, String vacuna) {
		Query query = em.createQuery("SELECT c FROM ConstanciaVacuna c WHERE reserva_etapa_planvacunacion_id = :plan AND vacuna = :vac AND fechaUltimaDosis BETWEEN :start AND :end");
		query.setParameter("start", LocalDate.now().minusDays(dias));
		query.setParameter("end", LocalDate.now());
		query.setParameter("plan", Integer.valueOf(plan));
		query.setParameter("vac", vacuna);
		
		ArrayList<ConstanciaVacuna> constancias = (ArrayList<ConstanciaVacuna>) query.getResultList();
		return constancias.size();
	}
	
}
