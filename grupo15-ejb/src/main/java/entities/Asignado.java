package entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import persistence.AsignadoID;

@Entity
@IdClass(AsignadoID.class)
public class Asignado {
	
	@Id
	private LocalDate fecha;
	
	@Id
	@ManyToOne//(cascade = CascadeType.ALL)
	@JoinColumn(insertable=false, updatable=false, referencedColumnName="idusuario")
	private Vacunador vacunador;
	
	@Id
	@ManyToOne//(cascade = CascadeType.ALL)
	@JoinColumn(insertable=false, updatable=false, referencedColumnName="id")
	@JoinColumn(insertable=false, updatable=false, referencedColumnName="vacunatorio_id")
	private Puesto puesto;
	
	public Asignado() {
		// TODO Auto-generated constructor stub
	}

	public Asignado(LocalDate fecha, Vacunador vacunador, Puesto puesto) {
		super();
		this.fecha = fecha;
		this.vacunador = vacunador;
		this.puesto = puesto;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Vacunador getVacunador() {
		return vacunador;
	}

	public void setVacunador(Vacunador vacunador) {
		this.vacunador = vacunador;
	}

	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}

	
}
