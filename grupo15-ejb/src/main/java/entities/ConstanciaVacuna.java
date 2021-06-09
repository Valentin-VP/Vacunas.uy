package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class ConstanciaVacuna {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private int idConstVac;
	private int periodoInmunidad;
	private int dosisRecibidas;
	private LocalDate fechaUltimaDosis;
	private String vacuna;
	
	@OneToOne
	private Reserva reserva;

	
	
	public ConstanciaVacuna(int periodoInmunidad, int dosisRecibidas, LocalDate fechaUltimaDosis,
			String vacuna, Reserva reserva) {
		super();
		//this.idConstVac = idConstVac;
		this.periodoInmunidad = periodoInmunidad;
		this.dosisRecibidas = dosisRecibidas;
		this.fechaUltimaDosis = fechaUltimaDosis;
		this.vacuna = vacuna;
		this.reserva = reserva;
	}

	public ConstanciaVacuna() {
		super();
		// TODO Auto-generated constructor stub
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

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	
	
}
