package datatypes;

import java.time.LocalDate;
import java.util.Date;

public class DtUsuarioInterno extends DtUsuario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Rol rol;
	
	public DtUsuarioInterno() {
		super();
	}
	
	
	public DtUsuarioInterno(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
	
	}
	
	public DtUsuarioInterno(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo, Rol rol) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
		this.rol = rol;
	
	}
	
	public DtUsuarioInterno(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo, Rol rol, String token) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo, token);
		this.rol = rol;
	
	}


	public Rol getRol() {
		return rol;
	}


	public void setRol(Rol rol) {
		this.rol = rol;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DtUsuarioInterno other = (DtUsuarioInterno) obj;
		if (rol != other.rol)
			return false;
		return true;
	}
	
	
}
