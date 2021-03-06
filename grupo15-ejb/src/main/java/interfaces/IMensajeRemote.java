package interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtMensaje;
import exceptions.MensajeExistente;

@Remote
public interface IMensajeRemote {

	public void agregarMensaje(String contenido) ;
	public DtMensaje BuscarMensaje(int IdMensaje) throws MensajeExistente;
	public void EliminarMensaje (int IdMensaje) throws MensajeExistente;
	public void ModificarMensaje(DtMensaje mensaje) throws MensajeExistente;
	public ArrayList<DtMensaje> listarMensajes();
}
