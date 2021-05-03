package entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;

@Entity
public class PlanVacunacion {
	
	@Id
	private int id;
	private String nombre;
	private String descripcion;
	@OneToMany(mappedBy = "planVacunacion", cascade = CascadeType.ALL)
	private ArrayList<Etapa> etapas = new ArrayList<>();
	
	public PlanVacunacion() {
		super();
	}
	public PlanVacunacion(int id, String nombre, String descripcion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public ArrayList<Etapa> getEtapas() {
		return etapas;
	}
	public void setEtapas(ArrayList<Etapa> etapa) {
		this.etapas = etapa;
	}
	
	public void addEtapa(Etapa e) {
		this.etapas.add(e);
	}
	
	public DtPlanVacunacion toDtPlanVacunacion() { //las etapas de los dtPLanVacunacion no tienen un a su PlanVacunacion
		ArrayList<DtEtapa> dtEtapas = new ArrayList<>();
		for(Etapa e: this.etapas) {
			dtEtapas.add(new DtEtapa(e.getId(), e.getFechaInicio(), e.getFechaFin(), new DtPlanVacunacion()));
		}
		DtPlanVacunacion dtPlanVacunacion = new DtPlanVacunacion(this.id, this.nombre, this.descripcion, dtEtapas);
		return dtPlanVacunacion;
	}

}
