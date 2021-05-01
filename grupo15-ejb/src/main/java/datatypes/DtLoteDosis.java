package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtLoteDosis implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer idLote;
	private Integer cantidadDosis;
	private float temperatura;
	private String descripcion;
	private String estadoLote;
	
	public DtLoteDosis() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DtLoteDosis(Integer idLote, Integer cantidadDosis, float temperatura, String descripcion,
			String estadoLote) {
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

	public String getEstadoLote() {
		return estadoLote;
	}

	public void setEstadoLote(String estadoLote) {
		this.estadoLote = estadoLote;
	}
	
	
}