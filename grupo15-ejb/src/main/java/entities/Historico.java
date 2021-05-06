package entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import persistence.HistoricoID;

@Entity
@IdClass(HistoricoID.class)
public class Historico {
	@Id
	@Temporal(TemporalType.DATE)
	private Date fecha;
	private Integer cantidad;
	private Integer descartadas;
	private Integer disponibles;
	private Integer administradas;
	
	@Id
	@ManyToOne
	@JoinColumn(updatable=false,insertable=false, referencedColumnName="vacunatorio")
	@JoinColumn(updatable=false,insertable=false, referencedColumnName="vacuna")
	private Stock stock;

	public Historico() {
	}

	public Historico(Date fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, Stock stock) {
		super();
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.descartadas = descartadas;
		this.disponibles = disponibles;
		this.administradas = administradas;
		this.stock = stock;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getDescartadas() {
		return descartadas;
	}

	public void setDescartadas(Integer descartadas) {
		this.descartadas = descartadas;
	}

	public Integer getDisponibles() {
		return disponibles;
	}

	public void setDisponibles(Integer disponibles) {
		this.disponibles = disponibles;
	}

	public Integer getAdministradas() {
		return administradas;
	}

	public void setAdministradas(Integer administradas) {
		this.administradas = administradas;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
