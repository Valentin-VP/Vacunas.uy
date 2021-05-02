package entities;

import java.util.Date;

import datatypes.DtDireccion;
import datatypes.Sexo;

public class Ciudadano extends Usuario {

	private String TipoSector;
	private boolean autenticado;
	
	public Ciudadano() {
		super();
	}
	
	public Ciudadano( int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo, String TipoSector, Boolean autenticado) {
		super(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
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
