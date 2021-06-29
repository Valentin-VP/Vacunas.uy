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
import javax.json.JsonArray;
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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtEnfermedad;
import datatypes.DtPlanVacunacion;
import net.bytebuddy.asm.Advice.This;

@Named("ModificarPlan")
@RequestScoped
public class JSFModificarPlanVacunacionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	private List<DtPlanVacunacion> dtPlanes = new ArrayList<DtPlanVacunacion>();
	private String plan;
	private String nombreAnterior;
	private String descripcionAnterior;
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

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
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

	public String getNombreAnterior() {
		return nombreAnterior;
	}

	public void setNombreAnterior(String nombreAnterior) {
		this.nombreAnterior = nombreAnterior;
	}

	public String getDescripcionAnterior() {
		return descripcionAnterior;
	}

	public void setDescripcionAnterior(String descripcionAnterior) {
		this.descripcionAnterior = descripcionAnterior;
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
	        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/listar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				dtPlanes = response.readEntity(new GenericType<List<DtPlanVacunacion>>() {
				});
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
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
		if (response.getStatus() == 200) {
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			this.setNombreAnterior(reply.getString("nombre"));
			this.setDescripcionAnterior(reply.getString("descripcion"));		
			
		}else {
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Modificar:", message));
		}
	}
	
	public void modificarPlan() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        JSONObject plan = new JSONObject();
        for (DtPlanVacunacion each: this.getDtPlanes()) {
        	if (each.getId()== Integer.parseInt(this.plan)) {
        		try {
        			plan.put("id", this.getPlan());
        			plan.put("nombre", this.getNombre());
					plan.put("descripcion", this.getDescripcion());
					
					// http://omnifaces-fans.blogspot.com/2015/10/jax-rs-consume-restful-web-service-from.html
			        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
			        LOGGER.info("El server name es: " + hostname);
			        LOGGER.info("Modificando!!!!!!!");
					Client conexion = ClientBuilder.newClient();
					WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/modificar");
					Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(plan.toString(), MediaType.APPLICATION_JSON));
					Response response = invocation.invoke();
					LOGGER.info("Respuesta: " + response.getStatus());
					if (response.getStatus() == 200) {
						LOGGER.info("Modificado");
						String jsonString = response.readEntity(String.class);
						JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
						JsonObject reply = jsonReader.readObject();
						String message = reply.getString("message");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modificar:", message));
					}
					else {
						String jsonString = response.readEntity(String.class);
						JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
						JsonObject reply = jsonReader.readObject();
						String message = reply.getString("message");
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Crear:", message));
					}
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
				}
        	}
        }
        
        
	}
}
