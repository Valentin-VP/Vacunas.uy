package servlets;

import java.io.StringReader;
import java.util.logging.Logger;

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
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@Named("BorrarInterno")
@RequestScoped
public class JSFBorrarInterno {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token;
	
	private String ci;
	

	public void borrarInterno() {
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
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/usuario/interno/eliminar?ci="+ci);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).build("POST");
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminar:", "Interno Eliminado con exito"));
			
		}
		else {
			String jsonString = response.readEntity(String.class);
			JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
			JsonObject reply = jsonReader.readObject();
			String message = reply.getString("message");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar:", message));
		}
	}
	
	public JSFBorrarInterno() {}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getCi() {
		return ci;
	}


	public void setCi(String ci) {
		this.ci = ci;
	}

	
}
