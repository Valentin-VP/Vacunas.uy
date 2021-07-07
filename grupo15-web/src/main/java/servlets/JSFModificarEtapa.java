package servlets;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;


@Named("ModificarEtapa")
@SessionScoped
public class JSFModificarEtapa implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token; 
	
	private String plan;
	private List<DtPlanVacunacion> planes;
	private String etapa;
	private List<DtEtapa> etapas;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private String condicion;
	private int edadMin;
	private int edadMax;
	private String sector;
	private List<String> sectores;
	private String enf;
	
	
	@PostConstruct
	public void cargaInicial(){
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        // http://omnifaces-fans.blogspot.com/2015/10/jax-rs-consume-restful-web-service-from.html
        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			System.out.println("Entro al if de planes");
			setPlanes(response.readEntity(new GenericType<List<DtPlanVacunacion>>() {}));
			
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Error al cargar las etapas"));
		}
		
		//cargo los sectores
				conexion = ClientBuilder.newClient();
				webTarget = conexion.target(hostname + "/grupo15-services/rest/usuario/sectores");
				invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
				response = invocation.invoke();
				LOGGER.info("Respuesta: " + response.getStatus());
				if (response.getStatus() == 200) {
					this.sectores = response.readEntity(new GenericType<List<String>>() {});
				}
	}
	
	public void cargarEtapas() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/enf/"+plan);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.etapas = response.readEntity(new GenericType<List<DtEtapa>>() {});
			System.out.println(this.etapas);
		}
	}
	
	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public void modificar() {
		JSONObject datos = new JSONObject();
		this.condicion = this.getEdadMin() + "|" + this.getEdadMax() + "|" + this.getSector() + "|" + this.getEnf().toLowerCase();
		try {
			datos.put("id", etapa);
			datos.put("plan", plan);
			datos.put("fechaInicio", this.fechaInicio.toString());
			datos.put("fechaFin", this.fechaFin.toString());
			datos.put("condicion", this.condicion);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/etapa/modificar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(datos.toString(), MediaType.APPLICATION_JSON));
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificar:", "Etapa modificada con exito"));
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "al modificar la etapa"));
		}
		
	}
	
	public JSFModificarEtapa() {
		// TODO Auto-generated constructor stub
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public List<DtEtapa> getEtapas() {
		return etapas;
	}

	public void setEtapas(List<DtEtapa> etapas) {
		this.etapas = etapas;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public int getEdadMin() {
		return edadMin;
	}

	public void setEdadMin(int edadMin) {
		this.edadMin = edadMin;
	}

	public int getEdadMax() {
		return edadMax;
	}

	public void setEdadMax(int edadMax) {
		this.edadMax = edadMax;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getEnf() {
		return enf;
	}

	public void setEnf(String enf) {
		this.enf = enf;
	}

	public List<String> getSectores() {
		return sectores;
	}

	public void setSectores(List<String> sectores) {
		this.sectores = sectores;
	}

	public List<DtPlanVacunacion> getPlanes() {
		return planes;
	}

	public void setPlanes(List<DtPlanVacunacion> planes) {
		this.planes = planes;
	}

	
}
