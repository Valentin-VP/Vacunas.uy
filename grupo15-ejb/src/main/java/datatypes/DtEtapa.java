package datatypes;

import java.util.Date;

public class DtEtapa {

	private int id;
	private Date fechaInicio;
	private Date fechaFin;
	private DtPlanVacunacion DtPvac;
	
	public DtEtapa() {
	}

	public DtEtapa(int id, Date fechaInicio, Date fechaFin, DtPlanVacunacion dtPvac) {
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

	public DtPlanVacunacion getDtPvac() {
		return DtPvac;
	}

	public void setDtPvac(DtPlanVacunacion dtPvac) {
		DtPvac = dtPvac;
	}
	
	
}
