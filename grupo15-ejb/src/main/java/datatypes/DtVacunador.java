package datatypes;

import java.util.Date;

public class DtVacunador extends DtUsuario{


	public DtVacunador() {
		super();
	}
	
	
	public DtVacunador(String nombre, String apellido, Date fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
	
	}
}
