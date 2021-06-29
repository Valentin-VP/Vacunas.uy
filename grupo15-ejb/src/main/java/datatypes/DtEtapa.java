package datatypes;

import java.io.Serializable;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class DtEtapa implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String fechaInicio;
	private String fechaFin;
	private String condicion;
	private int planVac;
	private String vacuna;
	
	
	public DtEtapa() {
	}

	public DtEtapa(int id, String fechaInicio, String fechaFin, String condicion, int planVac, String vacuna) {
		super();
		this.id = id;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.condicion = condicion;
		this.planVac = planVac;
		this.vacuna = vacuna;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getPlanVac() {
		return planVac;
	}

	public void setDtPvac(int planVac) {
		this.planVac = planVac;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getVacuna() {
		return vacuna;
	}
	
}
