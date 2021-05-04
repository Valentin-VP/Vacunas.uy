package entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
public class Historico {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private LocalDate fecha;
	private Integer cantidad;
	private Integer descartadas;
	private Integer disponibles;
	private Integer administradas;
	
	@ManyToOne
	@JoinColumn(updatable=false,insertable=false, referencedColumnName="vacunatorio")
	@JoinColumn(updatable=false,insertable=false, referencedColumnName="vacuna")
	private Stock stock;

	public Historico() {
	}

	public Historico(LocalDate fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, Stock stock) {
		super();
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.descartadas = descartadas;
		this.disponibles = disponibles;
		this.administradas = administradas;
		this.stock = stock;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
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
