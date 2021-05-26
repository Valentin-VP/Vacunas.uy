package interfaces;

import java.util.ArrayList;

import datatypes.DtEnfermedad;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;

public interface IEnfermedadRemote {

public void agregarEnfermedad(String nombre) throws EnfermedadRepetida;
	
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente;

	public DtEnfermedad obtenerEnfermedad(String nombre) throws EnfermedadInexistente;
	
	public void eliminarEnfermedad(String nombre) throws EnfermedadInexistente, AccionInvalida;
}
