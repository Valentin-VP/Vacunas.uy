package persistence;

import java.io.Serializable;

public class PuestoID implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String vacunatorio;
	
	public PuestoID() {
		// TODO Auto-generated constructor stub
	}

	
	
	public PuestoID(String id, String vacunatorio) {
		super();
		this.id = id;
		this.vacunatorio = vacunatorio;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((vacunatorio == null) ? 0 : vacunatorio.hashCode());
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
		PuestoID other = (PuestoID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (vacunatorio == null) {
			if (other.vacunatorio != null)
				return false;
		} else if (!vacunatorio.equals(other.vacunatorio))
			return false;
		return true;
	}


	
}
