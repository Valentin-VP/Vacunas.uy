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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cantDosis;
		result = prime * result + ((dtEnf == null) ? 0 : dtEnf.hashCode());
		result = prime * result + ((dtLab == null) ? 0 : dtLab.hashCode());
		result = prime * result + expira;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + tiempoEntreDosis;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DtVacuna other = (DtVacuna) obj;
		if (cantDosis != other.cantDosis)
			return false;
		if (dtEnf == null) {
			if (other.dtEnf != null)
				return false;
		} else if (!dtEnf.equals(other.dtEnf))
			return false;
		if (dtLab == null) {
			if (other.dtLab != null)
				return false;
		} else if (!dtLab.equals(other.dtLab))
			return false;
		if (expira != other.expira)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (tiempoEntreDosis != other.tiempoEntreDosis)
			return false;
		return true;
	}
	
	
	
}