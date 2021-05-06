package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import datatypes.DtDireccion;
import datatypes.Sexo;

@Entity
@DiscriminatorValue("C")
public class Ciudadano extends Usuario {


	private String TipoSector;
	private boolean autenticado;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Reserva> reservas = new ArrayList<>();
	
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

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	
}
