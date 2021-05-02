package interfaces;

import javax.ejb.Local;

import datatypes.DtCertificadoVac;
import exceptions.CertificadoInexistente;

@Local
public interface ICertificadoVacunacionDAOLocal {
	public DtCertificadoVac obtenerCertificadoVacunacion(int id) throws CertificadoInexistente;
}
