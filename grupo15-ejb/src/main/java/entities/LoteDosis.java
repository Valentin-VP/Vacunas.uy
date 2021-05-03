package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import datatypes.EstadoLote;

@Entity
public class LoteDosis {
	
	@Id @Column(nullable=false)
	private Integer idLote;
	
	@Column(nullable=false)
	private Integer cantidadTotal;
	private Integer cantidadEntregada;
	private Integer cantidadDescartada;
	@Enumerated(EnumType.STRING)
	private EstadoLote estadoLote;
	private float temperatura;
	@ManyToOne
	private Transportista transportista;


	public LoteDosis() {
		super();
	}
	
	public LoteDosis(Integer idLote, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			float temperatura) {
		super();
		this.idLote = idLote;
		this.cantidadTotal = cantidadTotal;
		this.cantidadEntregada = cantidadEntregada;
		this.cantidadDescartada = cantidadDescartada;
		this.temperatura = temperatura;
		this.estadoLote = EstadoLote.SinAsignar;
		this.transportista = null;
	}

	
	public LoteDosis(Integer idLote, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			EstadoLote estadoLote, float temperatura, Transportista transportista) {
		super();
		this.idLote = idLote;
		this.cantidadTotal = cantidadTotal;
		this.cantidadEntregada = cantidadEntregada;
		this.cantidadDescartada = cantidadDescartada;
		this.estadoLote = estadoLote;
		this.temperatura = temperatura;
		this.transportista = transportista;
	}

	public Integer getIdLote() {
		return idLote;
	}

	public void setIdLote(Integer idLote) {
		this.idLote = idLote;
	}

	public Integer getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public Integer getCantidadEntregada() {
		return cantidadEntregada;
	}

	public void setCantidadEntregada(Integer cantidadEntregada) {
		this.cantidadEntregada = cantidadEntregada;
	}

	public Integer getCantidadDescartada() {
		return cantidadDescartada;
	}

	public void setCantidadDescartada(Integer cantidadDescartada) {
		this.cantidadDescartada = cantidadDescartada;
	}

	public EstadoLote getEstadoLote() {
		return estadoLote;
	}

	public void setEstadoLote(EstadoLote estadoLote) {
		this.estadoLote = estadoLote;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}

	public Transportista getTransportista() {
		return transportista;
	}

	public void setTransportista(Transportista transportista) {
		this.transportista = transportista;
	}

	
}