package datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtStock implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer cantidad;
	private Integer descartadas;
	private Integer disponibles;
	private Integer administradas;

	// Identidad del Stock: vacuna + vacunatorio (tipo asociativo)
	private String vacuna;
	private String vacunatorio;
	private List <DtHistoricoStock> historicos = new ArrayList<DtHistoricoStock>();

	public DtStock() {
	}

	public DtStock(String vacunatorio, String vacuna, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas) {
		super();
		this.vacuna = vacuna;
		this.vacunatorio = vacunatorio;
		this.cantidad = cantidad;
		this.descartadas = descartadas;
		this.disponibles = disponibles;
		this.administradas = administradas;
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

	public String getVacuna() {
		return vacuna;
	}

	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	public List <DtHistoricoStock> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List <DtHistoricoStock> historicos) {
		this.historicos = historicos;
	}

}
