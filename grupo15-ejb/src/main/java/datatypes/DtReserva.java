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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((etapa == null) ? 0 : etapa.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((puesto == null) ? 0 : puesto.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((vacuna == null) ? 0 : vacuna.hashCode());
		result = prime * result + ((vacunatorio == null) ? 0 : vacunatorio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DtReserva other = (DtReserva) obj;
		if (estado != other.estado)
			return false;
		if (etapa == null) {
			if (other.etapa != null)
				return false;
		} else if (!etapa.equals(other.etapa))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (puesto == null) {
			if (other.puesto != null)
				return false;
		} else if (!puesto.equals(other.puesto))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (vacuna == null) {
			if (other.vacuna != null)
				return false;
		} else if (!vacuna.equals(other.vacuna))
			return false;
		if (vacunatorio == null) {
			if (other.vacunatorio != null)
				return false;
		} else if (!vacunatorio.equals(other.vacunatorio))
			return false;
		return true;
	}
	

}
