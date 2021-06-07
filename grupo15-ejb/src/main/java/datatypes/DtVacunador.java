package datatypes;

import java.time.LocalDate;

public class DtVacunador extends DtUsuario{


	private static final long serialVersionUID = 1L;


	public DtVacunador() {
		super();
	}
	
	
	public DtVacunador(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
	
	}
	
	public DtVacunador(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo, String token) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo, token);
	
	}


	@Override
	public int hashCode() {
		return super.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
	
}
