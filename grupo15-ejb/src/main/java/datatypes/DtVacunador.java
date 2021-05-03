package datatypes;

import java.util.Date;

public class DtVacunador extends DtUsuario{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public DtVacunador() {
		super();
	}
	
	
	public DtVacunador(String nombre, String apellido, Date fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
	
	}
}
