package datatypes;

import java.io.Serializable;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtHistoricoStock implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LocalDate fecha;
	private Integer cantidad;
	private Integer descartadas;
	private Integer disponibles;
	private Integer administradas;
	
	// Identidad del Stock: vacuna + vacunatorio (tipo asociativo)
	private String vacuna;
	private String vacunatorio;
	
	public DtHistoricoStock() {}
	
	public DtHistoricoStock(LocalDate fecha, Integer cantidad, Integer descartadas, Integer disponibles,
			Integer administradas, String vacuna, String vacunatorio) {
		super();
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.descartadas = descartadas;
		this.disponibles = disponibles;
		this.administradas = administradas;
		this.vacuna = vacuna;
		this.vacunatorio = vacunatorio;
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

}
