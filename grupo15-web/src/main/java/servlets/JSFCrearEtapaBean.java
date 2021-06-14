package servlets;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import datatypes.DtPlanVacunacion;
import datatypes.DtVacuna;

@Named("CrearEtapa")
@RequestScoped
public class JSFCrearEtapaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	private String nombre;
	private Date fechaInicio;
	private Date fechaFin;
	private String condicion;
	//private List<String> planes = new ArrayList<String>();
	private List<DtPlanVacunacion> planes = new ArrayList<DtPlanVacunacion>();
	private DtPlanVacunacion plan;
	private List<String> vacunas = new ArrayList<String>();
	private List<DtVacuna> dtVacunas = new ArrayList<DtVacuna>();
	private String vacuna;

	
	public JSFCrearEtapaBean() {}


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


	public Date getFechaInicio() {
		return fechaInicio;
	}


	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	public Date getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}


	public String getCondicion() {
		return condicion;
	}


	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}


//	public List<String> getPlanes() {
//		return planes;
//	}
//
//
//	public void setPlanes(List<String> planes) {
//		this.planes = planes;
//	}


	public List<DtPlanVacunacion> getPlanes() {
		return planes;
	}


	public void setPlanes(List<DtPlanVacunacion> planes) {
		this.planes = planes;
	}


	public DtPlanVacunacion getPlan() {
		return plan;
	}


	public void setPlan(DtPlanVacunacion plan) {
		this.plan = plan;
	}


	public List<String> getVacunas() {
		return vacunas;
	}


	public void setVacunas(List<String> vacunas) {
		this.vacunas = vacunas;
	}


	public List<DtVacuna> getDtVacunas() {
		return dtVacunas;
	}


	public void setDtVacunas(List<DtVacuna> dtVacunas) {
		this.dtVacunas = dtVacunas;
	}


	public String getVacuna() {
		return vacuna;
	}


	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
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
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/plan/listar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				this.planes = response.readEntity(new GenericType<List<DtPlanVacunacion>>() {});
//				for (DtPlanVacunacion dt : dtPlanes) {
//					this.planes.add(Integer.toString(dt.getId()));
//				}
			}else {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Crear:", message));
			}
			conexion = ClientBuilder.newClient();
			webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/vacunas/listar");
			invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				dtVacunas = response.readEntity(new GenericType<List<DtVacuna>>() {
				});
				for (DtVacuna dt : dtVacunas) {
					this.vacunas.add(dt.getNombre());
				}
			}else {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Crear:", message));
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
	}
	
	public void crearEtapa() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		// int id, String nombre, String descripcion, ArrayList<DtEtapa> etapa
		JSONObject etapa = new JSONObject();
		DtVacuna vacunaSeleccionada = null; 
		for (DtVacuna each : this.getDtVacunas()) {
			if (each.getNombre().equals(this.getVacuna())) {
				vacunaSeleccionada = each;
				try {
					etapa.put("vacuna", vacunaSeleccionada);
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
				}
				break;
			}
		}
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
			String fechaInicio = dateFormat.format(this.getFechaInicio()); 
			etapa.put("fechaInicio", fechaInicio); 
			String fechaFin = dateFormat.format(this.getFechaFin()); 
			etapa.put("fechaFin", fechaFin);
			etapa.put("condicion", this.getCondicion());
			etapa.put("plan", this.getPlan().getId());
			etapa.put("vacuna", vacunaSeleccionada.getNombre());
			
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/etapa/agregar");
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
