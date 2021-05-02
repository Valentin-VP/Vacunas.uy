package entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import datatypes.DtDireccion;
import datatypes.Sexo;



@Entity
public abstract class Usuario {
	
	@Id 
	private int IdUsuario;

	private String nombre;
	private String apellido;
	private Date fechaNac;
	private String email;
	private DtDireccion direccion;
	private Sexo sexo;
	
	@OneToOne 
	CertificadoVacunacion certificado;
	
	public Usuario() {
		super();
	}

	public Usuario( int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo) {
		super();
		this.IdUsuario = IdUsuario;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNac = fechaNac;
		this.email = email;
		this.direccion = direccion;
		this.sexo = sexo;
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

	public int getIdUsuario() {
		return IdUsuario;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	
	
	public String getApellido() {
		return apellido;
	}
	
	
	public Date getFechaNac() {
		return fechaNac;
	}
	
	
	
	
}
