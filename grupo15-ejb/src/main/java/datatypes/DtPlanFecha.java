package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class DtPlanFecha implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String descripcion;
	private String fechas;
	private String enfermedad;
	public DtPlanFecha() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtPlanFecha(String nombre, String descripcion, String fechas, String enfermedad) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechas = fechas;
		this.enfermedad = enfermedad;
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
	public String getFechas() {
		return fechas;
	}
	public void setFechas(String fechas) {
		this.fechas = fechas;
	}
	public String getEnfermedad() {
		return enfermedad;
	}
	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}
	
	
}
