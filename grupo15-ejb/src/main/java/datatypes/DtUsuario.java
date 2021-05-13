package datatypes;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.io.Serializable;
import java.time.LocalDate;

public class DtUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String apellido;
	private LocalDate fechaNac;
	private int IdUsuario;
	private String email;
	private DtDireccion direccion;
	private Sexo sexo;
	
	
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
	
	
}
