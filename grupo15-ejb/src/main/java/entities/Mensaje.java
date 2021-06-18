package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Mensaje {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer IdMensaje;
	private String contenido;
	
	
//	@ManyToOne
//	private Vacunador vacunador;
	
	
	public Mensaje() {
		super();
	}
	
	public Mensaje(String contenido) {
		super();
		this.contenido = contenido;
	}
	
	public Mensaje(Integer IdMensaje, String contenido) {
		super();
		this.IdMensaje = IdMensaje;
		this.contenido = contenido;
	}
	
//	public Mensaje(Integer IdMensaje, String contenido, Chat chat) {
//		super();
//		this.IdMensaje = IdMensaje;
//		this.contenido = contenido;
//	
//	}
	
//	public Mensaje(Integer IdMensaje, String contenido, Chat chat, Vacunador vacunador) {
//		super();
//		this.IdMensaje = IdMensaje;
//		this.contenido = contenido;
//		this.vacunador = vacunador;
//	}
	
	
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

