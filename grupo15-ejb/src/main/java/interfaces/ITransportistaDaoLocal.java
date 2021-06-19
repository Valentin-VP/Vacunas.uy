package interfaces;

import java.util.List;

import javax.ejb.Local;

import datatypes.DtTransportista;
import datatypes.TransportistaInexistente;
import exceptions.TransportistaRepetido;

@Local
public interface ITransportistaDaoLocal {

	public void agregarTransportista(Integer id, String url) throws TransportistaRepetido ;

	public void setURLtoTransportista(Integer id, String url) throws TransportistaInexistente;
	
	public DtTransportista obtenerTransportista(Integer id) throws TransportistaInexistente ;

	public List<DtTransportista> listarTransportistas() throws TransportistaInexistente;
}
