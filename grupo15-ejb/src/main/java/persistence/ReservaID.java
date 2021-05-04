package persistence;

import java.io.Serializable;

public class ReservaID implements Serializable {
	private static final long serialVersionUID = 1L;

	private int etapa;
	private int usuario;
	public ReservaID() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReservaID(int etapa, int usuario) {
		super();
		this.etapa = etapa;
		this.usuario = usuario;
	}
	public int getEtapa() {
		return etapa;
	}
	public void setEtapa(int etapa) {
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
		result = prime * result + etapa;
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
		if (etapa != other.etapa)
			return false;
		if (usuario != other.usuario)
			return false;
		return true;
	}
	

}
