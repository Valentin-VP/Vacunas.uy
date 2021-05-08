package entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import datatypes.EstadoReserva;
import persistence.ReservaID;

////TODO:Reserva es un tipo asociativo
@Entity
@IdClass(ReservaID.class)
public class Reserva implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3497082720868854723L;
	private LocalDateTime fechaRegistro;
	@Enumerated(EnumType.STRING)
	private EstadoReserva estado;
	
	@Id
	@ManyToOne
	@JoinColumn(insertable=false,updatable=false, referencedColumnName="id")
	@JoinColumn(insertable=false,updatable=false, referencedColumnName="planVacunacion_id")
	private Etapa etapa;
	
	@Id
	@ManyToOne
	@JoinColumn(
			insertable=false,
			updatable=false
	)
	private Ciudadano ciudadano;
	
	@ManyToOne
	@JoinColumn(insertable=false,updatable=false, referencedColumnName="id")
	@JoinColumn(insertable=false,updatable=false, referencedColumnName="vacunatorio_id")
	private Puesto puesto;
	
	public Reserva() {
		
	}
	
	//TODO: DtPuesto

	public Reserva(LocalDateTime fechaRegistro, EstadoReserva estado, Etapa etapa, Ciudadano ciudadano, Puesto puesto) {
		super();
		this.fechaRegistro = fechaRegistro;
		this.estado = estado;
		this.etapa = etapa;
		this.ciudadano = ciudadano;
		this.puesto = puesto;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}

	public Ciudadano getCiudadano() {
		return ciudadano;
	}

	public void setCiudadano(Ciudadano ciudadano) {
		this.ciudadano = ciudadano;
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
