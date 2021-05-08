package datatypes;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class DtReglasCupos implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private Integer duracionTurno;
	private LocalTime horaApertura;
	private LocalTime horaCierre;
	
	
	
	public DtReglasCupos() {
		super();
		// TODO Auto-generated constructor stub
	}



	public DtReglasCupos(String id, Integer duracionTurno,  LocalTime horaApertura,
			LocalTime horaCierre) {
		super();
		this.id = id;
		this.duracionTurno = duracionTurno;
		
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
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
