package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtEnfermedad;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;

@Local
public interface IEnfermedadLocal {

	public void agregarEnfermedad(String nombre) throws EnfermedadRepetida;
	
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente;

	public DtEnfermedad obtenerLaboratorio(String nombre) throws EnfermedadInexistente;
	
	public void eliminarEnfermedad(String nombre) throws EnfermedadInexistente;
}
