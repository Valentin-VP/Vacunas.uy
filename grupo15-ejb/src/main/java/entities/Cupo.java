package entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cupo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3773853247340067319L;
	@Id
	private int idCupo;
	private boolean ocupado;
	
	
	public Cupo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Cupo(int id, boolean ocupado) {
		this.idCupo = id;
		this.ocupado = ocupado;
		// TODO Auto-generated constructor stub
	}
	
	public int getIdCupo() {
		return idCupo;
	}
	public void setIdCupo(int idCupo) {
		this.idCupo = idCupo;
	}
	public boolean isOcupado() {
		return ocupado;
	}
	public void setOcupado(boolean ocupado) {
		this.ocupado = ocupado;
	}
	
}
