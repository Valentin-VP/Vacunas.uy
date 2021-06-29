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
	private String enfermedad; //lo agrego para poder enviar al fron de que enfermedad es esta vacuna
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
	

	public DtConstancia(int idConstVac, int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis,
			String vacuna, DtReserva reserva, String enfermedad) {
		super();
		this.idConstVac = idConstVac;
		this.periodoInmunidad = periodoInmunidad;
		this.dosisRecibidas = dosisRecibidas;
		this.fechaUltimaDosis = fechaUltimaDosis;
		this.vacuna = vacuna;
		this.reserva = reserva;
		this.enfermedad = enfermedad;
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

	public String getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dosisRecibidas;
		result = prime * result + ((enfermedad == null) ? 0 : enfermedad.hashCode());
		result = prime * result + ((fechaUltimaDosis == null) ? 0 : fechaUltimaDosis.hashCode());
		result = prime * result + idConstVac;
		result = prime * result + periodoInmunidad;
		result = prime * result + ((reserva == null) ? 0 : reserva.hashCode());
		result = prime * result + ((vacuna == null) ? 0 : vacuna.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DtConstancia other = (DtConstancia) obj;
		if (dosisRecibidas != other.dosisRecibidas)
			return false;
		if (enfermedad == null) {
			if (other.enfermedad != null)
				return false;
		} else if (!enfermedad.equals(other.enfermedad))
			return false;
		if (fechaUltimaDosis == null) {
			if (other.fechaUltimaDosis != null)
				return false;
		} else if (!fechaUltimaDosis.equals(other.fechaUltimaDosis))
			return false;
		if (idConstVac != other.idConstVac)
			return false;
		if (periodoInmunidad != other.periodoInmunidad)
			return false;
		if (reserva == null) {
			if (other.reserva != null)
				return false;
		} else if (!reserva.equals(other.reserva))
			return false;
		if (vacuna == null) {
			if (other.vacuna != null)
				return false;
		} else if (!vacuna.equals(other.vacuna))
			return false;
		return true;
	}
	
	
	
}
