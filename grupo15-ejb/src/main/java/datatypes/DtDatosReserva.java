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

	private int idCiudadano;
	private String idEnfermedad;
	private int idPlan;
	private String idVacunatorio;
	private Date fecha;
	private Date hora;
	public int getIdCiudadano() {
		return idCiudadano;
	}
	public void setIdCiudadano(int idCiudadano) {
		this.idCiudadano = idCiudadano;
	}
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Date getHora() {
		return hora;
	}
	public void setHora(Date hora) {
		this.hora = hora;
	}



}
