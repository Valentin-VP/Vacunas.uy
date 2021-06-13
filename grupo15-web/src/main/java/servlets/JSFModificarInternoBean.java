package servlets;

import java.io.Serializable;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.Cookie;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

@Named("ModificarInterno")
@SessionScoped
public class JSFModificarInternoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String email;
	private String departamento;
	private String barrio;
	private String direccion;
	
	private String token;

	public JSFModificarInternoBean() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@PostConstruct
	public void cargaInicial() {
		try {
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
	        if (cookie != null) {
	        	token = cookie.getValue();
	        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
	        }
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/usuario/interno/buscar");
			Invocation invocation = webTarget.request(MediaType.APPLICATION_JSON).cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				String json = response.readEntity(String.class);
				LOGGER.info("Leyendo json como string: " + json);
				JSONObject interno = new JSONObject(json);
				//Setear parametros iniciales
				LOGGER.info("Leyendo respuesta: " + interno.toString());
				this.setEmail(interno.getString("email"));
				this.setBarrio(interno.getString("barrio"));
				this.setDepartamento(interno.getString("departamento"));
				this.setDireccion(interno.getString("direccion"));
				LOGGER.info("Se han seteado los parametros");
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
	}
	
	public void modificarInterno() {
		try {
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
	        if (cookie != null) {
	        	token = cookie.getValue();
	        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
	        }
	        JSONObject datos = new JSONObject();
	        JSONObject direccion = new JSONObject();
	        direccion.put("direccion", this.getDireccion());
	        direccion.put("barrio", this.getBarrio());
	        direccion.put("departamento", this.getDepartamento());
	        datos.put("direccion", direccion);
	        datos.put("email", this.getEmail());
	        // http://omnifaces-fans.blogspot.com/2015/10/jax-rs-consume-restful-web-service-from.html
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/usuario/interno/modificar");
			LOGGER.severe("Conectando a : " + webTarget.getUri());
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(datos.toString(), MediaType.APPLICATION_JSON));
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 201) {
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
			cargaInicial();
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
		}
	}
}
