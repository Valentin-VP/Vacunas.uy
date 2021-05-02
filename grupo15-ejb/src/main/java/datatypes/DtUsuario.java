package datatypes;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.io.Serializable;

public class DtUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String apellido;
	private Date fechaNac;
	private int IdUsuario;
	
	
	public DtUsuario() {
		super();
	}
	
	
	public DtUsuario(String nombre, String apellido, Date fechaNac, int IdUsuario) {
		super();
	
		this.nombre = nombre;
		this.apellido = apellido;
		this.IdUsuario = IdUsuario;
		this.fechaNac = fechaNac;
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
	

	public Date getFechaNac() {
		return fechaNac;
	}
	
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
	
	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;

	}
	
	public int getIdUsuario() {
		return IdUsuario;
	}
	
	public void setIdUsuario(int IdUsuario) {
		this.IdUsuario = IdUsuario;
	}
	
}
