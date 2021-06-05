package datatypes;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class DtEtapa implements Serializable {

	private static final long serialVersionUID = 1L;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DtPvac == null) ? 0 : DtPvac.hashCode());
		result = prime * result + ((fechaFin == null) ? 0 : fechaFin.hashCode());
		result = prime * result + ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
		result = prime * result + id;
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
		DtEtapa other = (DtEtapa) obj;
		if (DtPvac == null) {
			if (other.DtPvac != null)
				return false;
		} else if (!DtPvac.equals(other.DtPvac))
			return false;
		if (fechaFin == null) {
			if (other.fechaFin != null)
				return false;
		} else if (!fechaFin.equals(other.fechaFin))
			return false;
		if (fechaInicio == null) {
			if (other.fechaInicio != null)
				return false;
		} else if (!fechaInicio.equals(other.fechaInicio))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
