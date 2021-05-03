package datatypes;

import java.util.ArrayList;

public class DtPlanVacunacion {

	private int id;
	private String nombre;
	private String descripcion;
	private ArrayList<DtEtapa> etapa = new ArrayList<>();
	
	public DtPlanVacunacion() {
	}

	public DtPlanVacunacion(int id, String nombre, String descripcion, ArrayList<DtEtapa> etapa) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.etapa = etapa;
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

	public ArrayList<DtEtapa> getEtapa() {
		return etapa;
	}

	public void setEtapa(ArrayList<DtEtapa> etapa) {
		this.etapa = etapa;
	}
	
	
}
