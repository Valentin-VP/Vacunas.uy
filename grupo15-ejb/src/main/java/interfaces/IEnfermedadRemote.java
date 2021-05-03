package interfaces;

import java.util.ArrayList;

import datatypes.DtEnfermedad;
import exceptions.EnfermedadInexistente;
import exceptions.EnfermedadRepetida;

public interface IEnfermedadRemote {

public void agregarEnfermedad(String nombre) throws EnfermedadRepetida;
	
	public ArrayList<DtEnfermedad> listarEnfermedades() throws EnfermedadInexistente;

	public DtEnfermedad obtenerLaboratorio(String nombre) throws EnfermedadInexistente;
}
