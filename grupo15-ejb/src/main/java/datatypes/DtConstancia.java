package datatypes;

import java.io.Serializable;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtConstancia  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8298191415915420521L;
	
	private int idConstVac;
	private int periodoInmunidad;
	private int dosisRecibidas;
	private LocalDate fechaUltimaDosis;
	private String vacuna;
	////TODO:List<DtLoteDosis> lote = new ArrayList<DtLoteDosis>();
	
	private DtReserva reserva;

	public DtConstancia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DtConstancia(int idConstVac, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis,
			String vacuna, DtReserva reserva) {
		super();
		this.idConstVac = idConstVac;
		this.periodoInmunidad = periodoInmunidad;
		this.dosisRecibidas = dosisRecibidas;
		this.fechaUltimaDosis = fechaUltimaDosis;
		this.vacuna = vacuna;
		this.reserva = reserva;
	}

	public int getIdConstVac() {
		return idConstVac;
	}

	public void setIdConstVac(int idConstVac) {
		this.idConstVac = idConstVac;
	}

	public int getPeriodoInmunidad() {
		return periodoInmunidad;
	}

	public void setPeriodoInmunidad(int periodoInmunidad) {
		this.periodoInmunidad = periodoInmunidad;
	}

	public int getDosisRecibidas() {
		return dosisRecibidas;
	}

	public void setDosisRecibidas(int dosisRecibidas) {
		this.dosisRecibidas = dosisRecibidas;
	}

	public LocalDate getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}

	public void setFechaUltimaDosis(LocalDate fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}

	public String getVacuna() {
		return vacuna;
	}

	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}

	public DtReserva getReserva() {
		return reserva;
	}

	public void setReserva(DtReserva reserva) {
		this.reserva = reserva;
	}
	
	
}
