package controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import datatypes.DtPuesto;
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
    			Puesto pLibre = null;
    			for (Puesto p: listaPuesto) { //recorro puestos de vacunatorio
    				if (p.getAsignado() == null) {	//si hay uno sin vacunador asignado, lo tomo
    					pLibre = p;
    					break;
    				}
    			}
    			if (pLibre == null) { 
    				throw new SinPuestosLibres(
    						String.format("No existe un puesto libre en el vacunatorio %s (ID: %s)", vact.getNombre(), vact.getId()));
    			}else { //si habia alguno libre
    				if (fecha.before(Date.from(Instant.now()))) //es la fecha que me pasaron anterior a la actual?
    					throw new FechaIncorrecta("La fecha es anterior a la actual.");
    				else {
    					if (u.getAsignado()!=null) {
    						u.getAsignado().getPuesto().setAsignado(null); //desvinculo el [Asignado] del [Puesto] previo asociado a ese usuario
    						u.getAsignado().setPuesto(null); //desvinculo el [Puesto] del [Asignado] asociado a ese usuario
    						u.getAsignado().setVacunador(null); //desvinculo el [Vacunador] del [Asignado] asociado a ese usuario
    					}
    					Asignado assign = new Asignado(fecha, u, pLibre);
    					pLibre.setAsignado(assign);
        				u.setAsignado(assign);
        				em.merge(u);
        				em.merge(pLibre);
    				}
    			}
    		}
    	}
    	
    }
    
    public DtPuesto consultarPuestoAsignadoVacunador(int idVacunador, String idVacunatorio) throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar {
    	Vacunador u = em.find(Vacunador.class, idVacunador);
    	if (u==null) {
    		throw new UsuarioInexistente("El vacunador no existe.");
    	}else {
    		Vacunatorio vact = em.find(Vacunatorio.class, idVacunatorio);
    		if (vact == null) {
    			throw new VacunatorioNoCargadoException("El vacunatorio no existe.");
    		}else {
    			if (u.getAsignado()==null)
    				throw new VacunadorSinAsignar("El vacunador no tiene un puesto asignado.");//return null; 
    			else {
    				Puesto p = u.getAsignado().getPuesto();
    				DtPuesto dt = new DtPuesto(p.getId(), p.getVacunatorio()); //TODO: DtVacunatorio en vez de Vacunatorio (en DtPuesto)
    				return dt;
    			}
    		}
    	}
    }
}
