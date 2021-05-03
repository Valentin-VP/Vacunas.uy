package datatypes;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class DtReglasCupos implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private Date fecha;
	private Integer duracionTurno;
	private DtHora horaApertura;
	private DtHora horaCierre;
	
	
	
	public DtReglasCupos() {
		super();
		// TODO Auto-generated constructor stub
	}



	public DtReglasCupos(String id, Date fecha, Integer duracionTurno,  DtHora horaApertura,
			DtHora horaCierre) {
		super();
		this.id = id;
		this.fecha = fecha;
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
