package servlets;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
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

import org.codehaus.jettison.json.JSONObject;

import datatypes.DtEnfermedad;
import datatypes.DtPlanVacunacion;

@Named("BorrarPlan")
@RequestScoped
public class JSFBorrarPlanVacunacionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	private List<String> nombres = new ArrayList<String>();
	private List<DtPlanVacunacion> dtPlanes = new ArrayList<DtPlanVacunacion>();
	private DtPlanVacunacion plan;
	private String nombre;
	
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public JSFBorrarPlanVacunacionBean() {}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getNombres() {
		return nombres;
	}

	public void setNombres(List<String> nombres) {
		this.nombres = nombres;
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
	
	@PostConstruct
	public void cargaInicial() {
		try {
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
					.get("x-access-token");
			if (cookie != null) {
				token = cookie.getValue();
				LOGGER.severe("Guardando cookie en Managed Bean: " + token);
			}
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = "https://" + origRequest.getServerName();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/listar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				this.dtPlanes = response.readEntity(new GenericType<List<DtPlanVacunacion>>() {});
				
				for(DtPlanVacunacion dt: dtPlanes) {
					this.nombres.add(dt.getNombre());
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
	}
	
	public void borrarPlan() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/eliminar").queryParam("p", this.nombre);
		LOGGER.severe("Conectando a : " + webTarget.getUri());
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildDelete();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Borrar:", "Plan de Vacunacion eliminado"));
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "" + response.getStatus()));
		}
		cargaInicial();
	}
}
