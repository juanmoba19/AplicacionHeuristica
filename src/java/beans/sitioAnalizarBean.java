/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.SitioDao;
import dao.SitioDaoImpl;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.Sitioevaluacion;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "sitioAnalizarBean")
@SessionScoped
public class sitioAnalizarBean implements Serializable {

    private Sitioevaluacion selectedSitio;
    private SitioDao sitioDao;
    // Model que nos permite crear las graficas de estadistica 
    private BarChartModel barModel;
    /**
     * Creates a new instance of sitioAnalizarBean
     */
    
    @PostConstruct
    public void init() {
        createBarModels();
    }
    
    public sitioAnalizarBean() {
        this.sitioDao = new SitioDaoImpl();
        this.selectedSitio = new Sitioevaluacion();
    }

    public Sitioevaluacion getSelectedSitio() {
        return selectedSitio;
    }

    public void setSelectedSitio(Sitioevaluacion selectedSitio) {
        this.selectedSitio = selectedSitio;
    }
    
     public void seguirSitio(ActionEvent event){
      RequestContext context = RequestContext.getCurrentInstance();
      boolean isRuta;
      String ruta = "";
      
      Sitioevaluacion sitioevaluacion = this.sitioDao.findBySitio(this.selectedSitio);
      if (sitioevaluacion != null){
          isRuta = true;
          ruta = MyUtil.basepathlogin()+"views/estadistica/analisis_sitio.xhtml";
      }else{
          isRuta = false;
          if (this.selectedSitio == null ){
              this.selectedSitio = new Sitioevaluacion();
          }
      }
     context.addCallbackParam("isRuta", isRuta);
     context.addCallbackParam("ruta", ruta);   
 }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }
     
     private void createBarModels() {
        createBarModel();
    }
     
     private void createBarModel() {
        barModel = initBarModel();
         
        barModel.setTitle("Bar Chart");
        barModel.setLegendPosition("ne");
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Gender");
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }
     
     private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
         
        return model;
    }
    
     public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                        "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
