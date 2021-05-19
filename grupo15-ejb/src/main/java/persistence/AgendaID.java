package persistence;

import java.io.Serializable;
import java.time.LocalDate;

public class AgendaID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LocalDate fecha;
	private String vacunatorio;
	public AgendaID() {
		
		// TODO Auto-generated constructor stub
	}
	public AgendaID(LocalDate fecha, String vacunatorio) {
		super();
		this.fecha = fecha;
		this.vacunatorio = vacunatorio;
	}
	
	
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
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
		AgendaID other = (AgendaID) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (vacunatorio == null) {
			if (other.vacunatorio != null)
				return false;
		} else if (!vacunatorio.equals(other.vacunatorio))
			return false;
		return true;
	}
	
	

}
