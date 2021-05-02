package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtLaboratorio;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;

@Local
public interface ILaboratorioLocal {

	public void agregarVacuna(String nombre) throws LaboratorioRepetido;
	
	public ArrayList<DtLaboratorio> listarLaboratorios() throws LaboratorioInexistente;
	
	public DtLaboratorio obtenerLaboratorio(String nombre) throws LaboratorioInexistente;
}
