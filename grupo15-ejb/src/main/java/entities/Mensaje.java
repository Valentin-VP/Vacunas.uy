package entities;

import javax.persistence.ManyToOne;

public class Mensaje {

	private Integer IdMensaje;
	private String contenido;
	
	@ManyToOne
	private Chat chat;
	
	@ManyToOne
	private Vacunador vacunador;
	

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

