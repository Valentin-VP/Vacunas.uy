package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;



@Entity
public class Vacuna{

	@Id
	private String nombre;
	private int cantDosis; //cuantas veces se da la vacuna
	private Date expira;  //fecha de expiracion
	@OneToOne
	private Laboratorio laboratorio;
	@OneToOne
	private Enfermedad enfermedad;
	
	public Vacuna() {
		super();
	}

	public Vacuna(String nombre, int cantDosis, Date expira, Laboratorio laboratorio, Enfermedad enfermedad) {
		super();
		this.nombre = nombre;
		this.cantDosis = cantDosis;
		this.expira = expira;
		this.laboratorio = laboratorio;
		this.enfermedad = enfermedad;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setCantDosis(int cant) {
		this.cantDosis = cant;
	}
	
	public Integer getCantDosis() {
		return this.cantDosis;
	}
	
	public void setExpira(Date fecha) {
		this.expira = fecha;
	}
	
	public Date getExpira() {
		return this.expira;
	}

	public Laboratorio getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(Laboratorio laboratorio) {
		this.laboratorio = laboratorio;
	}
	
}
