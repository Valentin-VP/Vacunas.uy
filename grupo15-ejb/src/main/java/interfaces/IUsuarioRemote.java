package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.ejb.Remote;

import datatypes.DtCiudadano;
import datatypes.DtDireccion;
import datatypes.DtUsuario;
import datatypes.DtUsuarioInterno;
import datatypes.DtUsuarioSoap;
import datatypes.DtVacunador;
import datatypes.Rol;
import datatypes.Sexo;
import exceptions.UsuarioExistente;
import exceptions.UsuarioInexistente;

	
@Remote
public interface IUsuarioRemote {

	public void agregarUsuarioVacunador(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email, DtDireccion direccion, Sexo sexo) throws UsuarioExistente;
	public void agregarUsuarioInterno(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email, DtDireccion direccion, Sexo sexo, Rol rol) throws UsuarioExistente;
	public void agregarUsuarioCiudadano(int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email, DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado) throws UsuarioExistente;
	public DtUsuario buscarUsuario(int IdUsuario);
	public DtCiudadano buscarCiudadano(int id) throws UsuarioInexistente;
	public DtVacunador buscarVacunador(int id) throws UsuarioInexistente;
	public DtUsuarioInterno buscarUsuarioInterno(int id) throws UsuarioInexistente;
	public ArrayList<DtCiudadano> listarCiudadanos();
	public ArrayList<DtUsuarioInterno> listarUsuariosInternos();
	public ArrayList<DtVacunador> listarVacunadores();
	public void EliminarUsuario(int IdUsuario) throws UsuarioInexistente;
	public void ModificarCiudadano(DtCiudadano ciudadano) throws UsuarioInexistente;
	public void ModificarUsuarioInterno(DtUsuarioInterno usu) throws UsuarioInexistente;
	public void ModificarVacunador(DtVacunador vacunador) throws UsuarioInexistente;
	public ArrayList<DtUsuarioSoap> listarVacunadoresSoap();
	public DtUsuarioSoap buscarVacunadorSoap(int id) throws UsuarioInexistente;
	}


