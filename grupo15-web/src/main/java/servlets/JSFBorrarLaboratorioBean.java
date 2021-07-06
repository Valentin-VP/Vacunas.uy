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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
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

import datatypes.DtEnfermedad;
import datatypes.DtLaboratorio;
import exceptions.AccionInvalida;
import exceptions.EnfermedadInexistente;

@Named("BorrarLaboratorio")
@RequestScoped
public class JSFBorrarLaboratorioBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token; 
	
	@EJB
	interfaces.ILaboratorioLocal iControlador;
	
	private String nombre;
	private List<String> laboratorios = new ArrayList<String>();
	private List<DtLaboratorio> dtlaboratorios = new ArrayList<DtLaboratorio>();

	public JSFBorrarLaboratorioBean() {}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	

	public List<String> getLaboratorios() {
		return laboratorios;
	}

	public void setLaboratorios(List<String> laboratorios) {
		this.laboratorios = laboratorios;
	}

	@PostConstruct
	public void cargaInicial() {
		try {
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
	        if (cookie != null) {
	        	token = cookie.getValue();
	        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
	        }
	        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = "https://" + origRequest.getServerName();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lab/listar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				
				dtlaboratorios = response.readEntity(new GenericType<List<DtLaboratorio>>() {});
				for(DtLaboratorio dt: dtlaboratorios) {
					this.laboratorios.add(dt.getNombre());
				}
//				String jsonString = response.readEntity(String.class);
//				JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
//				JsonArray jsonArray = jsonReader.readArray();
//				int i = 0;
//				while(i < jsonArray.size()) {
//					JsonObject each = jsonArray.getJsonObject(i);
//					String enfermedad = each.getString("nombre");
//				}
//				JsonObject reply = jsonReader.readObject();
//				String message = reply.getString("message");
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
		
	}
	
	public void borrarLaboratorio() {
		try {
			Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
	        if (cookie != null) {
	        	token = cookie.getValue();
	        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
	        }
	        // http://omnifaces-fans.blogspot.com/2015/10/jax-rs-consume-restful-web-service-from.html
	        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = "https://" + origRequest.getServerName();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/lab/eliminar").queryParam("lab", this.nombre);
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildDelete();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 201) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Borrar:", "Laboratorio eliminado"));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "" + response.getStatus()));
			}
		//	cargaInicial();
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}

	}
}
