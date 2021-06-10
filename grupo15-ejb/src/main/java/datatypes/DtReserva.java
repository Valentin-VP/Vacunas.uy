package datatypes;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtReserva implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4541370995128297208L;
	private String fecha;
	private EstadoReserva estado;
	private String etapa;
	private String vacuna;
	private String usuario;
	private String puesto;
	private String vacunatorio = null;
	public DtReserva() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DtReserva(String fecha, EstadoReserva estado, String etapa, String usuario, String puesto) {
		super();
		this.fecha = fecha;
		this.estado = estado;
		this.etapa = etapa;
		this.vacuna = null;
		this.usuario = usuario;
		this.puesto = puesto;
	}
	
	public DtReserva(String fecha, String vacunatorio, EstadoReserva estado, String etapa, String usuario, String puesto) {
		super();
		this.fecha = fecha;
		this.estado = estado;
		this.etapa = etapa;
		this.vacuna = null;
		this.usuario = usuario;
		this.puesto = puesto;
		this.setVacunatorio(vacunatorio);
	}
	
	public DtReserva(String fecha, EstadoReserva estado, String etapa, String vacuna, String usuario, String puesto) {
		super();
		this.fecha = fecha;
		this.estado = estado;
		this.etapa = etapa;
		this.vacuna = vacuna;
		this.usuario = usuario;
		this.puesto = puesto;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public EstadoReserva getEstado() {
		return estado;
	}
	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}
	public String getEtapa() {
		return etapa;
	}
	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}
	public String getVacuna() {
		return vacuna;
	}
	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
	}
	

}
