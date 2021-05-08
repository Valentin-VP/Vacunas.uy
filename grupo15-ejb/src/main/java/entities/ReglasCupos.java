package entities;

import java.time.LocalTime;
import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.Id;



import datatypes.DtHora;


@Entity
public class ReglasCupos {
	@Id
	private String id;
	private Integer duracionTurno;
	
	private LocalTime horaApertura;
	private LocalTime horaCierre;
	public ReglasCupos(String id, Integer duracionTurno, LocalTime horaApertura, LocalTime horaCierre) {
		super();
		this.id = id;
		this.duracionTurno = duracionTurno;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
	}
	public ReglasCupos() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getDuracionTurno() {
		return duracionTurno;
	}
	public void setDuracionTurno(Integer duracionTurno) {
		this.duracionTurno = duracionTurno;
	}
	public LocalTime getHoraApertura() {
		return horaApertura;
	}
	public void setHoraApertura(LocalTime horaApertura) {
		this.horaApertura = horaApertura;
	}
	public LocalTime getHoraCierre() {
		return horaCierre;
	}
	public void setHoraCierre(LocalTime horaCierre) {
		this.horaCierre = horaCierre;
	}
	


}