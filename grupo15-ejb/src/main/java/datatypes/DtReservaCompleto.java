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
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
	
	
}
