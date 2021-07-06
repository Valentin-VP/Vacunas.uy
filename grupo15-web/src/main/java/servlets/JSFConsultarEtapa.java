package servlets;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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

import datatypes.DtEtapa;

@Named("etapa")
@RequestScoped
public class JSFConsultarEtapa {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token;
	private List<DtEtapa> etapas;
	private String etapa;
	private String fechaInicio;
	private String fechaFin;
	private String condicion;
	private String plan;
	private String vacuna;
	
	
	@PostConstruct
	public void cargaInicial() {
		System.out.println("Entro al carga inicial");
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
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/etapa/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.etapas = response.readEntity(new GenericType<List<DtEtapa>>() {});
		}
	}
	
	public void mostrarEtapa() {
		DtEtapa e = new DtEtapa();
		for(DtEtapa Dte: this.etapas) {
			if(this.etapa.equals(String.valueOf(Dte.getId())))
				e = Dte;
		}
		fechaInicio = e.getFechaInicio();
		fechaFin = e.getFechaFin();
		condicion = e.getFechaInicio();
		plan = String.valueOf(e.getPlanVac());
		 vacuna = String.valueOf(e.getPlanVac());
	}
	public JSFConsultarEtapa() {
		// TODO Auto-generated constructor stub
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<DtEtapa> getEtapas() {
		return etapas;
	}

	public void setEtapas(List<DtEtapa> etapas) {
		this.etapas = etapas;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getVacuna() {
		return vacuna;
	}

	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}

	
}
