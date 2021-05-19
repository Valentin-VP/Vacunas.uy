package datatypes;

import java.time.LocalDate;
import java.util.Date;

public class DtCiudadano extends DtUsuario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String TipoSector;
	private boolean autenticado;
	
	public DtCiudadano() {
		super();
	}
	
	public DtCiudadano( int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email, DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo);
		this.autenticado = autenticado;
		this.TipoSector = TipoSector;
		
	}

	public DtCiudadano( int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email, DtDireccion direccion, Sexo sexo, String token, String TipoSector, Boolean autenticado) {
		super(nombre, apellido, fechaNac, IdUsuario, email, direccion, sexo, token);
		this.autenticado = autenticado;
		this.TipoSector = TipoSector;
		
	}
	
	public String getTipoSector() {
		return TipoSector;
	}

	public void setTipoSector(String tipoSector) {
		TipoSector = tipoSector;
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}
	
}
