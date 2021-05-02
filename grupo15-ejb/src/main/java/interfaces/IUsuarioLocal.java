package interfaces;

import java.util.ArrayList;
import java.util.Date;


import javax.ejb.Local;

import Datatypes.DtUsuario;
import Excepciones.UsuarioExistente;


@Local
public interface IUsuarioLocal {

	public void agregarUsuario(int IdUsuario, String nombre, String apellido, Date fechaNac)throws UsuarioExistente;
	public DtUsuario buscarUsuario(int IdUsuario);
	public ArrayList<DtUsuario> listarUsuarios();
}
