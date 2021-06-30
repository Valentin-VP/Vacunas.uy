package controllers;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtMensaje;
import entities.Mensaje;
import exceptions.MensajeExistente;
import interfaces.IMensajeLocal;
import interfaces.IMensajeRemote;

@Stateless
public class ControladorMensaje implements IMensajeLocal, IMensajeRemote {

	@PersistenceContext(name = "test")
	private EntityManager em;

	public ControladorMensaje() {
		// TODO Auto-generated constructor stub
	}

	public void agregarMensaje(String contenido) {

		// if (em.find(Mensaje.class,IdMensaje) != null) {
		// throw new MensajeExistente("Ya existe el mensaje ingresado");
		// }

		Mensaje cha = new Mensaje(contenido);
		em.persist(cha);

	}

	public DtMensaje BuscarMensaje(int IdMensaje) throws MensajeExistente {
		/*
		 * if (em.find(Mensaje.class,IdMensaje) == null) { throw new
		 * MensajeExistente("No existe el mensaje ingresado"); }
		 * 
		 * Mensaje c = em.find(Mensaje.class,IdMensaje); DtMensaje dt = new
		 * DtMensaje(c.getIdMensaje(), c.getContenido()); return dt;
		 */
		return null;
	}

	public void EliminarMensaje(int IdMensaje) throws MensajeExistente {
		/*
		 * if (em.find(Mensaje.class,IdMensaje) == null) { throw new
		 * MensajeExistente("No existe el mensaje ingresado"); }
		 * 
		 * Mensaje c = em.find(Mensaje.class,IdMensaje); em.remove(c);
		 */
	}

	public void ModificarMensaje(DtMensaje mensaje) throws MensajeExistente {

		/*
		 * if (em.find(Mensaje.class,mensaje) == null) { throw new
		 * MensajeExistente("No existe el mensaje ingresado"); }
		 * 
		 * Mensaje c = em.find(Mensaje.class, mensaje.getIdMensaje());
		 * 
		 * 
		 * c.setIdMensaje(mensaje.getIdMensaje());
		 * c.setContenido(mensaje.getContenido()); em.persist(c);
		 * 
		 */
	}

	public ArrayList<DtMensaje> listarMensajes() {
		
		  ArrayList<DtMensaje> chat = new ArrayList<>();
		  
		  
		  Query q = em.createQuery("select u from Mensaje u"); ArrayList<Mensaje>
		  mensajes = (ArrayList<Mensaje>) q.getResultList();
		  
		  for(Mensaje u: mensajes) { chat.add(new DtMensaje(u.getIdMensaje(),
		  u.getContenido())); }
		  
		  return chat;
	}

}
