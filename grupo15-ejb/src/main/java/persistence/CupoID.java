package persistence;

import java.io.Serializable;

public class CupoID implements Serializable {

	private static final long serialVersionUID = 1L;
	private int idCupo;
	private int agenda;

	public int getIdCupo() {
		return idCupo;
	}

	public void setIdCupo(int idCupo) {
		this.idCupo = idCupo;
	}

	public int getAgenda() {
		return agenda;
	}

	public void setAgenda(int agenda) {
		this.agenda = agenda;
	}

	public CupoID() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + agenda;
		result = prime * result + idCupo;
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
		CupoID other = (CupoID) obj;
		if (agenda != other.agenda)
			return false;
		if (idCupo != other.idCupo)
			return false;
		return true;
	}

}
