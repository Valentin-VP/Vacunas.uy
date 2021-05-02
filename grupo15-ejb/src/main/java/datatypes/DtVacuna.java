package datatypes;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtVacuna implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private Integer cantDosis; //cuantas veces se da la vacuna
	private Date expira;  //fecha de expiracion
	
	public DtVacuna() {
		
	}
	
	public DtVacuna(String nombre, Integer cantDosis, Date expira) {
		super();
		this.nombre = nombre;
		this.cantDosis = cantDosis;
		this.expira = expira;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantDosis() {
		return cantDosis;
	}

	public void setCantDosis(Integer cantDosis) {
		this.cantDosis = cantDosis;
	}

	public Date getExpira() {
		return expira;
	}

	public void setExpira(Date expira) {
		this.expira = expira;
	}
	
	
}