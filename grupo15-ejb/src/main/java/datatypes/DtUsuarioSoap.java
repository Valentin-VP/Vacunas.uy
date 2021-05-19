package datatypes;

import java.io.Serializable;
import java.time.LocalDate;

public class DtUsuarioSoap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String apellido;
	private String fechaNac;
	private int IdUsuario;
	private String email;
	private DtDireccion direccion;
	private Sexo sexo;
	private String token = null;
	public DtUsuarioSoap() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtUsuarioSoap(String nombre, String apellido, String fechaNac, int idUsuario, String email,
			DtDireccion direccion, Sexo sexo, String token) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNac = fechaNac;
		this.IdUsuario = idUsuario;
		this.email = email;
		this.direccion = direccion;
		this.sexo = sexo;
		this.token = token;
	}
	
	
	public DtUsuarioSoap(String nombre, String apellido, String fechaNac, int idUsuario, String email,
			DtDireccion direccion, Sexo sexo) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNac = fechaNac;
		IdUsuario = idUsuario;
		this.email = email;
		this.direccion = direccion;
		this.sexo = sexo;
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
	public String getFechaNac() {
		return fechaNac;
	}
	public void setFechaNac(String fechaNac) {
		this.fechaNac = fechaNac;
	}
	public int getIdUsuario() {
		return IdUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		IdUsuario = idUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public DtDireccion getDireccion() {
		return direccion;
	}
	public void setDireccion(DtDireccion direccion) {
		this.direccion = direccion;
	}
	public Sexo getSexo() {
		return sexo;
	}
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
