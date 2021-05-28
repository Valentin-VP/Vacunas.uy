package datatypes;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtDatosPlan implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String idPlan;
	private String nombre;
	private String desc;
	public DtDatosPlan() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtDatosPlan(String idPlan, String nombre, String desc) {
		super();
		this.idPlan = idPlan;
		this.nombre = nombre;
		this.desc = desc;
	}
	public String getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(String idPlan) {
		this.idPlan = idPlan;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
