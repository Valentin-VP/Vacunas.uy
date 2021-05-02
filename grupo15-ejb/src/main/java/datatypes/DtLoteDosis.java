package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtLoteDosis implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer idLote;
	private Integer cantidadTotal;
	private Integer cantidadEntregada;
	private Integer cantidadDescartada;
	private String estadoLote;
	private float temperatura;
	private Integer transportista;
	
	public DtLoteDosis() {
		super();
	}

	
	public DtLoteDosis(Integer idLote, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			String estadoLote, float temperatura) {
		super();
		this.idLote = idLote;
		this.cantidadTotal = cantidadTotal;
		this.cantidadEntregada = cantidadEntregada;
		this.cantidadDescartada = cantidadDescartada;
		this.estadoLote = estadoLote;
		this.temperatura = temperatura;
	}

	
	
	public DtLoteDosis(Integer idLote, Integer cantidadTotal, Integer cantidadEntregada, Integer cantidadDescartada,
			String estadoLote, float temperatura, Integer transportista) {
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


	public String getEstadoLote() {
		return estadoLote;
	}


	public void setEstadoLote(String estadoLote) {
		this.estadoLote = estadoLote;
	}


	public float getTemperatura() {
		return temperatura;
	}


	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}


	public Integer getTransportista() {
		return transportista;
	}


	public void setTransportista(Integer transportista) {
		this.transportista = transportista;
	}
	
	
}