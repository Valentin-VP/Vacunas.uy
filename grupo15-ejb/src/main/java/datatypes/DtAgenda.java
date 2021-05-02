package datatypes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import entities.Cupo;

@XmlRootElement
public class DtAgenda implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7487696106296726826L;
	private int id;
	private LocalDate fecha;
	private List<DtCupo> cupos;
	private List<DtReserva> reservas;
	public DtAgenda() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DtAgenda(int id, LocalDate fecha, List<DtCupo> cupos, List<DtReserva> reservas) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cupos = cupos;
		this.reservas = reservas;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public List<DtCupo> getCupos() {
		return cupos;
	}
	public void setCupos(List<DtCupo> cupos) {
		this.cupos = cupos;
	}
	public List<DtReserva> getReservas() {
		return reservas;
	}
	public void setReservas(List<DtReserva> reservas) {
		this.reservas = reservas;
	}
	
	
}
