package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

public class Chat {
 
	private String IdChat;
	
	@ManyToMany(mappedBy = "chats")
	List<Vacunador> vacunadores = new ArrayList<>();

	@OneToMany(mappedBy = "chat",cascade = CascadeType.ALL)
	private List<Mensaje> mensajes = new ArrayList<>();
	
	
	public String getIdChat() {
		return IdChat;
	}

	public void setIdChat(String idChat) {
		IdChat = idChat;
	}
	
	
}
