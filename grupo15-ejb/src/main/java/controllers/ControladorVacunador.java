package controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DtAsignado;
import datatypes.DtPuesto;
import datatypes.DtVacunatorio;
import entities.Asignado;
import entities.Puesto;
import entities.Vacunador;
import entities.Vacunatorio;
import exceptions.FechaIncorrecta;
import exceptions.SinPuestosLibres;
import exceptions.UsuarioInexistente;
import exceptions.VacunadorSinAsignar;
import exceptions.VacunatorioNoCargadoException;
import interfaces.IControladorVacunadorLocal;
import interfaces.IControladorVacunadorRemote;

/**
 * Session Bean implementation class ControladorVacunador
 */
@Stateless
@LocalBean


public class ControladorVacunador implements IControladorVacunadorRemote, IControladorVacunadorLocal {
	@PersistenceContext(name = "test")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public ControladorVacunador() {
        // TODO Auto-generated constructor stub
    }
    
  //para asignar un puesto libre, debo recorrer cada puesto del vacunatorio que me pasan, y ver entre sus Asignados que no esté esa fecha
    //si no lo tiene, elijo ese puesto, y asignado queda linkeado a los dos
    //si la tiene, paso al siguiente y hago lo mismo hasta encontrar alguna (o ninguna)
    //debo comprobar de eliminar la asignacion de ese vacunador en esa fecha (si es que la tenia),
    //recorriendo sus asignados previamente, antes de asignarlo a ese puesto (solo si es del mismo vacunatorio¿?¿?¿?¿?¿¿)
    
    
    public void asignarVacunadorAVacunatorio(int idVacunador, String idVacunatorio, Date fecha) throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta {
    	Vacunador u = em.find(Vacunador.class, idVacunador);
    	if (u==null) {
    		throw new UsuarioInexistente("El vacunador no existe.");
    	}else {
    		Vacunatorio vact = em.find(Vacunatorio.class, idVacunatorio);
    		if (vact == null) {
    			throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
    		}else {
    			ArrayList<Puesto> listaPuesto = (ArrayList<Puesto>) vact.getPuesto();
    			Puesto pLibre = getPuestoLibreEnFecha(listaPuesto, fecha);
    			if (pLibre == null) { 
    				throw new SinPuestosLibres(
    						String.format("No existe un puesto libre en esa fecha en el vacunatorio %s.", vact.getNombre()));
    			}else { //si habia alguno libre
    				if (!fecha.after(Date.from(Instant.now()))) //es la fecha que me pasaron anterior a la actual?
    					throw new FechaIncorrecta("La fecha es anterior a la actual.");
    				else {
    					Asignado asignadoPrevioEnFecha = getAsignadoEnFecha(u.getAsignado(), fecha);
    					if (asignadoPrevioEnFecha != null) {
    						asignadoPrevioEnFecha.getPuesto().getAsignado().remove(asignadoPrevioEnFecha);
    						asignadoPrevioEnFecha.getVacunador().getAsignado().remove(asignadoPrevioEnFecha);
    					}
    					
    					Asignado assign = new Asignado(fecha, u, pLibre);
    					pLibre.getAsignado().add(assign);
        				u.getAsignado().add(assign);
        				
        				em.merge(u);
        				em.merge(pLibre);
    				}
    			}
    		}
    	}
    	
    }
    
    public DtAsignado consultarPuestoAsignadoVacunador(int idVacunador, String idVacunatorio, Date fecha) throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar {
    	Vacunador u = em.find(Vacunador.class, idVacunador);
    	if (u==null) {
    		throw new UsuarioInexistente("El vacunador no existe.");
    	}else {
    		Vacunatorio vact = em.find(Vacunatorio.class, idVacunatorio);
    		if (vact == null) {
    			throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
    		}else {
    			Asignado a = getAsignadoEnFecha(u.getAsignado(), fecha);
    			
    			if (a==null)
    				throw new VacunadorSinAsignar("El vacunador no tiene un puesto asignado en esa fecha.");//return null; 
    			else {
    				Puesto p = a.getPuesto();
    				if (p.getVacunatorio().equals(vact)) {
    					DtAsignado dt = new DtAsignado(fecha, p.getId());
    					return dt;
    				}else {
    					throw new VacunadorSinAsignar("El vacunador no tiene un puesto asignado en esa fecha en el vacunatorio.");//return null; 
    				}
    				
    			}
    		}
    	}
    }
    
	private DtVacunatorio getDtVacunatorio(Vacunatorio v) {
		return new DtVacunatorio(v.getId(), v.getNombre(), v.getDtDir(), v.getTelefono(), v.getLatitud(), v.getLongitud());
	}
	
	private Puesto getPuestoLibreEnFecha(List<Puesto> puestos, Date fecha) {
		for (Puesto temp: puestos) {
			if (getAsignadoEnFecha(temp.getAsignado(), fecha)==null);
				return temp;
		}
		return null;
	}
	
	private boolean existeFechaEnListaAsignados(List<Asignado> asignados, Date fecha) {
		for (Asignado a: asignados) {
			if (a.getFecha().equals(fecha))
				return true;
		}
		return false;
	}
	
	private Asignado getAsignadoEnFechaDeUnVacunatorio(List<Asignado> asignados, Date fecha, Vacunatorio v) {
		for (Asignado a: asignados) {
			if (a.getFecha().equals(fecha) && a.getPuesto().getVacunatorio().equals(v))
				return a;
		}
		return null;
	}
	
	private Asignado getAsignadoEnFecha(List<Asignado> asignados, Date fecha) {
		for (Asignado a: asignados) {
			if (a.getFecha().equals(fecha))
				return a;
		}
		return null;
	}
}
