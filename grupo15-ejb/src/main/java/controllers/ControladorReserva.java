package controllers;

import interfaces.IReservaDAOLocal;
import interfaces.IReservaDAORemote;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtReserva;
import datatypes.DtUsuario;
import datatypes.EstadoReserva;
import entities.Cupo;
import entities.Etapa;
import entities.Puesto;
import entities.Reserva;
import entities.Usuario;
import exceptions.PuestoNoCargadoException;
import exceptions.ReservaInexistente;
import exceptions.ReservaRepetida;
import exceptions.UsuarioExistente;

/**
 * Session Bean implementation class ControladorReserva
 */

//TODO:No estoy considerando Etapa y Usuario, ni Puesto (y soy un Tipo Asociativo) => TODO: cambiar nombreUser por la entidad
/*TODO:Constancia puede modificarse a si mismo, debe agregarse 1 reserva, puede agregar (o modificar) * lotedosis.
No puede crearse, ni destruirse (por si propio), porque dejaria huerfano a su link actual con Certificado.

Agenda puede modificarse a si mismo, puede agregar (o modificar/eliminar) * reservas, puede agregar (o modificar/eliminar) * cupos.
No puede crearse, ni destruirse (por si propio), porque dejaria huerfano a su link actual con Vacunatorio.
[Si un Vacunatorio lo destruye, se debera destruir los cupos asociados a la Agenda]

Certificado no puede modificarse a si mismo, puede agregar (o modificar/eliminar) * Constancias.
No puede crearse, ni destruirse (por si propio), porque dejaria huerfano a su link actual con Usuario.
[Si un Usuario lo destruye, se debera destruir las Constancias asociados al Certificado]

Reserva puede modificarse a si mismo, puede agregar (o modificar/eliminar) 1 puesto.
No puede crearse, ni destruirse (por si propio), porque es un tipo asociativo.

Cupo puede modificarse a si mismo.
No puede crearse, ni destruirse (por si propio), porque dejaria huerfano a su link actual con Agenda.*/

@Stateless
@LocalBean
public class ControladorReserva implements IReservaDAORemote, IReservaDAOLocal {
	
	@PersistenceContext(name = "test")
	private EntityManager em;
    /**
     * Default constructor. 
     */
	
    public ControladorReserva() {
        // TODO Auto-generated constructor stub
    }
    

    public void agregarReserva(int usuario, int etapa, String puesto, LocalDateTime fecha, EstadoReserva estado) throws ReservaRepetida, PuestoNoCargadoException, ReservaInexistente, UsuarioExistente {
    	Usuario u = em.find(Usuario.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("El usuario seleccionado no existe.");
    	}else{
    		Etapa e = em.find(Etapa.class, etapa);
        	if (e==null) {
        		throw new ReservaInexistente("La etapa seleccionada no existe.");
        	}else {
        		Puesto p = em.find(Puesto.class, puesto);
	    		if (p!=null) {
	    			Reserva test = getReservaEtapa(u.getReservas(), e);
	        		if (test==null) {	//si no existe una reserva con el objeto Etapa e (de id etapa)
			    		
			    			Reserva r = new Reserva(fecha, estado, e, u, p);
			    			u.getReservas().add(r);
			    			//e.setReservas(e.getReservas().add(r));
			    			//p.getReservas().add(r);;	//puesto setea reserva
			        		em.persist(u);
			        		//em.persist(e);
			        		em.persist(r);
			        		//em.merge(p);
	        		}else {
			    		throw new ReservaRepetida("Ya existe una reserva para esa etapa.");
			    	}
	    		}else
	    			throw new PuestoNoCargadoException("El puesto seleccionado no existe.");	
        	}
	    	
    	}
    }
    
    public void modificarReserva(int usuario, int etapa, String puesto, LocalDateTime fecha, EstadoReserva estado) throws ReservaInexistente, PuestoNoCargadoException, UsuarioExistente {
    	Usuario u = em.find(Usuario.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("El usuario seleccionado no existe.");
    	}else{
    		Etapa e = em.find(Etapa.class, etapa);
        	if (e==null) {
        		throw new ReservaInexistente("La etapa seleccionada no existe.");
        	}else {
        		Puesto p = em.find(Puesto.class, puesto);
	    		if (p!=null) {
	    			Reserva r = getReservaEtapa(u.getReservas(), e);
			    	if (r!=null) {
			    		
			    			r.setFechaRegistro(fecha);
			    			r.setEstado(estado);
			    			//compruebo si el puesto es distinto al anterior
			    			Puesto oldPuesto = r.getPuesto();
			    			if (!p.equals(oldPuesto)) {
			    				//oldPuesto.getReservas().remove(r);	//borro reserva de puesto anterior
			    				r.setPuesto(p);
				    			//p.getReservas().add(r);	//puesto setea reserva
				    			//em.merge(p);
			    			}
			    			em.merge(r);
			    	}else {
			    		throw new ReservaInexistente("No hay una reserva para esa etapa.");
			    	}
	    		}else {
	    			throw new PuestoNoCargadoException("No existe un puesto con ese ID.");
	    		}	
        	}
    	}
    }
    
    public void eliminarReserva(int usuario, int etapa) throws ReservaInexistente, UsuarioExistente {
    	Usuario u = em.find(Usuario.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("El usuario seleccionado no existe.");
    	}else{
    		Etapa e = em.find(Etapa.class, etapa);
        	if (e==null) {
        		throw new ReservaInexistente("La etapa seleccionada no existe.");
        	}else {
    			Reserva r = getReservaEtapa(u.getReservas(), e);
		    	if (r!=null) {
		    			Puesto p = r.getPuesto();
		    			u.getReservas().remove(r);
		    			//e.getReservas().remove(r);
		    			//p.getReservas().remove(r);
		    			em.merge(u);
		    			//em.merge(e);
		    			//em.merge(p);
		    			em.remove(r);
		    	}else {
		    		throw new ReservaInexistente("No hay una reserva para esa etapa.");
		    	}
        	}
    	}
    }
	
    /* TODO: DtPuesto tiene Vacunatorio, y no DtVacunatorio.
     * 
     */
	public DtReserva obtenerReserva(int usuario, int etapa) throws ReservaInexistente, UsuarioExistente {
		Usuario u = em.find(Usuario.class, usuario);
    	if (u==null) {
    		throw new UsuarioExistente("El usuario seleccionado no existe.");
    	}else{
    		Etapa e = em.find(Etapa.class, etapa);
        	if (e==null) {
        		throw new ReservaInexistente("La etapa seleccionada no existe.");
        	}else {
        		Reserva r = getReservaEtapa(u.getReservas(), e);
        		if (r!=null) {
        			DtReserva retorno = new DtReserva(r.getEstado(), getDtUsuario(r.getUsuario()), r.getFechaRegistro(), r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
        					r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(), r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId());

        			return retorno;
        		}else {
        			throw new ReservaInexistente("No hay una reserva para esa etapa.");
        		}
        	}
    	}
	}
	
	public ArrayList<DtReserva> listarReservasGenerales()  throws ReservaInexistente{
		Query query = em.createQuery("SELECT r FROM Reserva r");
		@SuppressWarnings("unchecked")
		ArrayList<Reserva> result = (ArrayList<Reserva>) query.getResultList();
		ArrayList<DtReserva> retorno = new ArrayList<>();
		if (result!=null) {
			for (Reserva r: result) {
				retorno.add(new DtReserva(r.getEstado(), getDtUsuario(r.getUsuario()), r.getFechaRegistro(), r.getPuesto().getId(), r.getPuesto().getVacunatorio().getNombre(),
    					r.getEtapa().toDtEtapa().getFechaInicio(), r.getEtapa().toDtEtapa().getFechaFin(), r.getEtapa().toDtEtapa().getDtPvac().getNombre(), r.getEtapa().getId()));
			}
			return retorno;
		}else {
			throw new ReservaInexistente("No hay ninguna reserva.");
		}
	}
	
	private Reserva getReserva(int id) {
		Reserva r = em.find(Reserva.class, id);
		return r;
	}
	
	private boolean existeReservaEtapa(ArrayList<Reserva> lista, Etapa e) {
		for (Reserva r: lista) {
			if (r.getEtapa().equals(e))
				return true;
		}
		
		return false;
	}
	
	private Reserva getReservaEtapa(List<Reserva> lista, Etapa e) {
		for (Reserva r: lista) {
			if (r.getEtapa().equals(e))
				return r;
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
