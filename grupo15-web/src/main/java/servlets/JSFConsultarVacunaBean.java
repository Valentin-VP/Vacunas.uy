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


import datatypes.DtVacuna;

@Named("vacuna")
@RequestScoped
public class JSFConsultarVacunaBean {

	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token;
	private List<DtVacuna> vacunas;
	private String vacuna;
	private int cantDosis;
	private int expira;
	private int tiempoEntreDosis;
	private String enfermedad;
	private String laboratorio;
	
	
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
        String hostname = origRequest.getScheme() + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort();
        LOGGER.info("El server name es: " + hostname);
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunas/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.vacunas = response.readEntity(new GenericType<List<DtVacuna>>() {});
		}
	}
	
	public void mostrarVacuna() {
		DtVacuna e = new DtVacuna();
		for(DtVacuna Dte: this.vacunas) {
			if(this.vacuna.equals(String.valueOf(Dte.getNombre())))
				e = Dte;
		}
		vacuna = e.getNombre();
		cantDosis = e.getCantDosis();
		expira = e.getExpira();
		tiempoEntreDosis = e.getTiempoEntreDosis();
		enfermedad = String.valueOf(e.getDtEnf().getNombre());
		laboratorio = String.valueOf(e.getDtLab().getNombre());
		
	}
	public JSFConsultarVacunaBean() {
		// TODO Auto-generated constructor stub
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}

	public String getLaboratorio() {
		return laboratorio;
	}

	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}

	

	
}
