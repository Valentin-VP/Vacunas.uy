package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import datatypes.DtEtapa;
import persistence.EtapaID;

@Entity
@IdClass(EtapaID.class)
public class Etapa {
	
	@Id
	private int id;
	private Date fechaInicio;
	private Date fechaFin;
	private String condicion;
	@Id
	@ManyToOne
	@JoinColumn
	private PlanVacunacion planVacunacion;
	
	public Etapa() {
		super();
	}

	public Etapa(int id, Date fechaInicio, Date fechaFin, String condicion, PlanVacunacion planVacunacion) {
		super();
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.condicion = condicion;
		this.planVacunacion = planVacunacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public PlanVacunacion getPlanVacunacion() {
		return planVacunacion;
	}

	public void setPlanVacunacion(PlanVacunacion planVacunacion) {
		this.planVacunacion = planVacunacion;
	}
	
	public DtEtapa toDtEtapa() {
		DtEtapa dtEtapa = new DtEtapa(this.id, this.fechaInicio, this.fechaFin,this.planVacunacion.toDtPlanVacunacion());
		return dtEtapa;
	}
	
}
