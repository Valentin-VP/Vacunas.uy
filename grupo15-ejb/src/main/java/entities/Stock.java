package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import persistence.StockID;

@Entity
@IdClass(StockID.class)
public class Stock {
	
	@Id
	@ManyToOne
	@JoinColumn(
			insertable=false,
			updatable=false,
			name="vacunatorio"
	)
	private Vacunatorio vacunatorio;
	
	@Id
	@ManyToOne
	@JoinColumn(
			insertable=false,
			updatable=false,
			name="vacuna"
	)
	private Vacuna vacuna;
	
	private Integer cantidad;
	private Integer descartadas;
	private Integer administradas;
	private Integer disponibles;
	
	@OneToMany(mappedBy="stock", cascade = CascadeType.ALL)
	private List<Historico> historicos = new ArrayList<Historico>();
	
	public Stock() {
	}

	public Stock(Vacunatorio vacunatorio, Vacuna vacuna, Integer cantidad, Integer descartadas, Integer administradas, Integer disponibles) {
		super();
		this.vacunatorio = vacunatorio;
		this.vacuna = vacuna;
		this.cantidad = cantidad;
		this.descartadas = descartadas;
		this.administradas = administradas;
		this.disponibles = disponibles;
	}

	public Vacunatorio getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(Vacunatorio vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	public Vacuna getVacuna() {
		return vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
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
