package datatypes;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtDatosReserva implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5360946905967514828L;

	//private int idCiudadano;
	private String idEnfermedad;
	private int idPlan;
	private String idVacunatorio;
	private String fecha;
	private String hora;
	/*public int getIdCiudadano() {
		return idCiudadano;
	}
	public void setIdCiudadano(int idCiudadano) {
		this.idCiudadano = idCiudadano;
	}*/
	public String getIdEnfermedad() {
		return idEnfermedad;
	}
	public void setIdEnfermedad(String idEnfermedad) {
		this.idEnfermedad = idEnfermedad;
	}
	public int getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(int idPlan) {
		this.idPlan = idPlan;
	}
	public String getIdVacunatorio() {
		return idVacunatorio;
	}
	public void setIdVacunatorio(String idVacunatorio) {
		this.idVacunatorio = idVacunatorio;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}



}
