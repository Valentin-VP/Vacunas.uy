package datatypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtReserva implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4541370995128297208L;
	private EstadoReserva estado;
	
	private DtUsuario usuario;
	private LocalDateTime fecha;
	private String puesto;
	private String vacunatorio;
	private Date fechaInicioEtapa;
	private Date fechaFinEtapa;
	private String planVac;
	private int etapa;
	
	public DtReserva(EstadoReserva estado, DtUsuario usuario, LocalDateTime fecha, String puesto, String vacunatorio) {
		super();
		this.estado = estado;
		this.usuario = usuario;
		this.fecha = fecha;
		this.puesto = puesto;
		this.vacunatorio = vacunatorio;
	}
	
	

	public DtReserva(EstadoReserva estado, DtUsuario usuario, LocalDateTime fecha, String puesto, String vacunatorio,
			Date fechaInicioEtapa, Date fechaFinEtapa, String planVac, int etapa) {
		super();
		this.estado = estado;
		this.usuario = usuario;
		this.fecha = fecha;
		this.puesto = puesto;
		this.vacunatorio = vacunatorio;
		this.fechaInicioEtapa = fechaInicioEtapa;
		this.fechaFinEtapa = fechaFinEtapa;
		this.planVac = planVac;
		this.etapa = etapa;
	}



	public DtReserva() {
		super();
	}


	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
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

	public DtUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(DtUsuario usuario) {
		this.usuario = usuario;
	}



	public Date getFechaInicioEtapa() {
		return fechaInicioEtapa;
	}



	public void setFechaInicioEtapa(Date fechaInicioEtapa) {
		this.fechaInicioEtapa = fechaInicioEtapa;
	}



	public Date getFechaFinEtapa() {
		return fechaFinEtapa;
	}



	public void setFechaFinEtapa(Date fechaFinEtapa) {
		this.fechaFinEtapa = fechaFinEtapa;
	}



	public String getPlanVac() {
		return planVac;
	}



	public void setPlanVac(String planVac) {
		this.planVac = planVac;
	}



	public int getEtapa() {
		return etapa;
	}



	public void setEtapa(int etapa) {
		this.etapa = etapa;
	}
	
	
	
}
