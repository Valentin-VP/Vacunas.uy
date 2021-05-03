package entities;




import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Puesto {
	@Id
	private String id;
	
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