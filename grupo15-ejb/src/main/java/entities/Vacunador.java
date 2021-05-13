package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import datatypes.DtDireccion;

import datatypes.Sexo;

@Entity
@DiscriminatorValue("vacunador")
public class Vacunador extends Usuario {

	@ManyToMany (cascade = CascadeType.ALL)
	private List<Chat> chats = new ArrayList<Chat>();

	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Asignado> asignado = new ArrayList<Asignado>(); //asignado
	 
	public Vacunador() {
		super();
	}
	
	public Vacunador( int IdUsuario, String nombre, String apellido, LocalDate fechaNac, String email, DtDireccion direccion, Sexo sexo) {
		super(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
	
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public List<Asignado> getAsignado() {
		return asignado;
	}

	public void setAsignado(List<Asignado> asignado) {
		this.asignado = asignado;
	}


	
	
}
