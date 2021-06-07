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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((TipoSector == null) ? 0 : TipoSector.hashCode());
		result = prime * result + (autenticado ? 1231 : 1237);
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
		DtCiudadano other = (DtCiudadano) obj;
		if (TipoSector == null) {
			if (other.TipoSector != null)
				return false;
		} else if (!TipoSector.equals(other.TipoSector))
			return false;
		if (autenticado != other.autenticado)
			return false;
		return true;
	}
	
	
}
