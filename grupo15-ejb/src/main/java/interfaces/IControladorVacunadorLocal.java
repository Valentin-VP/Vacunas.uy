package interfaces;

import java.util.Date;

import javax.ejb.Local;

import datatypes.DtPuesto;
import exceptions.FechaIncorrecta;
import exceptions.SinPuestosLibres;
import exceptions.UsuarioInexistente;
import exceptions.VacunadorSinAsignar;
import exceptions.VacunatorioNoCargadoException;

@Local
public interface IControladorVacunadorLocal {
	public void asignarVacunadorAVacunatorio(int idVacunador, String idVacunatorio, Date fecha) throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta;
	public DtPuesto consultarPuestoAsignadoVacunador(int idVacunador, String idVacunatorio) throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar;
}
