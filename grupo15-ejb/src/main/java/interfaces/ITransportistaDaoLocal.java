package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DtTransportista;
import datatypes.TransportistaInexistente;
import exceptions.TransportistaRepetido;

@Local
public interface ITransportistaDaoLocal {

	public void agregarTransportista(Integer id)
			throws TransportistaRepetido;

	public DtTransportista obtenerTransportista(Integer id) throws TransportistaInexistente;

	public List<DtTransportista> listarTransportistas();
}
