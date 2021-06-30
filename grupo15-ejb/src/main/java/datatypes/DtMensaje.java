package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtMensaje implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int IdMensaje;
	private String contenido;

	public DtMensaje() {
		super();
	}

	public DtMensaje(String contenido) {
		super();
		this.contenido = contenido;
	}

	public DtMensaje(int IdMensaje, String contenido) {
		super();
		this.IdMensaje = IdMensaje;
		this.contenido = contenido;

	}

	public int getIdMensaje() {
		return IdMensaje;
	}

	public void setIdMensaje(int idMensaje) {
		IdMensaje = idMensaje;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + IdMensaje;
		result = prime * result + ((contenido == null) ? 0 : contenido.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DtMensaje other = (DtMensaje) obj;
		if (IdMensaje != other.IdMensaje)
			return false;
		if (contenido == null) {
			if (other.contenido != null)
				return false;
		} else if (!contenido.equals(other.contenido))
			return false;
		return true;
	}

	
}
