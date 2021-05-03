package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;


import datatypes.DtDireccion;

import datatypes.Sexo;

@Entity
public class Vacunador extends Usuario {

	@ManyToMany (cascade = CascadeType.ALL)
	private List<Chat> chats = new ArrayList<Chat>();
	
	
	public Vacunador() {
		super();
	}
	
	public Vacunador( int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo) {
		super(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
	
		
	}
}
