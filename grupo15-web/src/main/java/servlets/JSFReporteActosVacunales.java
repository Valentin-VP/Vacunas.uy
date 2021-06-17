package servlets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

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



@Named("ReporteActosVacunales")
@RequestScoped
public class JSFReporteActosVacunales implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private List<DtPlanVacunacion> Lista = new ArrayList<DtPlanVacunacion>();
    private LineChartModel lineModel2;
   
    private BarChartModel barModel;
   
    private PieChartModel pieModel1;
    
    @PostConstruct
    public void init() {
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
        meses.set("enero", 25000);
        meses.set("febrero", 35000);
        meses.set("marzo", 44000);
        meses.set("abril", 50000);
        meses.set("mayo", 70000);
        meses.set("junio", 100000);
        meses.set("julio", 200000);
        meses.set("agosto", 250000);
        meses.set("septiembre", 270000);
        meses.set("octubre", 500000);
        meses.set("noviembre", 1000000);
        meses.set("diciembre", 1500000);
        

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
        edad.set("Hasta 17", 20000);
        edad.set("18-45", 10000);
        edad.set("45-60", 25000);
        edad.set("61+", 65000);


  
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
        yAxis.setMax(2000000);
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

        pieModel1.set("Masculino", 540);
        pieModel1.set("Femenino", 325);
     

        pieModel1.setTitle("Actos Vacunales por sexo");
        pieModel1.setLegendPosition("w");
        pieModel1.setShadow(false);
    }

  

}