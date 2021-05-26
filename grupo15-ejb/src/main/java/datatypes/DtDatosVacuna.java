package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtDatosVacuna implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String cantDosis;
	private String expira; 
	private String tiempoEntreDosis;
	private String laboratorio;
	private String enfermedad;
	public DtDatosVacuna() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtDatosVacuna(String nombre, String cantDosis, String expira, String tiempoEntreDosis, String laboratorio,
			String enfermedad) {
		super();
		this.nombre = nombre;
		this.cantDosis = cantDosis;
		this.expira = expira;
		this.tiempoEntreDosis = tiempoEntreDosis;
		this.laboratorio = laboratorio;
		this.enfermedad = enfermedad;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCantDosis() {
		return cantDosis;
	}
	public void setCantDosis(String cantDosis) {
		this.cantDosis = cantDosis;
	}
	public String getExpira() {
		return expira;
	}
	public void setExpira(String expira) {
		this.expira = expira;
	}
	public String getTiempoEntreDosis() {
		return tiempoEntreDosis;
	}
	public void setTiempoEntreDosis(String tiempoEntreDosis) {
		this.tiempoEntreDosis = tiempoEntreDosis;
	}
	public String getLaboratorio() {
		return laboratorio;
	}
	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}
	public String getEnfermedad() {
		return enfermedad;
	}
	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}
	
	
}
