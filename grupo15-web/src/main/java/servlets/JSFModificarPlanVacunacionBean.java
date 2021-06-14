package servlets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import datatypes.DtPlanVacunacion;

@Named("ModificarPlan")
@RequestScoped
public class JSFModificarPlanVacunacionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	private List<DtPlanVacunacion> dtPlanes = new ArrayList<DtPlanVacunacion>();
	private DtPlanVacunacion plan;
	private String nombre;
	private String descripcion;

	public JSFModificarPlanVacunacionBean() {}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<DtPlanVacunacion> getDtPlanes() {
		return dtPlanes;
	}

	public void setDtPlanes(List<DtPlanVacunacion> dtPlanes) {
		this.dtPlanes = dtPlanes;
	}

	public DtPlanVacunacion getPlan() {
		return plan;
	}

	public void setPlan(DtPlanVacunacion plan) {
		this.plan = plan;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void modificarPlan() {
		
	}
}
