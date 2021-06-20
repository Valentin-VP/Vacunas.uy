
package interfaces;

import java.time.LocalTime;
import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtDireccion;
import datatypes.DtVacunatorio;
import exceptions.ReglasCuposCargadoException;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Local
public interface IControladorVacunatorioLocal {

	public void agregarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud, String url) throws VacunatorioCargadoException ;
	public void setURLtoVacunatorio(String id, String url) throws VacunatorioNoCargadoException;
	public void generarTokenVacunatorio(String id) throws VacunatorioNoCargadoException;
	public DtVacunatorio obtenerVacunatorio(String id) throws VacunatorioNoCargadoException;
	public ArrayList<DtVacunatorio> listarVacunatorio()throws VacunatoriosNoCargadosException;
	public void modificarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud,
			Float longitud, String url) throws VacunatorioNoCargadoException;
	public void eliminarVacunatorio(String id) throws VacunatorioNoCargadoException;
	public void agregarReglasCupos(String idVac, String id, Integer duracionTurno, LocalTime horaApertura,
			LocalTime horaCierre) throws VacunatorioNoCargadoException, ReglasCuposCargadoException;
	public boolean isTokenCorrecto(String id, String token) throws VacunatorioNoCargadoException;
}
