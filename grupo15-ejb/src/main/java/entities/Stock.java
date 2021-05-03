package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import persistence.StockID;

@Entity
@IdClass(StockID.class)
public class Stock {
	
	@Id
	@ManyToOne
	@JoinColumn(
			insertable=false,
			updatable=false
	)
	private Vacunatorio vacunatorio;
	
	@Id
	@ManyToOne
	@JoinColumn(
			insertable=false,
			updatable=false
	)
	private Vacuna vacuna;
	
	private Integer cantidad;
	private Integer descartadas;
	private Integer administradas;
	private Integer disponibles;
	
	public Stock() {
	}

	public Stock(Integer cantidad, Integer descartadas, Integer administradas, Integer disponibles) {
		super();
		this.cantidad = cantidad;
		this.descartadas = descartadas;
		this.administradas = administradas;
		this.disponibles = disponibles;
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

	public Integer getAdministradas() {
		return administradas;
	}

	public void setAdministradas(Integer administradas) {
		this.administradas = administradas;
	}

	public Integer getDisponibles() {
		return disponibles;
	}

	public void setDisponibles(Integer disponibles) {
		this.disponibles = disponibles;
	}

	
}
