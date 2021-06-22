package servlets;

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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtPuesto;
import datatypes.DtVacunatorio;

@Named("AgregarPuesto")
@RequestScoped
public class JSFAgregarPuestoBean {
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	private List<String> vacunatorios = new ArrayList<String>();
	private List<DtVacunatorio> dtVacunatorios = new ArrayList<DtVacunatorio>();
	private String vacunatorio;
	private List<String> dtPuestos = new ArrayList<String>();
	private String puesto;
	
	public JSFAgregarPuestoBean() {
		// TODO Auto-generated constructor stub
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getVacunatorios() {
		return vacunatorios;
	}

	public void setVacunatorios(List<String> vacunatorios) {
		this.vacunatorios = vacunatorios;
	}

	public List<DtVacunatorio> getDtVacunatorios() {
		return dtVacunatorios;
	}

	public void setDtVacunatorios(List<DtVacunatorio> dtVacunatorios) {
		this.dtVacunatorios = dtVacunatorios;
	}

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	public List<String> getDtPuestos() {
		return dtPuestos;
	}

	public void setDtPuestos(List<String> dtPuestos) {
		this.dtPuestos = dtPuestos;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	@PostConstruct
	public void cargaInicial() {
		cargaVacunatorios();
	}
	
	private void cargaVacunatorios() {
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
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunatorios/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.dtVacunatorios = response.readEntity(new GenericType<List<DtVacunatorio>>() {});
			this.vacunatorios.clear();
			for (DtVacunatorio dt : dtVacunatorios) {
				this.vacunatorios.add(dt.getId());
			}
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	public void cargaPuestos() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		if (this.vacunatorio == null) {
			return;
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunatorios/listarPuestos?idVacunatorio=" + this.vacunatorio);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.dtPuestos = response.readEntity(new GenericType<List<String>>() {});
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	public void agregarPuesto() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		if (this.vacunatorio == null || this.vacunatorio.equals("") || this.puesto == null || this.puesto.equals("") || this.puesto.contains("|")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Faltan datos."));
			return;
		}
		JSONObject puesto = new JSONObject();
		for (DtVacunatorio each : this.getDtVacunatorios()) {
			if (each.getId().equals(this.getVacunatorio())) {
				try {
					puesto.put("idVacunatorio", this.getVacunatorio());
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
					return;
				}
				break;
			}
		}
		try {
			puesto.put("idPuesto", this.puesto);
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunatorios/agregarPuesto");
			LOGGER.info("Conectando a : " + webTarget.getUri());
			LOGGER.info("Enviando JSON: " + puesto.toString());
	
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(puesto.toString(), MediaType.APPLICATION_JSON));
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 201 || response.getStatus() == 200) {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Crear:", message));
			}else {
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
