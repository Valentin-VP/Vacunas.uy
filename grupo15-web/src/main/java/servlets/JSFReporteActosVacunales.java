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
    
    @PostConstruct
    public void init() {
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
        ChartSeries meses = new ChartSeries();
        meses.setLabel("meses");
        meses.set("enero", Integer.valueOf(this.vacunadosMes.get("1")));
        meses.set("febrero", Integer.valueOf(this.vacunadosMes.get("2")));
        meses.set("marzo", Integer.valueOf(this.vacunadosMes.get("3")));
        meses.set("abril", Integer.valueOf(this.vacunadosMes.get("4")));
        meses.set("mayo", Integer.valueOf(this.vacunadosMes.get("5")));
        meses.set("junio", Integer.valueOf(this.vacunadosMes.get("6")));
        meses.set("julio", Integer.valueOf(this.vacunadosMes.get("7")));
        meses.set("agosto", Integer.valueOf(this.vacunadosMes.get("8")));
        meses.set("septiembre", Integer.valueOf(this.vacunadosMes.get("9")));
        meses.set("octubre", Integer.valueOf(this.vacunadosMes.get("10")));
        meses.set("noviembre", Integer.valueOf(this.vacunadosMes.get("11")));
        meses.set("diciembre", Integer.valueOf(this.vacunadosMes.get("12")));
        

        model.addSeries(meses);

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
        yAxis.setMax(3000000);

     
    }

  

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries edad = new ChartSeries();
        edad.setLabel("Edad");
        edad.set("Hasta 17", Integer.valueOf(this.vacunadosEdad.get("adolescentes")));
        edad.set("18-45", Integer.valueOf(this.vacunadosEdad.get("jovenes")));
        edad.set("45-60", Integer.valueOf(this.vacunadosEdad.get("adultos")));
        edad.set("61+", Integer.valueOf(this.vacunadosEdad.get("mayores")));


  
        model.addSeries(edad);


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
        yAxis.setMax(200);
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

        pieModel1.set("Masculino", Integer.valueOf(this.vacunadosSexo.get("masculino")));
        pieModel1.set("Femenino", Integer.valueOf(this.vacunadosSexo.get("femenino")));
        pieModel1.set("otro", Integer.valueOf(this.vacunadosSexo.get("otro")));
     

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