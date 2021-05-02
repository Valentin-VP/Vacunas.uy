package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import datatypes.DtDireccion;
import datatypes.Rol;
import datatypes.Sexo;

public class Vacunador extends Usuario {

	@ManyToMany (cascade = CascadeType.ALL)
	private List<Chat> chats = new ArrayList<Chat>();
	
	@OneToMany(mappedBy = "vacunador",cascade = CascadeType.ALL)
	private List<Mensaje> mensajes = new ArrayList<>();
	
	@ManyToMany(mappedBy = "vacunadores")
	List<Vacunatorio> vacunatorios = new ArrayList<>();
	
	@ManyToMany(mappedBy = "vacunador")
	List<Etapa> etapas = new ArrayList<>();
	
	public Vacunador() {
		super();
	}
	
	public Vacunador( int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo) {
		super(IdUsuario, nombre, apellido, fechaNac, email, direccion, sexo);
	
		
	}
}
