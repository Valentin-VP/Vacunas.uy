package entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Enfermedad {
	
	@Id
	private String nombre;

	public Enfermedad() {
		// TODO Auto-generated constructor stub
	}

	public Enfermedad(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
