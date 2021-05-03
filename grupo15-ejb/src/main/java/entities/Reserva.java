package entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import datatypes.EstadoReserva;

////TODO:Reserva es un tipo asociativo
@Entity
public class Reserva implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3497082720868854723L;
	@Id
	private int idReserva;
	private String nombreUser;
	private LocalDateTime fechaRegistro;
	@Enumerated(EnumType.STRING)
	private EstadoReserva estado;
	////TODO:private Etapa etapa;
	//private Usuario usuario;
	@ManyToOne
	@JoinColumn(name="puesto_id")
	private Puesto puesto;
	
	public Reserva() {
		
	}
	
	//TODO: DtPuesto
	public Reserva(int id, String nombre, LocalDateTime fecha, EstadoReserva state) {
		this.setIdReserva(id);
		this.setNombreUser(nombre);
		this.setFechaRegistro(fecha);
		this.setEstado(state);
	}

	public int getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	public String getNombreUser() {
		return nombreUser;
	}

	public void setNombreUser(String nombreUser) {
		this.nombreUser = nombreUser;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public Puesto getPuesto() {
		return puesto;
	}

	public void setPuesto(Puesto puesto) {
		this.puesto = puesto;
	}
	
	
}
