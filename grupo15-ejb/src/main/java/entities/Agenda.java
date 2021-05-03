package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Agenda {
	
	@Id
	private int idAgenda;

	private LocalDate fecha;
	@OneToMany
	private List<Cupo> cupos = new ArrayList<Cupo>();
	@OneToMany
	private List<Reserva> reservas = new ArrayList<Reserva>();
	@ManyToOne
	@JoinColumn(name="vacunatorio_id")
	private Vacunatorio vacunatorio;
	
	public Agenda() {
		super();
	}

	public Agenda(int idAgenda, LocalDate fecha) {
		super();
		this.idAgenda = idAgenda;
		this.fecha = fecha;
	}
		
	public int getIdAgenda() {
		return idAgenda;
	}

	public void setIdAgenda(int idAgenda) {
		this.idAgenda = idAgenda;
	}

	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public List<Cupo> getCupos() {
		return cupos;
	}
	public void setCupos(List<Cupo> cupos) {
		this.cupos = cupos;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	
}
