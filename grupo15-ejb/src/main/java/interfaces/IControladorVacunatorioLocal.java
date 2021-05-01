
package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtDireccion;
import datatypes.DtVacunatorio;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;

@Local
public interface IControladorVacunatorioLocal {

	public void agregarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud) throws VacunatorioCargadoException ;
	public DtVacunatorio obtenerVacunatorio(String id) throws VacunatorioNoCargadoException;
	public ArrayList<DtVacunatorio> listarVacunatorio()throws VacunatoriosNoCargadosException;
	
	
}
