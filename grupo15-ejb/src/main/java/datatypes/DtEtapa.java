package datatypes;

import java.io.Serializable;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class DtEtapa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String fechaInicio;
	private String fechaFin;
	private String condicion;
	private int planVac;
	private String vacuna;
	
	
	public DtEtapa() {
	}

	public DtEtapa(int id, String fechaInicio, String fechaFin, String condicion, int planVac, String vacuna) {
		super();
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.condicion = condicion;
		this.planVac = planVac;
		this.vacuna = vacuna;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getPlanVac() {
		return planVac;
	}

	public void setDtPvac(int planVac) {
		this.planVac = planVac;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getVacuna() {
		return vacuna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((condicion == null) ? 0 : condicion.hashCode());
		result = prime * result + ((fechaFin == null) ? 0 : fechaFin.hashCode());
		result = prime * result + ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
		result = prime * result + id;
		result = prime * result + planVac;
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
		DtEtapa other = (DtEtapa) obj;
		if (condicion == null) {
			if (other.condicion != null)
				return false;
		} else if (!condicion.equals(other.condicion))
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
		if (planVac != other.planVac)
			return false;
		if (vacuna == null) {
			if (other.vacuna != null)
				return false;
		} else if (!vacuna.equals(other.vacuna))
			return false;
		return true;
	}
	
	
}
