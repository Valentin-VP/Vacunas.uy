package interfaces;

import java.util.List;

import javax.ejb.Remote;

import datatypes.DtTransportista;
import datatypes.TransportistaInexistente;
import exceptions.TransportistaRepetido;

@Remote
public interface ITransportistaDaoRemote {
	public void agregarTransportista(Integer id, String url) throws TransportistaRepetido ;

	public void setURLtoTransportista(Integer id, String url) throws TransportistaInexistente;
	
	public void generarTokenTransportista(Integer id) throws TransportistaInexistente ;
	
	public DtTransportista obtenerTransportista(Integer id) throws TransportistaInexistente ;
	
	public List<DtTransportista> listarTransportistas() throws TransportistaInexistente;
	
	public boolean isTokenCorrecto(Integer id, String token) throws TransportistaInexistente;
}
