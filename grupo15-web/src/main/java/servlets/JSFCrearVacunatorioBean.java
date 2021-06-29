package servlets;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Named("CrearVacunatorio")
@RequestScoped
public class JSFCrearVacunatorioBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token; 
	
	@EJB
	interfaces.IControladorVacunatorioLocal iControlador;
	
	
	private int idReglas;
	private Date horaApertura;
	private Date horaCierre;
	private int duracionTurno;
	
	private String url;
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIdReglas() {
		return idReglas;
	}

	public void setIdReglas(int idReglas) {
		this.idReglas = idReglas;
	}

	public Date getHoraApertura() {
		return horaApertura;
	}

	public void setHoraApertura(Date horaApertura) {
		this.horaApertura = horaApertura;
	}

	public Date getHoraCierre() {
		return horaCierre;
	}

	public void setHoraCierre(Date horaCierre) {
		this.horaCierre = horaCierre;
	}

	public int getDuracionTurno() {
		return duracionTurno;
	}

	public void setDuracionTurno(int duracionTurno) {
		this.duracionTurno = duracionTurno;
	}

	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

private String nombre;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
private String departamento;
	
	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	
private String barrio;
	
	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	
private String direccion;
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	
	
private int telefono;
	
	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	
	
private float longitud;
	
	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}
	
	
private float latitud;
	
	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}
	
	
	
	
	public JSFCrearVacunatorioBean() {}
	
	public void crearVacunatorio() throws IOException {
		Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()
				.get("x-access-token");
		if (cookie != null) {
			token = cookie.getValue();
			LOGGER.severe("Guardando cookie en Managed Bean: " + token);
		}
		// int id, String nombre, String descripcion, ArrayList<DtEtapa> etapa
		JSONObject vac = new JSONObject();
		try {
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");  
			String horaApertura = dateFormat.format(this.getHoraApertura()); 
			String horaCierre = dateFormat.format(this.getHoraCierre()); 
			
			
			vac.put("id",this.getId());
			vac.put("nombre", this.getNombre());
			vac.put("url", this.getUrl());
			vac.put("direccion", this.getDireccion());
			vac.put("barrio", this.getBarrio());
			vac.put("departamento", this.getDepartamento());
			vac.put("telefono", this.getTelefono());
			vac.put("latitud", this.getLatitud());
			vac.put("longitud", this.getLongitud());
			vac.put("idReglas", this.getIdReglas());
			vac.put("duracionTurno", this.getDuracionTurno());
			vac.put("horaApertura", horaApertura);
			vac.put("horaCierre", horaCierre);
			
			System.out.println(vac);
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
	        LOGGER.info("El server name es: " + hostname);
			Client conexion = ClientBuilder.newClient();
			WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunatorios/agregar");
			LOGGER.severe("Conectando a : " + webTarget.getUri());
			Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(vac.toString(), MediaType.APPLICATION_JSON));
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
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Crear:", message));
			}
			
		} catch (JSONException e) {
			LOGGER.severe("Ha ocurrido un error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", e.getMessage()));
		}
		
	}
	
}
