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
	private Date fecha;
	private String idPuesto;
	public DtAsignado() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtAsignado(Date fecha, String idPuesto) {
		super();
		this.fecha = fecha;
		this.idPuesto = idPuesto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getIdPuesto() {
		return idPuesto;
	}
	public void setIdPuesto(String idPuesto) {
		this.idPuesto = idPuesto;
	}
	
	
}
