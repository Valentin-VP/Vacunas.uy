package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Agenda {
	
	@Id
	private LocalDate fecha;
	@OneToMany
	private List<Cupo> cupos = new ArrayList<Cupo>();
	
	public Agenda() {
		super();
	}
	
	public Agenda(LocalDate fAgenda) {
		this.fecha = fAgenda;
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
	
	
}
