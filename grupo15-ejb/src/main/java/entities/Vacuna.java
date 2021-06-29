package entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import datatypes.DtEnfermedad;
import datatypes.DtLaboratorio;
import datatypes.DtVacuna;



@Entity
public class Vacuna{

	@Id
	private String nombre;
	private int cantDosis; //cuantas veces se da la vacuna
	private int expira;  //tiempo de inmunidad meses
	private int tiempoEntreDosis;
	@OneToOne//(cascade = CascadeType.ALL)
	private Laboratorio laboratorio;
	@OneToOne//(cascade = CascadeType.ALL)
	private Enfermedad enfermedad;
	
	public Vacuna() {
		super();
	}

	public Vacuna(String nombre, int cantDosis, int expira, int tiempoEntreDosis, Laboratorio laboratorio,
			Enfermedad enfermedad) {
		super();
		this.nombre = nombre;
		this.cantDosis = cantDosis;
		this.expira = expira;
		this.tiempoEntreDosis = tiempoEntreDosis;
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
	
	public void setExpira(int fecha) {
		this.expira = fecha;
	}
	
	public int getExpira() {
		return this.expira;
	}

	public Laboratorio getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(Laboratorio laboratorio) {
		this.laboratorio = laboratorio;
	}

	public Enfermedad getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(Enfermedad enfermedad) {
		this.enfermedad = enfermedad;
	}
	
	
	
	public int getTiempoEntreDosis() {
		return tiempoEntreDosis;
	}

	public void setTiempoEntreDosis(int tiempoEntreDosis) {
		this.tiempoEntreDosis = tiempoEntreDosis;
	}

	public DtVacuna toDtVacuna() {
		DtLaboratorio dtLab = new DtLaboratorio(this.getLaboratorio().getNombre());
		DtEnfermedad dtEnf = new DtEnfermedad(this.getEnfermedad().getNombre());
		DtVacuna dtVac = new DtVacuna(this.getNombre(), this.getCantDosis(), this.getExpira(), this.getTiempoEntreDosis(), dtLab, dtEnf);
		return dtVac;
	}
	
}
