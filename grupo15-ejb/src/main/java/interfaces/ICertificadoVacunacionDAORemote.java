package interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtCertificadoVac;
import datatypes.DtConstancia;
import exceptions.CertificadoInexistente;
import exceptions.CertificadoRepetido;
import exceptions.ConstanciaInexistente;
import exceptions.UsuarioExistente;

@Remote
public interface ICertificadoVacunacionDAORemote {
	//public void agregarCertificadoVacunacion(int usuario, ArrayList<DtConstancia> constancias) throws CertificadoRepetido, ConstanciaInexistente, UsuarioExistente;
	//public void modificarCertificadoVacunacion(int usuario, ArrayList<DtConstancia> constancias) throws CertificadoRepetido, CertificadoInexistente, UsuarioExistente, ConstanciaInexistente;
	public DtCertificadoVac obtenerCertificadoVacunacion(int usuario) throws CertificadoInexistente, UsuarioExistente;
}