package entities;

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
@DiscriminatorValue("V")
public class Vacunador extends Usuario {

	@ManyToMany (cascade = CascadeType.ALL)
	private List<Chat> chats = new ArrayList<Chat>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Puesto> puestos = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	private Asignado asignado = null; //asignado
	 
	public Vacunador() {
		super();
	}
	
	public Vacunador( int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo) {
		super(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
	
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	public List<Puesto> getPuestos() {
		return puestos;
	}

	public void setPuestos(List<Puesto> puestos) {
		this.puestos = puestos;
	}

	public Asignado getAsignado() {
		return asignado;
	}

	public void setAsignado(Asignado asignado) {
		this.asignado = asignado;
	}
	
	
}
