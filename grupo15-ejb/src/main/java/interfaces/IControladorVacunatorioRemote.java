package interfaces;

import java.util.ArrayList;


import javax.ejb.Remote;

import datatypes.DtDireccion;
import datatypes.DtVacunatorio;
import exceptions.VacunatorioCargadoException;
import exceptions.VacunatorioNoCargadoException;
import exceptions.VacunatoriosNoCargadosException;


@Remote
public interface IControladorVacunatorioRemote {


	public void agregarVacunatorio(String id, String nombre, DtDireccion dtDir, Integer telefono, Float latitud, Float longitud) throws VacunatorioCargadoException ;
	public DtVacunatorio obtenerVacunatorio(String id) throws VacunatorioNoCargadoException;
	public ArrayList<DtVacunatorio> listarVacunatorio()throws VacunatoriosNoCargadosException;
	
	
}
