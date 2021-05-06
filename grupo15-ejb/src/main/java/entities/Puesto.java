package entities;




import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import persistence.PuestoID;


@Entity
@IdClass(PuestoID.class)
public class Puesto {
	@Id
	private String id;
	
	@Id
	@ManyToOne
	@JoinColumn
	private Vacunatorio vacunatorio;
	
	
	public Puesto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Puesto(String id, Vacunatorio vacunatorio) {
		super();
		this.id = id;
		this.vacunatorio = vacunatorio;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vacunatorio getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(Vacunatorio vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	



}