package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class DtDatosEtapa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String idEtapa;
	private String fIni;
	private String fFin;
	private String cond;
	private String idPlan;
	private String idVacuna;
	public DtDatosEtapa() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtDatosEtapa(String idEtapa, String fIni, String fFin, String cond, String idPlan, String idVacuna) {
		super();
		this.idEtapa = idEtapa;
		this.fIni = fIni;
		this.fFin = fFin;
		this.cond = cond;
		this.idPlan = idPlan;
		this.idVacuna = idVacuna;
	}
	public String getIdEtapa() {
		return idEtapa;
	}
	public void setIdEtapa(String idEtapa) {
		this.idEtapa = idEtapa;
	}
	public String getfIni() {
		return fIni;
	}
	public void setfIni(String fIni) {
		this.fIni = fIni;
	}
	public String getfFin() {
		return fFin;
	}
	public void setfFin(String fFin) {
		this.fFin = fFin;
	}
	public String getCond() {
		return cond;
	}
	public void setCond(String cond) {
		this.cond = cond;
	}
	public String getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(String idPlan) {
		this.idPlan = idPlan;
	}
	public String getIdVacuna() {
		return idVacuna;
	}
	public void setIdVacuna(String idVacuna) {
		this.idVacuna = idVacuna;
	}
}
