package entities;

import java.util.ArrayList;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
	@OneToMany(mappedBy = "vacunatorio",cascade = CascadeType.ALL)
	private ArrayList<ReglasCupos> reglasCupos = new ArrayList<>();
	@OneToMany(mappedBy = "vacunatorio",cascade = CascadeType.ALL)
	private ArrayList<Puesto> puesto = new ArrayList<>();
	@ManyToMany(cascade = CascadeType.ALL)
	private ArrayList<Vacunador> vacunadores = new ArrayList<>();
	@OneToMany(mappedBy = "vacunatorio",cascade = CascadeType.ALL)
	private ArrayList<Stock> stock = new ArrayList<Stock>();
	@OneToMany(mappedBy = "vacunatorio",cascade = CascadeType.ALL)
	private ArrayList<Agenda> agenda = new ArrayList<>();



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

public ArrayList<Vacunador> getVacunadores() {
	return vacunadores;
}

public void setVacunadores(ArrayList<Vacunador> vacunadores) {
	this.vacunadores = vacunadores;
}

public ArrayList<ReglasCupos> getReglasCupos() {
	return reglasCupos;
}

public void setReglasCupos(ArrayList<ReglasCupos> reglasCupos) {
	this.reglasCupos = reglasCupos;
}

public ArrayList<Puesto> getPuesto() {
	return puesto;
}

public void setPuesto(ArrayList<Puesto> puesto) {
	this.puesto = puesto;
}

public ArrayList<Stock> getStock() {
	return stock;
}

public void setStock(ArrayList<Stock> stock) {
	this.stock = stock;
}



}