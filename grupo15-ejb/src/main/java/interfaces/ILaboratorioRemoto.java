package interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtLaboratorio;
import exceptions.LaboratorioInexistente;
import exceptions.LaboratorioRepetido;

@Remote
public interface ILaboratorioRemoto {

public void agregarVacuna(String nombre) throws LaboratorioRepetido;
	
	public ArrayList<DtLaboratorio> listarLaboratorios() throws LaboratorioInexistente;
	
	public DtLaboratorio obtenerLaboratorio(String nombre) throws LaboratorioInexistente;
}
