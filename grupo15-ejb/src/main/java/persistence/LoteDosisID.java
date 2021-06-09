package persistence;

import java.io.Serializable;

public class LoteDosisID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idLote;
	private String vacunatorio;
	private String vacuna;

	public LoteDosisID() {}


	public LoteDosisID(Integer idLote, String vacunatorio, String vacuna) {
		super();
		this.idLote = idLote;
		this.vacunatorio = vacunatorio;
		this.vacuna = vacuna;
	}


	public Integer getIdLote() {
		return idLote;
	}

	public void setIdLote(Integer idLote) {
		this.idLote = idLote;
	}

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	public String getVacuna() {
		return vacuna;
	}

	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLote == null) ? 0 : idLote.hashCode());
		result = prime * result + ((vacuna == null) ? 0 : vacuna.hashCode());
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
		LoteDosisID other = (LoteDosisID) obj;
		if (idLote == null) {
			if (other.idLote != null)
				return false;
		} else if (!idLote.equals(other.idLote))
			return false;
		if (vacuna == null) {
			if (other.vacuna != null)
				return false;
		} else if (!vacuna.equals(other.vacuna))
			return false;
		if (vacunatorio == null) {
			if (other.vacunatorio != null)
				return false;
		} else if (!vacunatorio.equals(other.vacunatorio))
			return false;
		return true;
	}

}
