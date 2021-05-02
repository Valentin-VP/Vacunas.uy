package interfaces;

import java.util.List;

import javax.ejb.Remote;

import datatypes.DtTransportista;
import datatypes.TransportistaInexistente;
import exceptions.TransportistaRepetido;

@Remote
public interface ITransportistaDaoRemote {

	public void agregarTransportista(Integer id)
			throws TransportistaRepetido;

	public DtTransportista obtenerTransportista(Integer id) throws TransportistaInexistente;

	public List<DtTransportista> listarTransportistas();
}
