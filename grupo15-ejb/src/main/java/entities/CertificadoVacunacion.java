package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CertificadoVacunacion {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int idCert;
	
	@OneToMany
	private List<ConstanciaVacuna> constancias = new ArrayList<ConstanciaVacuna>();

	
	
	public CertificadoVacunacion(int idCert) {
		super();
		this.idCert = idCert;
	}

	public CertificadoVacunacion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getIdCert() {
		return idCert;
	}

	public void setIdCert(int idCert) {
		this.idCert = idCert;
	}

	public List<ConstanciaVacuna> getConstancias() {
		return constancias;
	}

	public void setConstancias(List<ConstanciaVacuna> constancias) {
		this.constancias = constancias;
	}
	
	
}
