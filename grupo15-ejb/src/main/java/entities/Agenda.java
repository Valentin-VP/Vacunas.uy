package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import persistence.AgendaID;

@Entity
@IdClass(AgendaID.class)
public class Agenda {
	
	
	//private int idAgenda;
	@Id
	private LocalDate fecha;
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private Vacunatorio vacunatorio;
	
	
	//@OneToMany(mappedBy="agenda")
	//private List<Cupo> cupos = new ArrayList<Cupo>();
	@OneToMany
	private List<Reserva> reservas = new ArrayList<Reserva>();

	
	public Agenda() {
		super();
	}

	public Agenda(LocalDate fecha) {
		super();
		this.fecha = fecha;
	}
		
//	public int getIdAgenda() {
//		return idAgenda;
//	}
//
//	public void setIdAgenda(int idAgenda) {
//		this.idAgenda = idAgenda;
//	}

	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
//	public List<Cupo> getCupos() {
//		return cupos;
//	}
//	public void setCupos(List<Cupo> cupos) {
//		this.cupos = cupos;
//	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public Vacunatorio getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(Vacunatorio vacunatorio) {
		this.vacunatorio = vacunatorio;
	}
	
	
	
	
}
