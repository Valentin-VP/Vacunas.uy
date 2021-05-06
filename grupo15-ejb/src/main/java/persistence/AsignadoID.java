package persistence;

import java.io.Serializable;
import java.util.Date;

public class AsignadoID implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date fecha; // PK de asignado
	private int vacunador; //PK de vacunador
	private PuestoID puesto; // PK compuesta de puesto
	
	public AsignadoID() {
		// TODO Auto-generated constructor stub
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getVacunador() {
		return vacunador;
	}

	public void setVacunador(int vacunador) {
		this.vacunador = vacunador;
	}

	public PuestoID getPuesto() {
		return puesto;
	}

	public void setPuesto(PuestoID puesto) {
		this.puesto = puesto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((puesto == null) ? 0 : puesto.hashCode());
		result = prime * result + vacunador;
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
		AsignadoID other = (AsignadoID) obj;
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
		if (vacunador != other.vacunador)
			return false;
		return true;
	}

	
}
