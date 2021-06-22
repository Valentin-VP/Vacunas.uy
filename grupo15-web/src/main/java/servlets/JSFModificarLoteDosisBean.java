package servlets;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
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

import datatypes.DtLoteDosis;
import datatypes.DtMensaje;
import datatypes.DtTransportista;
import datatypes.DtVacuna;
import datatypes.DtVacunatorio;

@Named("ModificarLoteDosis")
@SessionScoped
public class JSFModificarLoteDosisBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token;
	
	private List<String> lotes = new ArrayList<String>();
	private List<DtLoteDosis> dtLotes = new ArrayList<DtLoteDosis>();
	private String lote;
	
	private List<String> vacunas = new ArrayList<String>();
	private List<DtVacuna> dtVacunas = new ArrayList<DtVacuna>();
	private String vacuna;
	private List<String> vacunatorios = new ArrayList<String>();
	private List<DtVacunatorio> dtVacunatorios = new ArrayList<DtVacunatorio>();
	private String vacunatorio;
	
	private Integer cantEntregada;
	private Integer cantDescartada;
	private float temperatura;
	
	private String estado;
	private ArrayList<String> estados = new ArrayList<String>();
	
	private ArrayList<String> mensajes = new ArrayList<String>();
	private ArrayList<DtMensaje> dtMensajes = new ArrayList<DtMensaje>();

	public JSFModificarLoteDosisBean() {}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getLotes() {
		return lotes;
	}

	public void setLotes(List<String> lotes) {
		this.lotes = lotes;
	}

	public List<DtLoteDosis> getDtLotes() {
		return dtLotes;
	}

	public void setDtLotes(List<DtLoteDosis> dtLotes) {
		this.dtLotes = dtLotes;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
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

	public String getVacunatorio() {
		return vacunatorio;
	}

	public void setVacunatorio(String vacunatorio) {
		this.vacunatorio = vacunatorio;
	}

	public Integer getCantEntregada() {
		return cantEntregada;
	}

	public void setCantEntregada(Integer cantEntregada) {
		this.cantEntregada = cantEntregada;
	}

	public Integer getCantDescartada() {
		return cantDescartada;
	}

	public void setCantDescartada(Integer cantDescartada) {
		this.cantDescartada = cantDescartada;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<String> getEstados() {
		return estados;
	}

	public void setEstados(ArrayList<String> estados) {
		this.estados = estados;
	}

	public ArrayList<String> getMensajes() {
		return mensajes;
	}

	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}

	
	
	public ArrayList<DtMensaje> getDtMensajes() {
		return dtMensajes;
	}

	public void setDtMensajes(ArrayList<DtMensaje> dtMensajes) {
		this.dtMensajes = dtMensajes;
	}

	@PostConstruct
	public void cargaInicial() {
		cargaVacunas();
		cargaVacunatorios();
		cargaMensajes();
	}
	
	private void cargaVacunas() {
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
	
	public void cargaMensajes() {
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
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/listarMensajes");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.dtMensajes.clear();
			this.dtMensajes = response.readEntity(new GenericType<ArrayList<DtMensaje>>() {});
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	public void cargaLotes() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		if (this.vacuna == null || this.vacunatorio == null) {
			LOGGER.severe("AAAAAAAAAAAAAAAAAAAA");
			return;
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/listar?idVacuna=" + this.vacuna + "&idVacunatorio=" + this.vacunatorio);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.dtLotes = response.readEntity(new GenericType<List<DtLoteDosis>>() {});
			this.lotes.clear();
			for (DtLoteDosis dt : dtLotes) {
				this.lotes.add(String.valueOf(dt.getIdLote()));
			}
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	public void test() {
		LOGGER.info("Lote:" + this.lote + " Vacuna:" + this.vacuna + " Vacunatorio:" + this.vacunatorio + " estado:" + this.estado + " entr:" + this.cantEntregada + " desc:" + this.cantDescartada + " temp:" + this.temperatura);
	}
	
	public void modificarLoteDosis() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		LOGGER.info("Lote:" + this.lote + " Vacuna:" + this.vacuna + " Vacunatorio:" + this.vacunatorio + " estado:" + this.estado + " entr:" + this.cantEntregada + " desc:" + this.cantDescartada + " temp:" + this.temperatura);
		if (this.vacuna == null || this.vacuna.equals("") || this.vacunatorio == null || this.vacunatorio.equals("") || this.lote == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Faltan datos."));
			return;
		}
		try {
			Integer.parseInt(this.lote);
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
		
		for (DtLoteDosis each : this.getDtLotes()) {
			if (each.getIdLote() == each.getIdLote()) {
				try {
					lote.put("transportista", String.valueOf(each.getTransportista()));
					lote.put("cantidadTotal", String.valueOf(each.getCantidadTotal()));
				} catch (JSONException e) {
					LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
					return;
				}
				break;
			}
		}
		
		try {
			lote.put("idLote", this.lote);
			lote.put("cantidadEntregada", String.valueOf(this.cantEntregada));
			lote.put("cantidadDescartada", String.valueOf(this.cantDescartada));
			lote.put("estadoLote", this.estado);
			lote.put("temperatura", this.temperatura);
			LOGGER.info("Lote:" + this.lote + " Vacuna:" + this.vacuna + " Vacunatorio:" + this.vacunatorio + " estado:" + this.estado + " entr:" + this.cantEntregada + " desc:" + this.cantDescartada + " temp:" + this.temperatura);
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/modificar");
			LOGGER.info("Conectando a : " + webTarget.getUri());
			LOGGER.info("Enviando JSON: " + lote.toString());
	
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(lote.toString(), MediaType.APPLICATION_JSON));
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
		cargaVacunas();
		cargaVacunatorios();
		cargaMensajes();
		this.lotes.clear();
	}
	public void obtenerLogLoteEnSocio() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		if (this.vacuna == null || this.vacunatorio == null || this.lote == null) {
			return;
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/obtenerInfoLoteSocio?idLote=" + this.lote + "&idVacuna=" + this.vacuna + "&idVacunatorio=" + this.vacunatorio);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			DtMensaje m = response.readEntity(new GenericType<DtMensaje>() {});
			this.dtMensajes.add(m);
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	public void obtenerLogsLotesEnSocio() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		if (this.lote == null || this.dtLotes.isEmpty()) {
			return;
		}
		Integer idTransportista = null;
		for (DtLoteDosis dtl: this.dtLotes) {
			if (dtl.getIdLote().equals(Integer.parseInt(this.lote))) {
				idTransportista = dtl.getTransportista();
				break;
			}	
		}
		if (idTransportista==null) {
			return;
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/obtenerInfoTodosLotesSocio?idTransportista=" + idTransportista);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.dtMensajes.clear();
			this.dtMensajes = response.readEntity(new GenericType<ArrayList<DtMensaje>>() {});
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	
}
