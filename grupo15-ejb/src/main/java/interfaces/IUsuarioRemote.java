package interfaces;

import java.util.ArrayList;
import java.util.Date;


import javax.ejb.Remote;

import Datatypes.DtUsuario;
import Excepciones.UsuarioExistente;

	
@Remote
public interface IUsuarioRemote {

		public void agregarUsuario(int IdUsuario, String nombre, String apellido, Date fechaNac) throws UsuarioExistente;
		public DtUsuario buscarUsuario(int IdUsuario);
		public ArrayList<DtUsuario> listarUsuarios();
	}


