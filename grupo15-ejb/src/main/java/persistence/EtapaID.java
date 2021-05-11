package persistence;

import java.io.Serializable;

public class EtapaID implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id; //id de etapa
    private int planVacunacion; //id de plan vacunacion
    
    public EtapaID() {
    }

    
    
    public EtapaID(int id, int planVacunacion) {
		super();
		this.id = id;
		this.planVacunacion = planVacunacion;
	}



	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanVacunacion() {
        return planVacunacion;
    }

    public void setPlanVacunacion(int planVacunacion) {
        this.planVacunacion = planVacunacion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + planVacunacion;
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
        EtapaID other = (EtapaID) obj;
        if (id != other.id)
            return false;
        if (planVacunacion != other.planVacunacion)
            return false;
        return true;
    }

    
}
