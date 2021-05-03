package interfaces;

import java.util.ArrayList;

import javax.ejb.Local;

import datatypes.DtChat;
import exceptions.ChatExistente;

@Local
public interface IChatLocal {

	public void agregarChat(String chat ) throws ChatExistente;
	public DtChat BuscarChat(String IdChat) throws ChatExistente;
	public void EliminarChat (String IdChat) throws ChatExistente;
	public void ModificarVacunador(DtChat chat) throws ChatExistente;
	public ArrayList<DtChat> listarChats();
}
