package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import datatypes.EstadoLote;
import persistence.LoteDosisID;
import persistence.StockID;

@Entity
@IdClass(LoteDosisID.class)
public class LoteDosis {
	
	@Id @Column(nullable=false)
	private Integer idLote;
	
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
			nullable=false,
			name="vacuna"
	)
	private Vacuna vacuna;
	
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
	
	public LoteDosis(Integer idLote, Vacunatorio vacunatorio, Vacuna vacuna, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			float temperatura) {
		super();
		this.idLote = idLote;
		this.vacunatorio = vacunatorio;
		this.vacuna = vacuna;
		this.cantidadTotal = cantidadTotal;
		this.cantidadEntregada = cantidadEntregada;
		this.cantidadDescartada = cantidadDescartada;
		this.temperatura = temperatura;
		this.estadoLote = EstadoLote.SinAsignar;
		this.transportista = null;
	}

	
	public LoteDosis(Integer idLote, Vacunatorio vacunatorio, Vacuna vacuna, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			EstadoLote estadoLote, float temperatura, Transportista transportista) {
		super();
		this.idLote = idLote;
		this.vacunatorio = vacunatorio;
		this.vacuna = vacuna;
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

	
}