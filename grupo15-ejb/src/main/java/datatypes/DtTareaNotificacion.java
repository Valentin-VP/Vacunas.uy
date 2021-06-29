package datatypes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtTareaNotificacion implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String puesto;
	private String mobileToken;
	private String vacunatorio;
	private String fecha;
	private String hora;

	public DtTareaNotificacion() {}

	public DtTareaNotificacion(String puesto, String mobileToken, String vacunatorio, String fecha, String hora) {
		super();
		this.puesto = puesto;
		this.mobileToken = mobileToken;
		this.vacunatorio = vacunatorio;
		this.fecha = fecha;
		this.hora = hora;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getMobileToken() {
		return mobileToken;
	}

	public void setMobileToken(String mobileToken) {
		this.mobileToken = mobileToken;
	}

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
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
