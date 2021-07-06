package servlets;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
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

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;

import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;

import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import org.primefaces.model.chart.PieChartModel;

import datatypes.DtPlanVacunacion;
import net.bytebuddy.asm.Advice.This;

import java.io.FileOutputStream;
import java.io.StringReader;


import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.*;

@Named("ReporteActosVacunales")
@RequestScoped
public class JSFReporteActosVacunales implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());
	private String token;

	private Map<String,String> vacunadosMes;
	private Map<String,String> vacunadosEdad;
	private Map<String,String> vacunadosSexo;
	private List<DtPlanVacunacion> Lista = new ArrayList<DtPlanVacunacion>();
    private LineChartModel lineModel2;
   
    private BarChartModel barModel;
   
    private PieChartModel pieModel1;
    
    private int maxValueBar = 1;
    private int maxValueLine = 1;
    
    @PostConstruct
    public void init() {
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
		//pido vacunados por mes
		WebTarget webTarget = conexion.target(hostname + "/grupo15-services/rest/reportes/reporte1");
		Invocation invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		Response response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.vacunadosMes = response.readEntity(new GenericType<Map<String,String>>() {});
			System.out.println(vacunadosMes);
		}
		
		//pido vacunados por edad
		webTarget = conexion.target(hostname + "/grupo15-services/rest/reportes/reporte2");
		invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.vacunadosEdad = response.readEntity(new GenericType<Map<String,String>>() {});
			System.out.println(vacunadosEdad);
		}
		
		webTarget = conexion.target(hostname + "/grupo15-services/rest/reportes/reporte3");
		invocation = webTarget.request("application/json").cookie("x-access-token", token).buildGet();
		response = invocation.invoke();
		LOGGER.info("Respuesta: " + response.getStatus());
		if (response.getStatus() == 200) {
			this.vacunadosSexo = response.readEntity(new GenericType<Map<String,String>>() {});
			System.out.println(vacunadosSexo);
		}
		
		createLineModels();
        createPieModels();
        createBarModels();
    }
    
  
 
    public List<DtPlanVacunacion> getLista() {
		return Lista;
	}

	public void setLista(List<DtPlanVacunacion> dtPlanes) {
		this.Lista = dtPlanes;
	}

    

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

   
    public PieChartModel getPieModel1() {
        return pieModel1;
    }

   

    public BarChartModel getBarModel() {
        return barModel;
    }

  

  
    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();
        int tempMax = 1;
        
        if (this.vacunadosMes!=null) {
	        ChartSeries meses = new ChartSeries();
	        meses.setLabel("meses");
	        if (this.vacunadosMes.get("1")!=null) {
	        	meses.set("enero", Integer.valueOf(this.vacunadosMes.get("1")));
	        	tempMax = Integer.valueOf(this.vacunadosMes.get("1"));
	        }else {
	        	meses.set("enero", 0);
	        }
	        if (this.vacunadosMes.get("2")!=null) {
	        	meses.set("febrero", Integer.valueOf(this.vacunadosMes.get("2")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("2")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("2"));
	        }else {
	        	meses.set("febrero", 0);
	        }
	        if (this.vacunadosMes.get("3")!=null) {
	        	meses.set("marzo", Integer.valueOf(this.vacunadosMes.get("3")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("3")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("3"));
	        }else {
	        	meses.set("marzo", 0);
	        }
	        if (this.vacunadosMes.get("4")!=null) {
	        	meses.set("abril", Integer.valueOf(this.vacunadosMes.get("4")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("4")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("4"));
	        }else {
	        	meses.set("abril", 0);
	        }
	        if (this.vacunadosMes.get("5")!=null) {
	        	meses.set("mayo", Integer.valueOf(this.vacunadosMes.get("5")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("5")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("5"));
	        }else {
	        	meses.set("mayo", 0);
	        }
	        if (this.vacunadosMes.get("6")!=null) {
	        	meses.set("junio", Integer.valueOf(this.vacunadosMes.get("6")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("6")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("6"));
	        }else {
	        	meses.set("junio", 0);
	        }
	        if (this.vacunadosMes.get("7")!=null) {
	        	meses.set("julio", Integer.valueOf(this.vacunadosMes.get("7")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("7")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("7"));
	        }else {
	        	meses.set("julio", 0);
	        }
	        if (this.vacunadosMes.get("8")!=null) {
	        	meses.set("agosto", Integer.valueOf(this.vacunadosMes.get("8")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("8")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("8"));
	        }else {
	        	meses.set("agosto", 0);
	        }
	        if (this.vacunadosMes.get("9")!=null) {
	        	meses.set("septiembre", Integer.valueOf(this.vacunadosMes.get("9")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("9")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("9"));
	        }else {
	        	meses.set("septiembre", 0);
	        }
	        if (this.vacunadosMes.get("10")!=null) {
	        	meses.set("octubre", Integer.valueOf(this.vacunadosMes.get("10")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("10")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("10"));
	        }else {
	        	meses.set("octubre", 0);
	        }
	        if (this.vacunadosMes.get("11")!=null) {
	        	meses.set("noviembre", Integer.valueOf(this.vacunadosMes.get("11")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("11")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("11"));
	        }else {
	        	meses.set("noviembre", 0);
	        }
	        if (this.vacunadosMes.get("12")!=null) {
	        	meses.set("diciembre", Integer.valueOf(this.vacunadosMes.get("12")));
	        	if (tempMax < Integer.valueOf(this.vacunadosMes.get("12")))
	        		tempMax = Integer.valueOf(this.vacunadosMes.get("12"));
	        }else {
	        	meses.set("diciembre", 0);
	        }
	        
	        model.addSeries(meses);
        }
        
        

        this.maxValueLine = tempMax;

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
        yAxis.setMax(this.maxValueLine);

     
    }

  

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        int tempMax = 1;
        if (this.vacunadosEdad!=null) {
	        	ChartSeries edad = new ChartSeries();
	        edad.setLabel("Edad");
	        if (this.vacunadosEdad.get("adolescentes")!=null) {
	        	edad.set("Hasta 17", Integer.valueOf(this.vacunadosEdad.get("adolescentes")));
	        	tempMax = Integer.valueOf(this.vacunadosEdad.get("adolescentes"));
	        }else {
	        	edad.set("Hasta 17", 0);
	        }
	        if (this.vacunadosEdad.get("jovenes")!=null) {
	        	edad.set("18-45", Integer.valueOf(this.vacunadosEdad.get("jovenes")));
	        	if (tempMax<Integer.valueOf(this.vacunadosEdad.get("jovenes")))
	        		tempMax = Integer.valueOf(this.vacunadosEdad.get("jovenes"));
	        }else {
	        	edad.set("18-45", 0);
	        }
	        if (this.vacunadosEdad.get("adultos")!=null) {
	        	edad.set("45-60", Integer.valueOf(this.vacunadosEdad.get("adultos")));
	        	if (tempMax<Integer.valueOf(this.vacunadosEdad.get("adultos")))
	        		tempMax = Integer.valueOf(this.vacunadosEdad.get("adultos"));
	        }else {
	        	edad.set("45-60", 0);
	        }
	        if (this.vacunadosEdad.get("mayores")!=null) {
	        	edad.set("61+", Integer.valueOf(this.vacunadosEdad.get("mayores")));
	        	if (tempMax<Integer.valueOf(this.vacunadosEdad.get("mayores")))
	        		tempMax = Integer.valueOf(this.vacunadosEdad.get("mayores"));
	        }else {
	        	edad.set("61+", 0);
	        }
	
	        this.maxValueBar = tempMax;
	        model.addSeries(edad);
        }
        


        return model;
    }

    private void createBarModels() {
        createBarModel();
      
    }

    private void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Actos vacunales por edad");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Edad");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Actos vacunales");
        yAxis.setMin(0);
        yAxis.setMax(this.maxValueBar);
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

    private void createPieModels() {
        createPieModel1();
  
    }

    private void createPieModel1() {
        pieModel1 = new PieChartModel();

        if (this.vacunadosSexo!=null) {
        	pieModel1.set("Masculino", Integer.valueOf(this.vacunadosSexo.get("masculino")));
        	pieModel1.set("Femenino", Integer.valueOf(this.vacunadosSexo.get("femenino")));
        	pieModel1.set("otro", Integer.valueOf(this.vacunadosSexo.get("otro")));
        }else {
        	pieModel1.set("Masculino", 0);
        	pieModel1.set("Femenino", 0);
        	pieModel1.set("otro", 0);
        }
        
     

        pieModel1.setTitle("Actos Vacunales por sexo");
        pieModel1.setLegendPosition("w");
        pieModel1.setShadow(false);
    }



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public Map<String, String> getVacunadosMes() {
		return vacunadosMes;
	}



	public void setVacunadosMes(Map<String, String> vacunadosMes) {
		this.vacunadosMes = vacunadosMes;
	}



	public Map<String, String> getVacunadosEdad() {
		return vacunadosEdad;
	}



	public void setVacunadosEdad(Map<String, String> vacunadosEdad) {
		this.vacunadosEdad = vacunadosEdad;
	}



	public Map<String, String> getVacunadosSexo() {
		return vacunadosSexo;
	}



	public void setVacunadosSexo(Map<String, String> vacunadosSexo) {
		this.vacunadosSexo = vacunadosSexo;
	}

    

}