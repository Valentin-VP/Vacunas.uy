package entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import datatypes.DtDireccion;
import datatypes.Rol;
import datatypes.Sexo;

@Entity
@DiscriminatorValue("interno")
public class UsuarioInterno extends Usuario{

	private Rol rol;
	
	
	public UsuarioInterno() {
		super();
	}
	
	public UsuarioInterno( int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email, DtDireccion direccion, Sexo sexo, Rol rol) {
		super(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
		this.rol = rol;
	}


	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	
	
	
	
}
