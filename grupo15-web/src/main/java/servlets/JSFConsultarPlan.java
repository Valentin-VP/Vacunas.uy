package servlets;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
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
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtPlanVacunacion;
import exceptions.PlanVacunacionInexistente;

@Named("ConsultarPlan")
@RequestScoped
public class JSFConsultarPlan {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token; 
	private String plan;
	private List<DtPlanVacunacion> planes = new ArrayList<>();
	private String nombre;
	private String descripcion;
	private String enfermedad;
	private String etapas;
			
	@EJB
	interfaces.IPlanVacunacionLocal ipv;
	
	public String getPlan() {
		return plan;
	}


	public void setPlan(String plan) {
		this.plan = plan;
	}


	public List<DtPlanVacunacion> getPlanes() {
		return planes;
	}


	public void setPlanes(List<DtPlanVacunacion> planes) {
		this.planes = planes;
	}

	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
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


	public String getEnfermedad() {
		return enfermedad;
	}


	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}


	public String getEtapas() {
		return etapas;
	}


	public void setEtapas(String etapas) {
		this.etapas = etapas;
	}


	public JSFConsultarPlan() {}

	@PostConstruct
	public void cargaInicial() {
		try {
			this.planes = ipv.listarPlanesVacunacion();
		} catch (PlanVacunacionInexistente e) {
			e.printStackTrace();
		}
	}
	
	public void consultarPlan() {
		
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        // http://omnifaces-fans.blogspot.com/2015/10/jax-rs-consume-restful-web-service-from.html
        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/obtener?p=" + this.plan);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() != 201) {
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			this.nombre = reply.getString("nombre");
			this.descripcion = reply.getString("descripcion");
			this.enfermedad = reply.getString("enfermedad");
			this.etapas = reply.getJsonObject("etapa").getString("nombre");
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			try {
				context.redirect(context.getRequestContextPath() + "/JSF/ConsultarPlanVacunacion2");
			} catch (IOException e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Modificar:", e.getMessage()));
			}
		}else {
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Modificar:", message));
		}
		
		
	}
}
