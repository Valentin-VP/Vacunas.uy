package servlets;

import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
	//private List<String> planes = new ArrayList<String>();
	private List<HashMap<String, String>> planes = new ArrayList<HashMap<String, String>>();
	private String plan;
	private List<String> vacunas = new ArrayList<String>();
	private List<DtVacuna> dtVacunas = new ArrayList<DtVacuna>();
	private String vacuna;
	private int edadMin;
	private int edadMax;
	private List<String> sectores = new ArrayList<String>();
	private String sector;
	private String enf; // convertir a booleano
	private String condicion;

	
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


//	public List<String> getPlanes() {
//		return planes;
//	}
//
//
//	public void setPlanes(List<String> planes) {
//		this.planes = planes;
//	}


	public List<HashMap<String, String>> getPlanes() {
		return planes;
	}


	public void setPlanes(List<HashMap<String, String>> planes) {
		this.planes = planes;
	}


	public String getPlan() {
		return plan;
	}


	public void setPlan(String plan) {
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


	public List<String> getSectores() {
		return sectores;
	}


	public void setSectores(List<String> sectores) {
		this.sectores = sectores;
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


	public String getCondicion() {
		return condicion;
	}


	public void setCondicion(String condicion) {
		this.condicion = condicion;
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
				List<DtPlanVacunacion> dtplanes = new ArrayList<>();
				try {
					dtplanes = response.readEntity(new GenericType<List<DtPlanVacunacion>>() {});
				} catch(Exception e) {
					LOGGER.info("Error!!!!!! : " + e.getStackTrace());
					LOGGER.info("Size del array: " + dtplanes.size());
				}
				LOGGER.info("Agregando al hashmap de planes");
				for (DtPlanVacunacion dtplan: dtplanes) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", "" + dtplan.getId());
					map.put("nombre", dtplan.getNombre());
					planes.add(map);
					LOGGER.info("Se ha agregado un plan a planes");
				}
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
			webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunas/listar");
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
			LOGGER.info("Por solicitar los planes en Bean...");
			origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
			conexion = ClientBuilder.newClient();
			webTarget = conexion.target(hostname + "/grupo15-services/rest/usuario/sectores");
			invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			LOGGER.info("Realizando Invoke...");
			response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				this.sectores = response.readEntity(new GenericType<List<String>>() {});
//				for (DtPlanVacunacion dt : dtPlanes) {
//					this.planes.add(Integer.toString(dt.getId()));
//				}
			}else {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", message));
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getStackTrace() + e.getMessage());
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
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");  
			String fechaInicio = dateFormat.format(this.getFechaInicio()); 
			String fechaFin = dateFormat.format(this.getFechaFin()); 
			System.out.println("Fecha del combo:" + this.getFechaInicio());		
			etapa.put("fechaInicio", fechaInicio); 
			etapa.put("fechaFin", fechaFin); 
			System.out.println("Fecha parseada en jsf:" + fechaInicio);
			//System.out.println("Fecha cambiada:" + fechaInicio);
			//String fechaFin = dateFormat.format(this.getFechaFin()); 

//			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|50|todos|si", 8, "vacuna1Virus1");
//			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "51|80|industria|si", 8, "vacuna1Virus1");
//			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|50|salud|si", 9, "vacuna1Virus1");
//			etapa.agregarEtapa(LocalDate.of(2021, 1, 1), LocalDate.of(2022, 1, 1), "18|500|industria|no", 10, "vacuna2Virus2");
			
			this.condicion = this.getEdadMin() + "|" + this.getEdadMax() + "|" + this.getSector() + "|" + this.getEnf().toLowerCase();
			etapa.put("plan", this.getPlan());
			etapa.put("vacuna", vacunaSeleccionada.getNombre());
			System.out.println("etapa" + etapa.toString());
			etapa.put("condicion", this.condicion);
			
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/etapa/agregar");
			LOGGER.info("Conectando a : " + webTarget.getUri());
			LOGGER.info("Enviando JSON: " + etapa.toString());
	
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(etapa.toString(), MediaType.APPLICATION_JSON));
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
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
