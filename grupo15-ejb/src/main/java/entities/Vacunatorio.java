package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import datatypes.DtDireccion;

@Entity
public class Vacunatorio {
	@Id
	private String id;
	@Column(nullable=false)
	private String nombre;
	private DtDireccion dtDir;
	private Integer telefono;
	private Float latitud;
	private Float longitud;
	




public Vacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud)
{
		super();
		this.id = id;
		this.nombre = nombre;
		this.dtDir = dtDir;
		this.telefono = telefono;
		this.latitud = latitud;
		this.longitud = longitud;
		
	}

public Vacunatorio() {
		super();
		// TODO Auto-generated constructor stub
	}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public DtDireccion getDtDir() {
	return dtDir;
}

public void setDtDir(DtDireccion dtDir) {
	this.dtDir = dtDir;
}

public Integer getTelefono() {
	return telefono;
}

public void setTelefono(Integer telefono) {
	this.telefono = telefono;
}

public Float getLatitud() {
	return latitud;
}

public void setLatitud(Float latitud) {
	this.latitud = latitud;
}

public Float getLongitud() {
	return longitud;
}

public void setLongitud(Float longitud) {
	this.longitud = longitud;
}



}