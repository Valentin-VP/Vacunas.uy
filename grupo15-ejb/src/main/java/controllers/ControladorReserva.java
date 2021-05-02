package controllers;

import interfaces.IReservaDAOLocal;
import interfaces.IReservaDAORemote;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtReserva;
import datatypes.EstadoReserva;
import entities.Reserva;
import exceptions.ReservaInexistente;
import exceptions.ReservaRepetida;

/**
 * Session Bean implementation class ControladorReserva
 */

//TODO:No estoy considerando Etapa y Usuario, ni Puesto (y soy un Tipo Asociativo) => TODO: cambiar nombreUser por la entidad
/*TODO:Constancia puede modificarse a si mismo, debe agregarse 1 reserva, puede agregar (o modificar) * lotedosis.
No puede crearse, ni destruirse (por si propio), porque dejaria huerfano a su link actual con Certificado.

Agenda puede modificarse a si mismo, puede agregar (o modificar/eliminar) * reservas, puede agregar (o modificar/eliminar) * cupos.
No puede crearse, ni destruirse (por si propio), porque dejaria huerfano a su link actual con Vacunatorio.
[Si un Vacunatorio lo destruye, debera destruir los cupos asociados a la Agenda]

Certificado no puede modificarse a si mismo, puede agregar (o modificar/eliminar) * Constancias.
No puede crearse, ni destruirse (por si propio), porque dejaria huerfano a su link actual con Usuario.
[Si un Usuario lo destruye, debera destruir las Constancias asociados al Certificado]

Reserva no puede modificarse a si mismo, puede agregar (o modificar/eliminar) 1 puesto.
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
    

    public void agregarReserva(int id, String user, LocalDateTime fecha, EstadoReserva estado) throws ReservaRepetida {
    	if (getReserva(id) == null) {
    		//DateFormat df = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss Z", new Locale("us"));
    		Reserva r = new Reserva(id, user, fecha, estado);
    		em.persist(r);
    	}else {
    		throw new ReservaRepetida("Ya existe una reserva con ese ID.");
    	}
    }
	
	public DtReserva obtenerReserva(int id) throws ReservaInexistente {
		Reserva temp = getReserva(id);
		
		if (temp!=null) {
			DtReserva retorno = new DtReserva(temp.getIdReserva(), temp.getEstado(), temp.getNombreUser(), temp.getFechaRegistro());

			return retorno;
		}else
			throw new ReservaInexistente("No hay una reserva con ese ID.");
	}
	
	public ArrayList<DtReserva> listarReservas()  throws ReservaInexistente{
		Query query = em.createQuery("SELECT r FROM Reserva r");
		@SuppressWarnings("unchecked")
		ArrayList<Reserva> result = (ArrayList<Reserva>) query.getResultList();
		ArrayList<DtReserva> retorno = new ArrayList<>();
		if (result!=null) {
			for (Reserva r: result) {
				retorno.add(new DtReserva(r.getIdReserva(), r.getEstado(), r.getNombreUser(), r.getFechaRegistro()));
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
}
