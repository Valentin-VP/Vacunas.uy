package datatypes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DtCupo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6812540570201770420L;
	private int idCupo;
	private boolean ocupado;
	private int idAgenda;
	public DtCupo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DtCupo(int idCupo, boolean ocupado, int idAgenda) {
		super();
		this.idCupo = idCupo;
		this.ocupado = ocupado;
		this.idAgenda = idAgenda;
	}
	public int getIdCupo() {
		return idCupo;
	}
	public void setIdCupo(int idCupo) {
		this.idCupo = idCupo;
	}
	public boolean isOcupado() {
		return ocupado;
	}
	public void setOcupado(boolean ocupado) {
		this.ocupado = ocupado;
	}
	public int getIdAgenda() {
		return idAgenda;
	}
	public void setIdAgenda(int idAgenda) {
		this.idAgenda = idAgenda;
	}

	
	
	
}
