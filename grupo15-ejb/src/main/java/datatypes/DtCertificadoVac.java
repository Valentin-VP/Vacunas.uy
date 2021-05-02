package datatypes;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtCertificadoVac implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1948532123451155350L;
	private int idCert;
	private ArrayList<DtConstancia> constancias;
	public DtCertificadoVac(int idCert, ArrayList<DtConstancia> constancias) {
		super();
		this.idCert = idCert;
		this.constancias = constancias;
	}
	public DtCertificadoVac() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getIdCert() {
		return idCert;
	}
	public void setIdCert(int idCert) {
		this.idCert = idCert;
	}
	public ArrayList<DtConstancia> getConstancias() {
		return constancias;
	}
	public void setConstancias(ArrayList<DtConstancia> constancias) {
		this.constancias = constancias;
	}
	
	
	
}
