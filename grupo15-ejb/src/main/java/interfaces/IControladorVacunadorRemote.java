package interfaces;

import java.util.Date;

import javax.ejb.Remote;

import datatypes.DtPuesto;
import exceptions.FechaIncorrecta;
import exceptions.SinPuestosLibres;
import exceptions.UsuarioInexistente;
import exceptions.VacunadorSinAsignar;
import exceptions.VacunatorioNoCargadoException;

@Remote
public interface IControladorVacunadorRemote {
	public void asignarVacunadorAVacunatorio(int idVacunador, String idVacunatorio, Date fecha) throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta;
	public DtPuesto consultarPuestoAsignadoVacunador(int idVacunador, String idVacunatorio) throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar;
}