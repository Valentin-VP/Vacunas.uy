package entities;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import datatypes.DtDireccion;
import datatypes.Rol;
import datatypes.Sexo;

@Entity
@DiscriminatorValue("UI")
public class UsuarioInterno extends Usuario{

	private String password;
	private Rol rol;
	
	
	public UsuarioInterno() {
		super();
	}
	
	public UsuarioInterno( int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo, String password, Rol rol) {
		super(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
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
