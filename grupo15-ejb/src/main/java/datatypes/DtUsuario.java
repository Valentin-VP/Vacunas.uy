package datatypes;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.Calendar;
import java.io.Serializable;
import java.time.LocalDate;

@XmlRootElement
public class DtUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String apellido;
	private LocalDate fechaNac;
	private int IdUsuario;
	private String email;
	private DtDireccion direccion;
	private Sexo sexo;
	private String token = null;
	
	
	public DtUsuario() {
		super();
	}
	
	
	public DtUsuario(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo) {
		super();
	
		this.nombre = nombre;
		this.apellido = apellido;
		this.IdUsuario = IdUsuario;
		this.fechaNac = fechaNac;
		this.email = email;
		this.direccion = direccion;
		this.sexo = sexo;
	}
	
	public DtUsuario(String nombre, String apellido, LocalDate fechaNac, int IdUsuario, String email, DtDireccion direccion, Sexo sexo, String token) {
		super();
	
		this.nombre = nombre;
		this.apellido = apellido;
		this.IdUsuario = IdUsuario;
		this.fechaNac = fechaNac;
		this.email = email;
		this.direccion = direccion;
		this.sexo = sexo;
		this.token = token;
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
	

	public LocalDate getFechaNac() {
		return fechaNac;
	}
	/*
	public int getDia() {
		Calendar c2 = new GregorianCalendar();
		c2.setTime(fechaNac);
		int dia = c2.get(Calendar.DATE);
		return dia;
	}
	
	public int getMes() {
		Calendar c2 = new GregorianCalendar();
		c2.setTime(fechaNac);
		int mes = c2.get(Calendar.MONTH);
		return mes;
	}
	
	public int getMes2() {
		Calendar c2 = new GregorianCalendar();
		c2.setTime(fechaNac);
		int mes = c2.get(Calendar.MONTH);
		return mes+1;
	}
	
	public int getAnio() {
		Calendar c2 = new GregorianCalendar();
		c2.setTime(fechaNac);
		int anio = c2.get(Calendar.YEAR);
		return anio;
	}
	*/
	public void setFechaNac(LocalDate fechaNac) {
		this.fechaNac = fechaNac;

	}
	
	public int getIdUsuario() {
		return IdUsuario;
	}
	
	public void setIdUsuario(int IdUsuario) {
		this.IdUsuario = IdUsuario;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + IdUsuario;
		result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fechaNac == null) ? 0 : fechaNac.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DtUsuario other = (DtUsuario) obj;
		if (IdUsuario != other.IdUsuario)
			return false;
		if (apellido == null) {
			if (other.apellido != null)
				return false;
		} else if (!apellido.equals(other.apellido))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fechaNac == null) {
			if (other.fechaNac != null)
				return false;
		} else if (!fechaNac.equals(other.fechaNac))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (sexo != other.sexo)
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	
	
}
