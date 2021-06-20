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
	private ArrayList<DtConstancia> constancias = new ArrayList<>();
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constancias == null) ? 0 : constancias.hashCode());
		result = prime * result + idCert;
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
		DtCertificadoVac other = (DtCertificadoVac) obj;
		if (constancias == null) {
			if (other.constancias != null)
				return false;
		} else if (!constancias.equals(other.constancias))
			return false;
		if (idCert != other.idCert)
			return false;
		return true;
	}
	
	
	
}
