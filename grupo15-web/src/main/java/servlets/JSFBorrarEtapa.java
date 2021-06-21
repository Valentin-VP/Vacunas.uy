package servlets;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import datatypes.DtEtapa;
import datatypes.DtPlanVacunacion;

@Named("BorrarEtapa")
@RequestScoped
public class JSFBorrarEtapa {
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token;
	
	private String etapa;
	private List<DtEtapa> etapas = new ArrayList<DtEtapa>();
	private String plan;
	private List<DtPlanVacunacion> planes = new ArrayList<DtPlanVacunacion>();

	@EJB
	interfaces.IPlanVacunacionLocal pv;
	
	public JSFBorrarEtapa() {}
	
	@PostConstruct
	public void cargaInicial() {
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
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			System.out.println("Entro al if");
			this.planes = response.readEntity(new GenericType<List<DtPlanVacunacion>>() {});
		}else {
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar:", message));
		}
		
	}
	
	public void borrarEtapa() {
		
	}
	
	public void cargarEtapas(){
		System.out.println(plan);
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
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/plan/enf/"+plan);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			System.out.println("Entro al if del cargar");
			this.etapas = response.readEntity(new GenericType<List<DtEtapa>>() {});
			System.out.println(etapas);
		}else {
			
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar:", message));
		}
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public interfaces.IPlanVacunacionLocal getPv() {
		return pv;
	}

	public void setPv(interfaces.IPlanVacunacionLocal pv) {
		this.pv = pv;
	}
	
	
}
