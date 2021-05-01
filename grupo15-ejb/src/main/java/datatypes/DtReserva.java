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
	private LocalDateTime fecha;

	
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
	
}
