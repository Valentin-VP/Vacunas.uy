package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

public class Chat {
 
	private String IdChat;
	
	@ManyToMany(cascade = CascadeType.ALL)
	ArrayList<Vacunador> vacunadores = new ArrayList<>();

	@OneToMany(mappedBy = "chat",cascade = CascadeType.ALL)
	private ArrayList<Mensaje> mensajes = new ArrayList<>();
	
	public Chat() {
		super();
	}
	
	public Chat(String chat) {
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
