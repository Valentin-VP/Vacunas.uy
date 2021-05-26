package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtLaboratorio;
import exceptions.AccionInvalida;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;

@Local
public interface ILaboratorioLocal {

	public void agregarLaboratorio(String nombre) throws LaboratorioRepetido;
	
	public ArrayList<DtLaboratorio> listarLaboratorios() throws LaboratorioInexistente;
	
	public DtLaboratorio obtenerLaboratorio(String nombre) throws LaboratorioInexistente;

	public void eliminarLaboratorio(String nombre) throws LaboratorioInexistente, AccionInvalida;
}
