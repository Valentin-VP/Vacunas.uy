package entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import datatypes.DtDireccion;
import datatypes.DtHora;

@Entity
public class ReglasCupos {
	@Id
	private String id;
	private Date fecha;
	private Integer duracionTurno;
	private DtHora horaApertura;
	private DtHora horaCierre;
	
	





public ReglasCupos(String id, Date fecha, Integer duracionTurno, DtHora horaApertura, DtHora horaCierre) {
		super();
		this.id = id;
		this.fecha = fecha;
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




public Date getFecha() {
	return fecha;
}




public void setFecha(Date fecha) {
	this.fecha = fecha;
}




public Integer getDuracionTurno() {
	return duracionTurno;
}




public void setDuracionTurno(Integer duracionTurno) {
	this.duracionTurno = duracionTurno;
}




public DtHora getHoraApertura() {
	return horaApertura;
}




public void setHoraApertura(DtHora horaApertura) {
	this.horaApertura = horaApertura;
}




public DtHora getHoraCierre() {
	return horaCierre;
}




public void setHoraCierre(DtHora horaCierre) {
	this.horaCierre = horaCierre;
}




}