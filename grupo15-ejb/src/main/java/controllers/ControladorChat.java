package controllers;

import java.util.ArrayList;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import datatypes.DtChat;

import entities.Chat;

import exceptions.ChatExistente;

import interfaces.IChatLocal;
import interfaces.IChatRemote;


@Stateless 
public class ControladorChat implements IChatRemote, IChatLocal{

	@PersistenceContext(name = "test")
	private EntityManager em;
	
	
	public ControladorChat() {
        // TODO Auto-generated constructor stub
    }
	
	
	public void agregarChat(String chat ) throws ChatExistente {
		
		if (em.find(Chat.class,chat) != null) {
			throw new ChatExistente("Ya existe el chat ingresado");
		}
		
		Chat cha = new Chat(chat);
		em.persist(cha);
		
		
	}
	
	
	public DtChat BuscarChat(String IdChat) throws ChatExistente {
		
		if (em.find(Chat.class,IdChat) == null) {
			throw new ChatExistente("No existe el chat ingresado");
		}
		
		Chat c = em.find(Chat.class,IdChat);
		DtChat dt = new DtChat(c.getIdChat());
		return dt;
	}
	
	
	
	public void EliminarChat (String IdChat) throws ChatExistente {	

		if (em.find(Chat.class,IdChat) == null) {
			throw new ChatExistente("No existe el chat ingresado");
		}
		
		Chat c = em.find(Chat.class,IdChat);
		em.remove(c);
	}
	
	
	public void ModificarVacunador(DtChat chat) throws ChatExistente {
		
		
		if (em.find(Chat.class,chat) == null) {
			throw new ChatExistente("No existe el chat ingresado");
		}
		
		Chat c = em.find(Chat.class, chat.getIdChat());

	
		c.setIdChat(chat.getIdChat());
		em.persist(c);
		
		
		}
	
	public ArrayList<DtChat> listarChats() {
	
		ArrayList<DtChat> chat = new ArrayList<>();
		
		
		Query q = em.createQuery("select u from Chat u");
		ArrayList<Chat> chats = (ArrayList<Chat>) q.getResultList();
		
		for(Chat u: chats) {
			chat.add(new DtChat(u.getIdChat()));
		}
			
		return chat;
	}
	
	
	
	
}
