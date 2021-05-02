package interfaces;

import javax.ejb.Remote;

import datatypes.DtCertificadoVac;
import exceptions.CertificadoInexistente;

@Remote
public interface ICertificadoVacunacionDAORemote {
	public DtCertificadoVac obtenerCertificadoVacunacion(int id) throws CertificadoInexistente;
}
