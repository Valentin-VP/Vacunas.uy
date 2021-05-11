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
	
	
}
