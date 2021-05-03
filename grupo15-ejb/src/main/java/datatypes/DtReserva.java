package datatypes;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtReserva implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4541370995128297208L;
	private Integer id;
	private EstadoReserva estado;
	private String usuario;
	////TODO:private DtEtapa etapa;
	//private DtUsuario usuario;
	//private DtPuesto puesto;
	private LocalDateTime fecha;
	private String puesto;
	private String vacunatorio;

	
	

	public DtReserva(Integer id, EstadoReserva estado, String usuario, LocalDateTime fecha, String puesto,
			String vacunatorio) {
		super();
		this.id = id;
		this.estado = estado;
		this.usuario = usuario;
		this.fecha = fecha;
		this.puesto = puesto;
		this.vacunatorio = vacunatorio;
	}

	public DtReserva(Integer id, EstadoReserva estado, String usuario, LocalDateTime fecha) {
		super();
		this.id = id;
		this.estado = estado;
		this.usuario = usuario;
		this.fecha = fecha;
	}
	
	public DtReserva() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	
	
	
}
