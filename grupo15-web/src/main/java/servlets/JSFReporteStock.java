package servlets;

import java.io.IOException;
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

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import datatypes.DtPlanVacunacion;

@Named("ReporteActosVacunales")
@RequestScoped
public class JSFReporteStock implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(getClass().getName());

	private List<DtPlanVacunacion> Lista = new ArrayList<DtPlanVacunacion>();
    private LineChartModel lineModel2;
   
    private BarChartModel barModel;
   
    private PieChartModel pieModel1;
    
    @PostConstruct
    public void init() {
        createLineModels();
      
    
     
    }
    
    public void pdf() throws IOException{

    		final String DEST = "C:/temp/reporte.pdf";
    	    PdfDocument pdf = new PdfDocument(new PdfWriter(DEST));
    	    Document document = new Document(pdf);
    	    String line = "Hello! Welcome to iTextPdf";
    	    document.add(new Paragraph(line));
    	    document.close();

    	    System.out.println("Awesome PDF just got created.");
    	    
    	  
    	  
    	}
 
    public List<DtPlanVacunacion> getVacunas() {
		return Lista;
	}

	public void setVacunas(List<DtPlanVacunacion> vacunas) {
		this.Lista = vacunas;
	}
	
	  public List<DtPlanVacunacion> getEnfermedades() {
			return Lista;
		}

		public void setEnfermedades(List<DtPlanVacunacion> enfermedades) {
			this.Lista = enfermedades;
		}
		
		  public List<DtPlanVacunacion> getNombres() {
				return Lista;
			}

			public void setNombres(List<DtPlanVacunacion> nombres) {
				this.Lista = nombres;
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

   
}
  
