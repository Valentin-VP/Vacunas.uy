package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Cupo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3773853247340067319L;
	@Id
	private int idCupo;
	private boolean ocupado;
	
	@JoinColumn
	@ManyToOne
	private Agenda agenda;
	
	
	public Cupo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Cupo(int idCupo, boolean ocupado, Agenda agenda) {
		super();
		this.idCupo = idCupo;
		this.ocupado = ocupado;
		this.agenda = agenda;
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

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}
	
	
	
}
