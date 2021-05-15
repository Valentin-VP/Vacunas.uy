package datatypes;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class DtLdap implements Serializable {
	private static final long serialVersionUID = 1L;
	private String idUsuario;
	private String nombre;
	private String apellido;
	private String rol;
	
	
	
	
	public DtLdap() {
		super();
		// TODO Auto-generated constructor stub
	}



	public DtLdap(String idUsuario, String nombre,  String apellido, String rol) {
		super();
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		
		this.apellido = apellido;
		this.rol = rol;
	}



	public String getIdUsuario() {
		return idUsuario;
	}



	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getApellido() {
		return apellido;
	}



	public void setApellido(String apellido) {
		this.apellido = apellido;
	}



	public String getRol() {
		return rol;
	}



	public void setRol(String rol) {
		this.rol = rol;
	}
	
}
