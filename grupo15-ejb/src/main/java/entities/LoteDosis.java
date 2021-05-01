package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import datatypes.EstadoLote;

@Entity
public class LoteDosis {
	
	@Id @Column(nullable=false)
	private Integer idLote;
	
	@Column(nullable=false)
	private Integer cantidadDosis;
	private float temperatura;
	private String descripcion;
	private EstadoLote estadoLote;


	public LoteDosis() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LoteDosis(Integer idLote, Integer cantidadDosis, float temperatura, String descripcion) {
		super();
		this.idLote = idLote;
		this.cantidadDosis = cantidadDosis;
		this.temperatura = temperatura;
		this.descripcion = descripcion;
		this.estadoLote = EstadoLote.SinAsignar;
	}

	public LoteDosis(Integer idLote, Integer cantidadDosis, float temperatura, String descripcion, EstadoLote estadoLote) {
		super();
		this.idLote = idLote;
		this.cantidadDosis = cantidadDosis;
		this.temperatura = temperatura;
		this.descripcion = descripcion;
		this.estadoLote = estadoLote;
	}

	public Integer getIdLote() {
		return idLote;
	}

	public void setIdLote(Integer idLote) {
		this.idLote = idLote;
	}

	public Integer getCantidadDosis() {
		return cantidadDosis;
	}

	public void setCantidadDosis(Integer cantidadDosis) {
		this.cantidadDosis = cantidadDosis;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstadoLote getEstadoLote() {
		return estadoLote;
	}

	public void setEstadoLote(EstadoLote estadoLote) {
		this.estadoLote = estadoLote;
	} 
	
}