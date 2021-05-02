package interfaces;

import java.util.ArrayList;
import java.util.Date;


import javax.ejb.Remote;

import datatypes.DtCiudadano;
import datatypes.DtDireccion;
import datatypes.DtUsuario;
import datatypes.DtUsuarioInterno;
import datatypes.DtVacunador;
import datatypes.Rol;
import datatypes.Sexo;
import exceptions.UsuarioExistente;

	
@Remote
public interface IUsuarioRemote {

	public void agregarUsuarioVacunador(int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo) throws UsuarioExistente;
	public void agregarUsuarioInterno(int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo, String password, Rol rol) throws UsuarioExistente;
	public void agregarUsuarioCiudadano(int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado) throws UsuarioExistente;
	public DtUsuario buscarUsuario(int IdUsuario);
	public ArrayList<DtCiudadano> listarCiudadanos();
	public ArrayList<DtUsuarioInterno> listarUsuariosInternos();
	public ArrayList<DtVacunador> listarVacunadores();
	public void EliminarUsuario(int IdUsuario) throws UsuarioExistente;
	public void ModificarCiudadano(DtCiudadano ciudadano) throws UsuarioExistente;
	public void ModificarUsuarioInterno(DtUsuarioInterno usu) throws UsuarioExistente;
	public void ModificarVacunador(DtVacunador vacunador) throws UsuarioExistente;
	
	}


