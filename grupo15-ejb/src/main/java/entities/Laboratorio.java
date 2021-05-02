package entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Laboratorio {

	@Id
	private String nombre;

	public Laboratorio() {
		super();
	}
	
	public Laboratorio(String nombre) {
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
