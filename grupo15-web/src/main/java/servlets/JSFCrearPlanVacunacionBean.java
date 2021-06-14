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
import datatypes.DtEtapa;

@Named("CrearPlan")
@RequestScoped
public class JSFCrearPlanVacunacionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	private String nombre;
	private String descripcion;
	private List<String> enfermedades = new ArrayList<String>();
	private List<DtEnfermedad> dtEnfermedades = new ArrayList<DtEnfermedad>();
	private String enfermedad;
	private List<String> etapas = new ArrayList<String>();
	private List<DtEtapa> dtEtapas = new ArrayList<DtEtapa>();
	private String etapa;

	public JSFCrearPlanVacunacionBean() {}

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

	public List<String> getEnfermedades() {
		return enfermedades;
	}

	public void setEnfermedades(List<String> enfermedades) {
		this.enfermedades = enfermedades;
	}

	public List<DtEnfermedad> getDtEnfermedades() {
		return dtEnfermedades;
	}

	public void setDtEnfermedades(List<DtEnfermedad> dtEnfermedades) {
		this.dtEnfermedades = dtEnfermedades;
	}

	public String getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}

	public List<String> getEtapas() {
		return etapas;
	}

	public void setEtapas(List<String> etapas) {
		this.etapas = etapas;
	}

	public List<DtEtapa> getDtEtapas() {
		return dtEtapas;
	}

	public void setDtEtapas(List<DtEtapa> dtEtapas) {
		this.dtEtapas = dtEtapas;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
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
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
					.get("x-access-token");
			if (cookie != null) {
				token = cookie.getValue();
				LOGGER.severe("Guardando cookie en Managed Bean: " + token);
			}
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/enfermedad/listar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				dtEnfermedades = response.readEntity(new GenericType<List<DtEnfermedad>>() {
				});
				for (DtEnfermedad dt : dtEnfermedades) {
					this.enfermedades.add(dt.getNombre());
				}
			}
			conexion = ClientBuilder.newClient();
			webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/lab/listar");
			invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				dtEtapas = response.readEntity(new GenericType<List<DtEtapa>>() {
				});
				for (DtEtapa dt : dtEtapas) {
					this.etapas.add(Integer.toString(dt.getId()));
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
	}

	public void crearPlan() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		// int id, String nombre, String descripcion, ArrayList<DtEtapa> etapa
		JSONObject plan = new JSONObject();
		for (DtEtapa each : this.getDtEtapas()) {
			if (Integer.toString(each.getId()).equals(this.getEtapa())) {
				DtEtapa etapaSeleccionada = each;
				try {
					plan.put("etapa", etapaSeleccionada);
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
				}
				break;
			}
		}
		try {
			plan.put("nombre", this.getNombre());
			plan.put("descripcion", this.getDescripcion());
			
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/plan/agregar");
			LOGGER.severe("Conectando a : " + webTarget.getUri());
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(plan.toString(), MediaType.APPLICATION_JSON));
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
		} catch (JSONException e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
		}
		
	}
}
