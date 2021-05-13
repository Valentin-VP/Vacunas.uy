package datatypes;

import java.time.LocalDate;
import java.util.Date;

public class DtUsuarioInterno extends DtUsuario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String password;
	private Rol rol;
	
	public DtUsuarioInterno() {
		super();
	}
	
	
	public DtUsuarioInterno(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
	
	}
	
	public DtUsuarioInterno(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo, String password, Rol rol) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
		this.password = password;
		this.rol = rol;
	
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Rol getRol() {
		return rol;
	}


	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	
}
