package interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtLaboratorio;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;

@Remote
public interface ILaboratorioRemote {

	public void agregarLaboratorio(String nombre) throws LaboratorioRepetido;
	
	public ArrayList<DtLaboratorio> listarLaboratorios() throws LaboratorioInexistente;
	
	public DtLaboratorio obtenerLaboratorio(String nombre) throws LaboratorioInexistente;
	
	public void eliminarLaboratorio(String nombre) throws LaboratorioInexistente;
}
