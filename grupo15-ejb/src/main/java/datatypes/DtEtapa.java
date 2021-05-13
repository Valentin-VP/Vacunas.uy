package datatypes;

import java.time.LocalDate;
import java.util.Date;

public class DtEtapa {

	private int id;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private DtPlanVacunacion DtPvac;
	
	public DtEtapa() {
	}

	public DtEtapa(int id, LocalDate fechaInicio, LocalDate fechaFin, DtPlanVacunacion dtPvac) {
		super();
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		DtPvac = dtPvac;
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

	public DtPlanVacunacion getDtPvac() {
		return DtPvac;
	}

	public void setDtPvac(DtPlanVacunacion dtPvac) {
		DtPvac = dtPvac;
	}
	
	
}
