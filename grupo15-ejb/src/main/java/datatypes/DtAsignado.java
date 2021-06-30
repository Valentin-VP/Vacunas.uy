package datatypes;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class DtAsignado implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fecha;
	private String idPuesto;
	public DtAsignado() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtAsignado(String fecha, String idPuesto) {
		super();
		this.fecha = fecha;
		this.idPuesto = idPuesto;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getIdPuesto() {
		return idPuesto;
	}
	public void setIdPuesto(String idPuesto) {
		this.idPuesto = idPuesto;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((idPuesto == null) ? 0 : idPuesto.hashCode());
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
		DtAsignado other = (DtAsignado) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (idPuesto == null) {
			if (other.idPuesto != null)
				return false;
		} else if (!idPuesto.equals(other.idPuesto))
			return false;
		return true;
	}
	
	
}
