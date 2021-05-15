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



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
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

	