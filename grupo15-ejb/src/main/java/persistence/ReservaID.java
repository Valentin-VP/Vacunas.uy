package persistence;

import java.io.Serializable;

public class ReservaID implements Serializable {
	private static final long serialVersionUID = 1L;

	private EtapaID etapa;
	private int usuario;
	public ReservaID() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReservaID(EtapaID etapa, int usuario) {
		super();
		this.etapa = etapa;
		this.usuario = usuario;
	}
	public EtapaID getEtapa() {
		return etapa;
	}
	public void setEtapa(EtapaID etapa) {
		this.etapa = etapa;
	}
	public int getUsuario() {
		return usuario;
	}
	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((etapa == null) ? 0 : etapa.hashCode());
		result = prime * result + usuario;
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
		if (etapa == null) {
			if (other.etapa != null)
				return false;
		} else if (!etapa.equals(other.etapa))
			return false;
		if (usuario != other.usuario)
			return false;
		return true;
	}
	

}
