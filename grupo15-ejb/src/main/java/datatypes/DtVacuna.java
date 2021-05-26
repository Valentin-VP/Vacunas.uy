package datatypes;

import java.io.Serializable;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtVacuna implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private int cantDosis; //cuantas veces se da la vacuna
	private int expira;  //fecha de expiracion
	private int tiempoEntreDosis;
	private DtLaboratorio dtLab;
	private DtEnfermedad dtEnf;
	
	public DtVacuna() {
		
	}
	
	public DtVacuna(String nombre, int cantDosis, int expira, int tiempoEntreDosis, DtLaboratorio dtLab,
			DtEnfermedad dtEnf) {
		super();
		this.nombre = nombre;
		this.cantDosis = cantDosis;
		this.expira = expira;
		this.tiempoEntreDosis = tiempoEntreDosis;
		this.dtLab = dtLab;
		this.dtEnf = dtEnf;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCantDosis() {
		return cantDosis;
	}

	public void setCantDosis(int cantDosis) {
		this.cantDosis = cantDosis;
	}

	public int getExpira() {
		return expira;
	}

	public void setExpira(int expira) {
		this.expira = expira;
	}

	public DtLaboratorio getDtLab() {
		return dtLab;
	}

	public void setDtLab(DtLaboratorio dtLab) {
		this.dtLab = dtLab;
	}

	public DtEnfermedad getDtEnf() {
		return dtEnf;
	}

	public void setDtEnf(DtEnfermedad dtEnf) {
		this.dtEnf = dtEnf;
	}

	public int getTiempoEntreDosis() {
		return tiempoEntreDosis;
	}

	public void setTiempoEntreDosis(int tiempoEntreDosis) {
		this.tiempoEntreDosis = tiempoEntreDosis;
	}
	
	
	
}