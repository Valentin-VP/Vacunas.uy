package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtCertificadoVac;
import datatypes.DtConstancia;
import exceptions.CertificadoInexistente;
import exceptions.CertificadoRepetido;
import exceptions.ConstanciaInexistente;
import exceptions.UsuarioExistente;

@Local
public interface ICertificadoVacunacionDAOLocal {
	public void agregarCertificadoVacunacion(int usuario, ArrayList<DtConstancia> constancias) throws CertificadoRepetido, ConstanciaInexistente, UsuarioExistente;
	//public void modificarCertificadoVacunacion(int usuario, ArrayList<DtConstancia> constancias) throws CertificadoRepetido, CertificadoInexistente, UsuarioExistente, ConstanciaInexistente;
	public DtCertificadoVac obtenerCertificadoVacunacion(int usuario) throws CertificadoInexistente, UsuarioExistente;
}
