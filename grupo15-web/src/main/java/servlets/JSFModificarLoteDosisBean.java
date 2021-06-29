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
@RequestScoped
public class JSFModificarLoteDosisBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token;
	
	private List<DtLoteDosis> lotes;
	private String lote;
	private List<DtVacuna> vacunas;
	private String vacuna;
	private List<DtVacunatorio> vacunatorios;
	private String vacunatorio;
	
	private Integer cantEntregada;
	private Integer cantDescartada;
	private float temperatura;
	
	private String estado;
	private ArrayList<String> estados = new ArrayList<String>();
	
	private ArrayList<String> mensajes = new ArrayList<String>();
	private ArrayList<DtMensaje> dtMensajes = new ArrayList<DtMensaje>();

	public JSFModificarLoteDosisBean() {}


	@PostConstruct
	public void cargaInicial() {
		//cargaVacunas();
		//cargaVacunatorios();
		cargaLotes();
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
			this.vacunas = response.readEntity(new GenericType<List<DtVacuna>>() {});
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
			this.vacunatorios = response.readEntity(new GenericType<List<DtVacunatorio>>() {});
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
			this.mensajes.clear();
			this.dtMensajes = response.readEntity(new GenericType<ArrayList<DtMensaje>>() {});
			for (DtMensaje dt: this.dtMensajes) {
				System.out.println(dt.getContenido());
				this.mensajes.add(dt.getContenido());
			}
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	public void cargaLotes() {
		System.out.println("Entro al cargaLotes");
		//System.out.println(vacuna);
		//System.out.println(vacunatorio);
		//if (this.vacuna == null || this.vacunatorio == null) {
		//	LOGGER.severe("Esperando que se guarde vacuna o vacunatorio");
		//	return;
		//}
		
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
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/listar");//?idVacuna=" + this.vacuna + "&idVacunatorio=" + this.vacunatorio);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			System.out.println("entro al if de 200 status");
			this.lotes = response.readEntity(new GenericType<List<DtLoteDosis>>() {});
		}else{
			System.out.println("entro al error");
			String jsonString = response.readEntity(String.class);
			System.out.println(jsonString);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	
	
	public void modificarLoteDosis() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		LOGGER.info("Lote:" + this.lote + " Vacuna:" + this.vacuna + " Vacunatorio:" + this.vacunatorio + " estado:" + this.estado + " entr:" + this.cantEntregada + " desc:" + this.cantDescartada + " temp:" + this.temperatura);
		if (this.lote == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Faltan datos."));
			return;
		}
		//try {
		//	Integer.parseInt(this.lote);
		//}catch(NumberFormatException e) {
		//	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Faltan datos."));
		//	return;
		//}
		
		
		
		JSONObject lote = new JSONObject();
		try {
			String[] temp;
			DtLoteDosis dt = new DtLoteDosis();
			temp = this.lote.split("\\Q|\\E");
			for (DtLoteDosis d: lotes) {
				if (d.getIdLote().equals(Integer.valueOf(temp[0])) && d.getIdVacunatorio().equals(temp[1]) && d.getIdVacuna().equals(temp[2])) {
					System.out.println("ASKJDnASKJDjASFDASDASDASDa " + String.valueOf(d.getTransportista()) + String.valueOf(d.getCantidadTotal()));
					lote.put("idLote", String.valueOf(d.getIdLote()));
					lote.put("idVacunatorio", d.getIdVacunatorio());
					lote.put("idVacuna", d.getIdVacuna());
					lote.put("transportista", String.valueOf(d.getTransportista()));
					lote.put("cantidadTotal", String.valueOf(d.getCantidadTotal()));
					break;
				}
			}
			//lote.put("idVacunatorio", this.vacunatorio);
			//lote.put("idVacuna", this.vacuna);
			lote.put("cantidadEntregada", String.valueOf(this.cantEntregada));
			lote.put("cantidadDescartada", String.valueOf(this.cantDescartada));
			lote.put("estadoLote", this.estado);
			lote.put("temperatura", this.temperatura);
			//LOGGER.info("Lote:" + this.lote + " Vacuna:" + this.vacuna + " Vacunatorio:" + this.vacunatorio + " estado:" + this.estado + " entr:" + this.cantEntregada + " desc:" + this.cantDescartada + " temp:" + this.temperatura);
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
		cargaLotes();
		cargaMensajes();
	}
	public void obtenerLogLoteEnSocio() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		if (this.lote == null) {
			return;
		}
		String[] temp;
		temp = this.lote.split("\\Q|\\E");
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/obtenerInfoLoteSocio?idLote=" + temp[0] + "&idVacuna=" + temp[2] + "&idVacunatorio=" + temp[1]);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			DtMensaje m = response.readEntity(new GenericType<DtMensaje>() {});
			this.dtMensajes.add(m);
			this.mensajes.clear();
			for (DtMensaje dt: this.dtMensajes) {
				this.mensajes.add(dt.getContenido());
			}
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}
	public void obtenerLogsLotesEnSocio() {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
		String[] temp;
		DtLoteDosis dt = new DtLoteDosis();
		temp = this.lote.split("\\Q|\\E");
		for (DtLoteDosis d: lotes) {
			if (d.getIdLote().equals(Integer.valueOf(temp[0])) && d.getIdVacunatorio().equals(temp[1]) && d.getIdVacuna().equals(temp[2])) {
				System.out.println("ASKJDnASKJDjASFDASDASDASDa " + String.valueOf(d.getTransportista()) + String.valueOf(d.getCantidadTotal()));
				dt = d;
				break;
			}
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lotedosis/obtenerInfoTodosLotesSocio?idTransportista=" + dt.getTransportista());
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.mensajes.clear();
			this.dtMensajes = response.readEntity(new GenericType<ArrayList<DtMensaje>>() {});
			for (DtMensaje d: this.dtMensajes) {
				this.mensajes.add(d.getContenido());
			}
		}else{
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Listar:", message));
		}
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public List<DtLoteDosis> getLotes() {
		return lotes;
	}


	public void setLotes(List<DtLoteDosis> lotes) {
		this.lotes = lotes;
	}


	public String getLote() {
		return lote;
	}


	public void setLote(String lote) {
		this.lote = lote;
	}


	public List<DtVacuna> getVacunas() {
		return vacunas;
	}


	public void setVacunas(List<DtVacuna> vacunas) {
		this.vacunas = vacunas;
	}


	public String getVacuna() {
		return vacuna;
	}


	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}


	public List<DtVacunatorio> getVacunatorios() {
		return vacunatorios;
	}


	public void setVacunatorios(List<DtVacunatorio> vacunatorios) {
		this.vacunatorios = vacunatorios;
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
	
	
}
