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
	private Date fecha;
	private EstadoReserva estado;
	private String etapa;
	private String vacuna;
	private String usuario;
	private String puesto;
	public DtReserva() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtReserva(Date fecha, EstadoReserva estado, String etapa, String vacuna, String usuario, String puesto) {
		super();
		this.fecha = fecha;
		this.estado = estado;
		this.etapa = etapa;
		this.vacuna = vacuna;
		this.usuario = usuario;
		this.puesto = puesto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
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
	

}
