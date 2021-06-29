package persistence;

import java.io.Serializable;

public class StockID implements Serializable {
	private static final long serialVersionUID = 1L;

	private String vacunatorio;
	private String vacuna;
	
	public StockID() {
	}

	public StockID(String vacunatorio, String vacuna) {
		super();
		this.vacunatorio = vacunatorio;
		this.vacuna = vacuna;
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
		StockID other = (StockID) obj;
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
