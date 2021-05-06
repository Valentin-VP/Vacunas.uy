package persistence;

import java.io.Serializable;
import java.util.Date;

public class HistoricoID implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date fecha;
	private StockID stock;
	
	public HistoricoID() {
		// TODO Auto-generated constructor stub
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public StockID getStock() {
		return stock;
	}

	public void setStock(StockID stock) {
		this.stock = stock;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
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
		HistoricoID other = (HistoricoID) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		return true;
	}

}
