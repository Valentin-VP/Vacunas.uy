package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import datatypes.DtDireccion;
import datatypes.Sexo;



@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Usuario {
	
	@Id 
	private int idUsuario;

	private String nombre;
	private String apellido;
	private Date fechaNac;
	private String email;
	private DtDireccion direccion;
	private Sexo sexo;
	

	
	
	public Usuario() {
		super();
	}

	public Usuario( int IdUsuario, String nombre, String apellido, Date fechaNac, String email, DtDireccion direccion, Sexo sexo) {
		super();
		this.idUsuario = IdUsuario;
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
		return idUsuario;
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

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}
/*
	public CertificadoVacunacion getCertificado() {
		return certificado;
	}

	public void setCertificado(CertificadoVacunacion certificado) {
		this.certificado = certificado;
	}
*/
	
	
}
