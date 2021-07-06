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
import javax.json.JsonException;
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

import datatypes.DtTransportista;
import datatypes.DtVacuna;
import datatypes.DtVacunatorio;

@Named("AltaLoteDosis")
@RequestScoped
public class JSFAltaLoteDosisBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private String token;
	
	private Integer identificador;
	private List<String> vacunas = new ArrayList<String>();
	private List<DtVacuna> dtVacunas = new ArrayList<DtVacuna>();
	private String vacuna;
	private List<String> vacunatorios = new ArrayList<String>();
	private List<DtVacunatorio> dtVacunatorios = new ArrayList<DtVacunatorio>();
	private String vacunatorio;
	private String transportista;
	private List<String> transportistas = new ArrayList<String>();
	private List<DtTransportista> dtTransportistas = new ArrayList<DtTransportista>();
	private Integer cantTotal;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Integer identificador) {
		this.identificador = identificador;
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

	public String getTransportista() {
		return transportista;
	}

	public void setTransportista(String transportista) {
		this.transportista = transportista;
	}

	public List<String> getTransportistas() {
		return transportistas;
	}

	public void setTransportistas(List<String> transportistas) {
		this.transportistas = transportistas;
	}

	public List<DtTransportista> getDtTransportistas() {
		return dtTransportistas;
	}

	public void setDtTransportistas(List<DtTransportista> dtTransportistas) {
		this.dtTransportistas = dtTransportistas;
	}

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	public Integer getCantTotal() {
		return cantTotal;
	}

	public void setCantTotal(Integer cantTotal) {
		this.cantTotal = cantTotal;
	}

	public JSFAltaLoteDosisBean() {}

	@PostConstruct
	public void cargaInicial() {
		cargaVacunas();
		cargaVacunatorios();
		cargaTransportistas();
	}
	
	private void cargaVacunas() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunas/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.dtVacunas = response.readEntity(new GenericType<List<DtVacuna>>() {});
			this.vacunas.clear();
			for (DtVacuna dt : dtVacunas) {
				this.vacunas.add(dt.getNombre());
			}
		}else{
			String jsonString = response.readEntity(String.class);
			System.out.println(jsonString);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	private void cargaVacunatorios() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
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
	
	private void cargaTransportistas() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/transportistas/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.dtTransportistas = response.readEntity(new GenericType<List<DtTransportista>>() {});
			this.transportistas.clear();
			for (DtTransportista dt : dtTransportistas) {
				this.transportistas.add(String.valueOf(dt.getId()));
			}
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	public void altaLoteDosis() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		if (this.vacuna == null || this.vacuna.equals("") || this.vacunatorio == null || this.vacunatorio.equals("") || this.transportista == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Faltan datos."));
			return;
		}
		try {
			Integer.parseInt(this.transportista);
		}catch(NumberFormatException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Faltan datos."));
			return;
		}
		
		
		JSONObject lote = new JSONObject();
		
		for (DtVacuna each : this.getDtVacunas()) {
			if (each.getNombre().equals(this.getVacuna())) {
				try {
					lote.put("idVacuna", this.getVacuna());
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
					return;
				}
				break;
			}
		}

		for (DtVacunatorio each : this.getDtVacunatorios()) {
			if (each.getId().equals(this.getVacunatorio())) {
				try {
					lote.put("idVacunatorio", this.getVacunatorio());
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
					return;
				}
				break;
			}
		}
		
		for (DtTransportista each : this.getDtTransportistas()) {
			if (each.getId() == Integer.parseInt(this.getTransportista())) {
				System.out.println(this.getTransportista());
				try {
					lote.put("idTransportista", this.getTransportista());
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
					return;
				}
				break;
			}
		}
		
		try {
			lote.put("idLote", this.identificador);
			lote.put("cantidadTotal", String.valueOf(this.cantTotal));
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = "https://" + origRequest.getServerName();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/agregar");
			LOGGER.info("Conectando a : " + webTarget.getUri());
			LOGGER.info("Enviando JSON: " + lote.toString());
	
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(lote.toString(), MediaType.APPLICATION_JSON));
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 201) {
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
			LOGGER.severe(e.getStackTrace()[0].getMethodName() + ": " + "##### "+e.getMessage()+ " #####");
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
		} catch (JsonException e1) {
			LOGGER.severe(e1.getStackTrace()[0].getMethodName() + ": " + "###############!!!! "+e1.getMessage()+ " !!!!###############");
			LOGGER.severe("Ha ocurrido un error: " + e1.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e1.getMessage()));
		}
		cargaVacunas();
		cargaVacunatorios();
		cargaTransportistas();
	}
}

