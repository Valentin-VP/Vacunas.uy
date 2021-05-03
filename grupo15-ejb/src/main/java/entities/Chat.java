package entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Chat {
 
	@Id
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
