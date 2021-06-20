package datatypes;

import java.io.Serializable;

public class DtReservaCompleto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idCiudadano;
	private String idEtapa;
	private String idPlan;
	private String fecha;
	private EstadoReserva estado;
	private String descEtapa;
	private String descPlan;
	private String vacuna;
	private String enfermedad;
	private String usuario;
	private String puesto;
	public DtReservaCompleto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtReservaCompleto(String idCiudadano, String idEtapa, String idPlan, String fecha, EstadoReserva estado,
			String descEtapa, String descPlan, String vacuna, String enfermedad, String usuario, String puesto) {
		super();
		this.idCiudadano = idCiudadano;
		this.idEtapa = idEtapa;
		this.idPlan = idPlan;
		this.fecha = fecha;
		this.estado = estado;
		this.descEtapa = descEtapa;
		this.descPlan = descPlan;
		this.vacuna = vacuna;
		this.enfermedad = enfermedad;
		this.usuario = usuario;
		this.puesto = puesto;
	}
	public String getIdCiudadano() {
		return idCiudadano;
	}
	public void setIdCiudadano(String idCiudadano) {
		this.idCiudadano = idCiudadano;
	}
	public String getIdEtapa() {
		return idEtapa;
	}
	public void setIdEtapa(String idEtapa) {
		this.idEtapa = idEtapa;
	}
	public String getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(String idPlan) {
		this.idPlan = idPlan;
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
	public String getDescEtapa() {
		return descEtapa;
	}
	public void setDescEtapa(String descEtapa) {
		this.descEtapa = descEtapa;
	}
	public String getDescPlan() {
		return descPlan;
	}
	public void setDescPlan(String descPlan) {
		this.descPlan = descPlan;
	}
	public String getVacuna() {
		return vacuna;
	}
	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}
	public String getEnfermedad() {
		return enfermedad;
	}
	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descEtapa == null) ? 0 : descEtapa.hashCode());
		result = prime * result + ((descPlan == null) ? 0 : descPlan.hashCode());
		result = prime * result + ((enfermedad == null) ? 0 : enfermedad.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((idCiudadano == null) ? 0 : idCiudadano.hashCode());
		result = prime * result + ((idEtapa == null) ? 0 : idEtapa.hashCode());
		result = prime * result + ((idPlan == null) ? 0 : idPlan.hashCode());
		result = prime * result + ((puesto == null) ? 0 : puesto.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((vacuna == null) ? 0 : vacuna.hashCode());
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
		DtReservaCompleto other = (DtReservaCompleto) obj;
		if (descEtapa == null) {
			if (other.descEtapa != null)
				return false;
		} else if (!descEtapa.equals(other.descEtapa))
			return false;
		if (descPlan == null) {
			if (other.descPlan != null)
				return false;
		} else if (!descPlan.equals(other.descPlan))
			return false;
		if (enfermedad == null) {
			if (other.enfermedad != null)
				return false;
		} else if (!enfermedad.equals(other.enfermedad))
			return false;
		if (estado != other.estado)
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (idCiudadano == null) {
			if (other.idCiudadano != null)
				return false;
		} else if (!idCiudadano.equals(other.idCiudadano))
			return false;
		if (idEtapa == null) {
			if (other.idEtapa != null)
				return false;
		} else if (!idEtapa.equals(other.idEtapa))
			return false;
		if (idPlan == null) {
			if (other.idPlan != null)
				return false;
		} else if (!idPlan.equals(other.idPlan))
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
		return true;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
	
	
}
