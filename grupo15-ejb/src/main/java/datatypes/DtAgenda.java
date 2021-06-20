package datatypes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtAgenda implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7487696106296726826L;
	//private int id;
	private LocalDate fecha;
	//private List<DtCupo> cupos;
	private List<DtReserva> reservas;
	public DtAgenda() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DtAgenda(LocalDate fecha, List<DtReserva> reservas) {
		super();
		this.fecha = fecha;
		this.reservas = reservas;
	}

	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public List<DtReserva> getReservas() {
		return reservas;
	}
	public void setReservas(List<DtReserva> reservas) {
		this.reservas = reservas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((reservas == null) ? 0 : reservas.hashCode());
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
		DtAgenda other = (DtAgenda) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (reservas == null) {
			if (other.reservas != null)
				return false;
		} else if (!reservas.equals(other.reservas))
			return false;
		return true;
	}
	
	
}
