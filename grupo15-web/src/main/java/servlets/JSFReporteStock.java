package servlets;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
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
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import datatypes.DtEnfermedad;
import datatypes.DtLaboratorio;
import datatypes.DtPlanVacunacion;
import datatypes.DtStock;
import datatypes.DtVacuna;
import datatypes.DtVacunatorio;

@Named("ReporteStock")
@SessionScoped
public class JSFReporteStock implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token; 
	
	private Map<String,Map<String,String>> historico = new HashMap<String,Map<String,String>>();
	private Map<String,String> historicoMes = new HashMap<String,String>();
	private List<DtStock> actual;
    private LineChartModel lineModel2;
    private String enfermedad;
    private List<DtEnfermedad> enfermedades;
    private String vacuna;
    private List<DtVacuna> vacunas;
    private String periodo;
    private Map<String,String> periodos = new HashMap<String,String>();
    private String vacunatorio;
   	private List<DtVacunatorio> vacunatorios;
    
    private BarChartModel barModel;
   
    private PieChartModel pieModel1;
    
    private int maxValue = 1;
    @PostConstruct
    public void init() {
        createLineModels();
        this.periodos.put("Tres meses", "3");
        this.periodos.put("Seis meses", "6");
        this.periodos.put("Un año", "12");
        
        //cargo enfermedades
        Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/enfermedad/listar");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.enfermedades = response.readEntity(new GenericType<List<DtEnfermedad>>() {});
		}
		
		//cargo vacunatorios
		origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        hostname = "https://" + origRequest.getServerName();
		conexion = ClientBuilder.newClient();
		webTarget = conexion.target(hostname + "/grupo15-services/rest/vacunatorios/listar");
		invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			vacunatorios = response.readEntity(new GenericType<List<DtVacunatorio>>() {});
		}
    }
    
    public void cargarVacunas(){
    	Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/stock/enf/"+enfermedad);
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info(webTarget.getUri().toString());
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.vacunas = response.readEntity(new GenericType<List<DtVacuna>>() {});
		}
		createLineModels();
    }
    
    
    public void getStock() {
    	System.out.println(vacunatorio);
    	System.out.println(periodo);
    	JSONObject datos = new JSONObject();
    	
    	try {
    		datos.put("vacunatorio", vacunatorio);
			datos.put("enfermedad", enfermedad);
			datos.put("vacuna", vacuna);
			datos.put("periodo", periodo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String hostname = "https://" + origRequest.getServerName();
		Client conexion = ClientBuilder.newClient();
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/stock/actual");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(datos.toString(), MediaType.APPLICATION_JSON));
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.actual = response.readEntity(new GenericType<List<DtStock>>() {});
		}
		
		cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("x-access-token");
        if (cookie != null) {
        	token = cookie.getValue();
        	LOGGER.severe("Guardando cookie en Managed Bean: " + token);
        }
        origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        hostname = "https://" + origRequest.getServerName();
		conexion = ClientBuilder.newClient();
		webTarget = conexion.target(hostname + "/grupo15-services/rest/stock/historico");
		invocation = webTarget.request("application/json").cookie("x-access-token", token).buildPost(Entity.entity(datos.toString(), MediaType.APPLICATION_JSON));
		response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.historico = response.readEntity(new GenericType<Map<String,Map<String,String>>>() {});	
			createLineModels();
			System.out.println(historico);
		}
		
		
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    

    public LineChartModel getLineModel2() {
        return lineModel2;
    }


    private LineChartModel initCategoryModel() {
    	LineChartModel model = new LineChartModel();
    	int tempMax = 1;
        if(this.historico != null) {
        	ChartSeries meses = new ChartSeries();
            meses.setLabel("meses");  
            if(this.historico.get("1") != null) {
            	meses.set("Enero", Integer.valueOf(this.historico.get("1").get("cantDisp")));
            	tempMax = Integer.valueOf(this.historico.get("1").get("cantDisp"));
            }else {
            	meses.set("Enero", 0);
            }

            if(this.historico.get("2") != null) {
            	meses.set("Febrero", Integer.valueOf(this.historico.get("2").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("2").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("2").get("cantDisp"));
            	}
            }else {
            	meses.set("Febrero", 0);
            }

            if(this.historico.get("3") != null) {
            	meses.set("Marzo", Integer.valueOf(this.historico.get("3").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("3").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("3").get("cantDisp"));
            	}
            }else {
            	meses.set("Marzo", 0);
            }

            if(this.historico.get("4") != null) {
            	meses.set("Abril", Integer.valueOf(this.historico.get("4").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("4").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("4").get("cantDisp"));
            	}
            }else {
            	meses.set("Abril", 0);
            }

            if(this.historico.get("5") != null) {
            	meses.set("Mayo", Integer.valueOf(this.historico.get("5").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("5").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("5").get("cantDisp"));
            	}
            }else {
            	meses.set("Mayo", 0);
            }

            if(this.historico.get("6") != null) {
            	meses.set("Junio", Integer.valueOf(this.historico.get("6").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("6").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("6").get("cantDisp"));
            	}
            }else {
            	meses.set("Junio", 0);
            }

            if(this.historico.get("7") != null) {
            	meses.set("Julio", Integer.valueOf(this.historico.get("7").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("7").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("7").get("cantDisp"));
            	}
            }else {
            	meses.set("Julio", 0);
            }

            if(this.historico.get("8") != null) {
            	meses.set("Agosto", Integer.valueOf(this.historico.get("8").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("8").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("8").get("cantDisp"));
            	}
            }else {
            	meses.set("Agosto", 0);
            }

            if(this.historico.get("9") != null) {
            	meses.set("Septiembre", Integer.valueOf(this.historico.get("9").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("9").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("9").get("cantDisp"));
            	}
            }else {
            	meses.set("Septiembre", 0);
            }

            if(this.historico.get("10") != null) {
            	meses.set("Octumbre", Integer.valueOf(this.historico.get("10").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("10").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("10").get("cantDisp"));
            	}
            }else {
            	meses.set("Octubre", 0);
            }

            if(this.historico.get("11") != null) {
            	meses.set("Noviembre", Integer.valueOf(this.historico.get("11").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("11").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("11").get("cantDisp"));
            	}
            }else {
            	meses.set("Noviembre", 0);
            }

            if(this.historico.get("12") != null) {
            	meses.set("Diciembre", Integer.valueOf(this.historico.get("12").get("cantDisp")));
            	if (tempMax < Integer.valueOf(this.historico.get("12").get("cantDisp"))) {
            		tempMax = Integer.valueOf(this.historico.get("12").get("cantDisp"));
            	}
            }else {
            	meses.set("Diciembre", 0);
            }

            this.maxValue = tempMax;
            model.addSeries(meses);

            
        }
        return model;
    }

    private void createLineModels() {
      

        lineModel2 = initCategoryModel();
        lineModel2.setTitle("Evolución en el tiempo");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
        Axis yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Actos vacunales");
        yAxis.setMin(0);
        yAxis.setMax(this.maxValue);

     
    }

  
    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();

        LineChartSeries meses = new LineChartSeries();
        meses.setLabel("meses");

        meses.set("enero", 25000);
        meses.set("febrero", 60000);
        meses.set("marzo", 100000);
        meses.set("abril", 80000);
     


        return model;
    }


	public String getEnfermedad() {
		return enfermedad;
	}


	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}


	public String getVacuna() {
		return vacuna;
	}


	public void setVacuna(String vacuna) {
		this.vacuna = vacuna;
	}


	public String getPeriodo() {
		return periodo;
	}


	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}


	public BarChartModel getBarModel() {
		return barModel;
	}


	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}


	public PieChartModel getPieModel1() {
		return pieModel1;
	}


	public void setPieModel1(PieChartModel pieModel1) {
		this.pieModel1 = pieModel1;
	}


	public List<DtVacuna> getVacunas() {
		return vacunas;
	}


	public void setVacunas(List<DtVacuna> vacunas) {
		this.vacunas = vacunas;
	}


	public List<DtEnfermedad> getEnfermedades() {
		return enfermedades;
	}


	public void setEnfermedades(List<DtEnfermedad> enfermedades) {
		this.enfermedades = enfermedades;
	}


	public Map<String, String> getPeriodos() {
		return periodos;
	}


	public void setPeriodos(Map<String, String> periodos) {
		this.periodos = periodos;
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

	public Map<String,Map<String,String>> getHistorico() {
		return historico;
	}

	public void setHistorico(Map<String,Map<String,String>> historico) {
		this.historico = historico;
	}

	public List<DtStock> getActual() {
		return actual;
	}

	public void setActual(List<DtStock> actual) {
		this.actual = actual;
	}

	public Map<String,String> getHistoricoMes() {
		return historicoMes;
	}

	public void setHistoricoMes(Map<String,String> historicoMes) {
		this.historicoMes = historicoMes;
	}
   
}
  
