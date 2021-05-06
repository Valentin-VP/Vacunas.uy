package persistence;

import java.io.Serializable;

public class ReservaID implements Serializable {
	private static final long serialVersionUID = 1L;

	private EtapaID etapa;
	private int ciudadano;
	
	public ReservaID() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EtapaID getEtapa() {
		return etapa;
	}
	public void setEtapa(EtapaID etapa) {
		this.etapa = etapa;
	}
	public int getCiudadano() {
		return ciudadano;
	}
	public void setCiudadano(int ciudadano) {
		this.ciudadano = ciudadano;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ciudadano;
		result = prime * result + ((etapa == null) ? 0 : etapa.hashCode());
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
		ReservaID other = (ReservaID) obj;
		if (ciudadano != other.ciudadano)
			return false;
		if (etapa == null) {
			if (other.etapa != null)
				return false;
		} else if (!etapa.equals(other.etapa))
			return false;
		return true;
	}
	
	
}
