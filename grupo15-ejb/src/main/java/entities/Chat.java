package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.OneToMany;

@Entity
public class Chat {
 
	@Id
	private String IdChat;
	
	@OneToMany()
	private List<Mensaje> mensajes = new ArrayList<>();
	
	public Chat() {
		super();
	}
	
	public Chat(String IdChat) {
		super();
		this.IdChat = IdChat;
	}

	public String getIdChat() {
		return IdChat;
	}

	public void setIdChat(String idChat) {
		IdChat = idChat;
	}
	
	
}
