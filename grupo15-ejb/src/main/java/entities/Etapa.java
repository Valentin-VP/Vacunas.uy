package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import datatypes.DtEtapa;
import persistence.EtapaID;

@Entity
@IdClass(EtapaID.class)
public class Etapa {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private String condicion;
	@Id
	@ManyToOne//(cascade = CascadeType.ALL)
	@JoinColumn
	private PlanVacunacion planVacunacion;
	
	@ManyToOne
	@JoinColumn
	private Vacuna vacuna;
	
	public Etapa() {
		super();
	}

	public Etapa(LocalDate fechaInicio, LocalDate fechaFin, String condicion, PlanVacunacion planVacunacion) {
		super();
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

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
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
		String fi,ff;
		fi = this.fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		ff = this.fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		DtEtapa dtEtapa = new DtEtapa(this.id, fi, ff, this.condicion, this.planVacunacion.getId(), this.vacuna.getNombre());
		return dtEtapa;
	}

	public Vacuna getVacuna() {
		return vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}
	
	
	
}
