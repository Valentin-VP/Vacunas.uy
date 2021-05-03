package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtMensaje;
import exceptions.MensajeExistente;

@Local
public interface IMensajeLocal {

	public void agregarMensaje(int IdMensaje, String contenido) throws MensajeExistente;
	public DtMensaje BuscarMensaje(int IdMensaje) throws MensajeExistente;
	public void EliminarMensaje (int IdMensaje) throws MensajeExistente;
	public void ModificarMensaje(DtMensaje mensaje) throws MensajeExistente;
	public ArrayList<DtMensaje> listarChats();
}
