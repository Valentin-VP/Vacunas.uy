package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Mensaje {

	@Id
	private Integer IdMensaje;
	private String contenido;
	
	@ManyToOne
	private Chat chat;
	
	@ManyToOne
	private Vacunador vacunador;
	
	
	public Mensaje() {
		super();
	}
	
	public Mensaje(Integer IdMensaje, String contenido) {
		super();
		this.IdMensaje = IdMensaje;
		this.contenido = contenido;
	}
	
	public Mensaje(Integer IdMensaje, String contenido, Chat chat) {
		super();
		this.IdMensaje = IdMensaje;
		this.contenido = contenido;
		this.chat = chat;
	}
	
	public Mensaje(Integer IdMensaje, String contenido, Chat chat, Vacunador vacunador) {
		super();
		this.IdMensaje = IdMensaje;
		this.contenido = contenido;
		this.chat = chat;
		this.vacunador = vacunador;
	}
	
	
	public Integer getIdMensaje() {
		return IdMensaje;
	}

	public void setIdMensaje(Integer idMensaje) {
		IdMensaje = idMensaje;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
}

