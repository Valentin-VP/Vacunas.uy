package datatypes;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;



public class DtLdap implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer ci;
	private String nombre;
	private String apellido;
	private String tipoUser;
	private String password;
	private Date fecha;
	private String direccion;
	private String email;
	private String sexo;
	
	
	
	
	
	public DtLdap() {
		super();
		// TODO Auto-generated constructor stub
	}



	public DtLdap(String apellido, Integer ci, String nombre, String tipoUser, String password) {
		super();
		this.ci = ci;
		this.nombre = nombre;
		
		this.apellido = apellido;
		this.tipoUser = tipoUser;
		this.password = password;
	}
	
	public DtLdap(String apellido, String email, Date fecha, String nombre, String tipoUser, String direccion, String sexo) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.tipoUser = tipoUser;
		this.direccion = direccion;
		this.sexo = sexo;
		this.email = email;
		this.fecha = fecha;
	
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Date getFecha() {
		return fecha;
	}



	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getSexo() {
		return sexo;
	}



	public void setSexo(String sexo) {
		this.sexo = sexo;
	}



	public Integer getCi() {
		return ci;
	}



	public void setCi(Integer ci) {
		this.ci = ci;
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



	public String getTipoUser() {
		return tipoUser;
	}



	public void setTipoUser(String tipoUser) {
		this.tipoUser = tipoUser;
	}

	
}

	