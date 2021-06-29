package servlets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import datatypes.DtEnfermedad;
import datatypes.DtVacuna;

@Named("BorrarVacuna")
@RequestScoped
public class JSFBorrarVacunaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token; 
	
	private String nombre;
	private List<String> vacunas = new ArrayList<String>();
	private List<DtVacuna> dtvacunas = new ArrayList<DtVacuna>();
	
	@EJB
	interfaces.IControladorVacunaLocal iControlador;
	
	public JSFBorrarVacunaBean() {}

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

	public List<String> getVacunas() {
		return vacunas;
	}

	public void setVacunas(List<String> vacunas) {
		this.vacunas = vacunas;
	}

	public List<DtVacuna> getDtvacunas() {
		return dtvacunas;
	}

	public void setDtvacunas(List<DtVacuna> dtvacunas) {
		this.dtvacunas = dtvacunas;
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
	        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunas/listar");
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				dtvacunas = response.readEntity(new GenericType<List<DtVacuna>>() {});
				for(DtVacuna dt: dtvacunas) {
					this.vacunas.add(dt.getNombre());
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}
		
	}
	
	public void borrarVacuna() {
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
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunas/eliminar").queryParam("vac", this.nombre);
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildDelete();
			Response response = invocation.invoke();
			LOGGER.info("Respuesta: " + response.getStatus());
			if (response.getStatus() == 200) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Borrar:", "Vacuna eliminada"));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "" + response.getStatus()));
			}
			cargaInicial();
		} catch (Exception e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
		}

	}
}
