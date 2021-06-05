package servlets;

import java.io.Serializable;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datatypes.DtDatosVacuna;
import datatypes.DtEnfermedad;
import datatypes.DtLaboratorio;

@Named("CrearVacuna")
@RequestScoped
public class JSFCrearVacunaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	
	private String token; 
	private String nombre;
	private int cantDosis;
	private int expira;
	private int tiempoEntreDosis;
	private List<String> enfermedades = new ArrayList<String>();
	private List<DtEnfermedad> dtenfermedades = new ArrayList<DtEnfermedad>();
	private String enfermedad;
	private List<String> laboratorios = new ArrayList<String>();
	private List<DtLaboratorio> dtlaboratorios = new ArrayList<DtLaboratorio>();
	private String laboratorio;
	
	@EJB
	interfaces.IControladorVacunaLocal iControlador;

	public JSFCrearVacunaBean() {}

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

	public int getCantDosis() {
		return cantDosis;
	}

	public void setCantDosis(int cantDosis) {
		this.cantDosis = cantDosis;
	}

	public int getExpira() {
		return expira;
	}

	public void setExpira(int expira) {
		this.expira = expira;
	}

	public int getTiempoEntreDosis() {
		return tiempoEntreDosis;
	}

	public void setTiempoEntreDosis(int tiempoEntreDosis) {
		this.tiempoEntreDosis = tiempoEntreDosis;
	}

	public List<String> getEnfermedades() {
		return enfermedades;
	}

	public void setEnfermedades(List<String> enfermedades) {
		this.enfermedades = enfermedades;
	}

	public List<DtEnfermedad> getDtenfermedades() {
		return dtenfermedades;
	}

	public void setDtenfermedades(List<DtEnfermedad> dtenfermedades) {
		this.dtenfermedades = dtenfermedades;
	}

	public String getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}

	public List<String> getLaboratorios() {
		return laboratorios;
	}

	public void setLaboratorios(List<String> laboratorios) {
		this.laboratorios = laboratorios;
	}

	public List<DtLaboratorio> getDtlaboratorios() {
		return dtlaboratorios;
	}

	public void setDtlaboratorios(List<DtLaboratorio> dtlaboratorios) {
		this.dtlaboratorios = dtlaboratorios;
	}

	public String getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
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
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/enfermedad/listar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				dtenfermedades = response.readEntity(new GenericType<List<DtEnfermedad>>() {});
				for(DtEnfermedad dt: dtenfermedades) {
					this.enfermedades.add(dt.getNombre());
				}
			}
			conexion = ClientBuilder.newClient();
			webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/lab/listar");
			invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				dtlaboratorios = response.readEntity(new GenericType<List<DtLaboratorio>>() {});
				for(DtLaboratorio dt: dtlaboratorios) {
					this.laboratorios.add(dt.getNombre());
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
	}
	
	public void crearVacuna() {
		try {
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
	        if (cookie != null) {
	        	token = cookie.getValue();
	        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
	        }
	        DtDatosVacuna vacuna = new DtDatosVacuna(this.getNombre(), Integer.toString(this.getCantDosis()), Integer.toString(this.getExpira()), Integer.toString(this.getTiempoEntreDosis()), this.getLaboratorio(), this.getEnfermedad());
//	        JSONObject vacuna = new JSONObject();
//	        vacuna.put("nombre", this.getNombre());
//	        vacuna.put("cantDosis", this.getCantDosis());
//	        vacuna.put("tiempoEntreDosis", this.getTiempoEntreDosis());
//	        vacuna.put("expira", this.getExpira());
//	        vacuna.put("laboratorio", this.getLaboratorio());
//	        vacuna.put("enfermedad", this.getEnfermedad());
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target("http://localhost:8080/grupo15-services/rest/vacunas/agregar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(vacuna, MediaType.APPLICATION_JSON));
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 201) {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Crear:", message));
				
			}
			else {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", message));
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage()));
		}
	}
	
}
