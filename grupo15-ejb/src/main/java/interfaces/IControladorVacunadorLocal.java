package interfaces;

import java.time.LocalDate;

import javax.ejb.Local;

import datatypes.DtAsignado;
import exceptions.FechaIncorrecta;
import exceptions.SinPuestosLibres;
import exceptions.UsuarioInexistente;
import exceptions.VacunadorSinAsignar;
import exceptions.VacunatorioNoCargadoException;

@Local
public interface IControladorVacunadorLocal {
	public void asignarVacunadorAVacunatorio(int idVacunador, String idVacunatorio, LocalDate fecha) throws UsuarioInexistente, VacunatorioNoCargadoException, SinPuestosLibres, FechaIncorrecta ;
	public DtAsignado consultarPuestoAsignadoVacunador(int idVacunador, String idVacunatorio, LocalDate fecha) throws UsuarioInexistente, VacunatorioNoCargadoException, VacunadorSinAsignar;
	public String isVacunadorAsignadoEnFecha(int idVacunador, LocalDate fecha) ;
}
