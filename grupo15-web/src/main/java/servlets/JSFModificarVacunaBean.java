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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import datatypes.DtEnfermedad;
import datatypes.DtLaboratorio;
import datatypes.DtVacuna;
import exceptions.EnfermedadInexistente;
import exceptions.LaboratorioInexistente;
import exceptions.VacunaInexistente;

@Named("ModificarVacuna")
@RequestScoped
public class JSFModificarVacunaBean {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token; 
	private String vacuna;
	private List<DtVacuna> vacunas = new ArrayList();
	private List<DtLaboratorio> laboratorios = new ArrayList();
	private List<DtEnfermedad> enfermedades = new ArrayList();
	private String nombre;
	private int expira;
	private int cantDosis;
	private int tiempoEntreDosis;
	private String laboratorio;
	private String enfermedad;

	@EJB
	interfaces.IControladorVacunaLocal iCV;
	@EJB 
	interfaces.ILaboratorioLocal iCL;
	@EJB
	interfaces.IEnfermedadLocal iCE;
	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVacuna() {
		return vacuna;
	}

	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}

	public List<DtVacuna> getVacunas() {
		return vacunas;
	}

	public void setVacunas(List<DtVacuna> vacunas) {
		this.vacunas = vacunas;
	}

	public List<DtLaboratorio> getLaboratorios() {
		return laboratorios;
	}

	public void setLaboratorios(List<DtLaboratorio> laboratorios) {
		this.laboratorios = laboratorios;
	}

	public List<DtEnfermedad> getEnfermedades() {
		return enfermedades;
	}

	public void setEnfermedades(List<DtEnfermedad> enfermedades) {
		this.enfermedades = enfermedades;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getExpira() {
		return expira;
	}

	public void setExpira(int expira) {
		this.expira = expira;
	}

	public int getCantDosis() {
		return cantDosis;
	}

	public void setCantDosis(int cantDosis) {
		this.cantDosis = cantDosis;
	}

	public int getTiempoEntreDosis() {
		return tiempoEntreDosis;
	}

	public void setTiempoEntreDosis(int tiempoEntreDosis) {
		this.tiempoEntreDosis = tiempoEntreDosis;
	}

	public String getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}

	public String getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}

	
	@PostConstruct
	public void cargaInicial() {
		try {
			vacunas = iCV.listarVacunas();
			laboratorios = iCL.listarLaboratorios();
			enfermedades = iCE.listarEnfermedades();
			
		} catch (VacunaInexistente | EnfermedadInexistente | LaboratorioInexistente e) {
			e.printStackTrace();
		}
	}
	
	public void modificarVacuna() {
		try {
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
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/enfermedad/agregar");
			JSONObject datos = new JSONObject();
			datos.put("nombre", vacuna);
			datos.put("cantDosis", String.valueOf(cantDosis));
			datos.put("expira", String.valueOf(expira));
			datos.put("tiempoEntreDosis", String.valueOf(tiempoEntreDosis));
			datos.put("laboratorio", laboratorio);
			datos.put("enfermedad", enfermedad);
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.json(datos));
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 201) {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Modficar:", message));
			
			}else {
				String jsonString = response.readEntity(String.class);
				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
				JsonObject reply = jsonReader.readObject();
				String message = reply.getString("message");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Modificar:", message));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
