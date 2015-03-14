/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.AnalisisHeuristicaDao;
import dao.AnalisisHeuristicaDaoImpl;
import dao.SitioDao;
import dao.SitioDaoImpl;
import enumerator.promCriteriosPadre;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.event.ActionEvent;
import model.Estadisticaprompuntaje;
import model.Sitioevaluacion;
import model.Usuario;
import org.primefaces.context.RequestContext;
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
    private AnalisisHeuristicaDao analisisHeuristicaDao;
    // Model que nos permite crear las graficas de estadistica 
    private BarChartModel barModel;
    // Lista de los usuarios implicados en al evaluacion
    private List<Usuario> listaUsuario;
    // usuario seleccionado del grid
    private Usuario selectedUsuario;
    /**
     * Creates a new instance of sitioAnalizarBean
     */
    
    
    public sitioAnalizarBean() {
        this.sitioDao = new SitioDaoImpl();
        this.analisisHeuristicaDao = new AnalisisHeuristicaDaoImpl();
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
          createBarModels();
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
         
        barModel.setTitle("Promedio Puntajes Evaluacion Heuristica");
        barModel.setLegendPosition("ne");
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Criterios Heuristicos");
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Puntaje");
        yAxis.setMin(0);
        yAxis.setMax(5);
    }
     
     private BarChartModel initBarModel() {
        
        Integer idSitio = this.selectedSitio.getCodigo();
        Estadisticaprompuntaje estadisticaprompuntaje = null; 
        boolean existeAnalisis = analisisHeuristicaDao.isSitioEstadisticaPromPuntaje(idSitio);
        if(existeAnalisis == false){
          analisisHeuristicaDao.agregarPromHeuristicos(idSitio);
        }        
        estadisticaprompuntaje = analisisHeuristicaDao.buscarEstadisticaPromPuntaje(idSitio);
                
        BarChartModel model = new BarChartModel();
        ChartSeries criterios = new ChartSeries();
        criterios.setLabel("Criterio Padre");
        criterios.set(promCriteriosPadre.CRITERIO1.getCriterioPadre(), estadisticaprompuntaje.getCriterio1());
        criterios.set(promCriteriosPadre.CRITERIO2.getCriterioPadre(), estadisticaprompuntaje.getCriterio2());
        criterios.set(promCriteriosPadre.CRITERIO3.getCriterioPadre(), estadisticaprompuntaje.getCriterio3());
        criterios.set(promCriteriosPadre.CRITERIO4.getCriterioPadre(), estadisticaprompuntaje.getCriterio4());
        criterios.set(promCriteriosPadre.CRITERIO5.getCriterioPadre(), estadisticaprompuntaje.getCriterio5());
        criterios.set(promCriteriosPadre.CRITERIO6.getCriterioPadre(), estadisticaprompuntaje.getCriterio6());
        criterios.set(promCriteriosPadre.CRITERIO7.getCriterioPadre(), estadisticaprompuntaje.getCriterio7());
        criterios.set(promCriteriosPadre.CRITERIO8.getCriterioPadre(), estadisticaprompuntaje.getCriterio8());
        criterios.set(promCriteriosPadre.CRITERIO9.getCriterioPadre(), estadisticaprompuntaje.getCriterio9());
        criterios.set(promCriteriosPadre.CRITERIO10.getCriterioPadre(),estadisticaprompuntaje.getCriterio10());
        model.addSeries(criterios);
         
        return model;
    }

    public List<Usuario> getListaUsuario() {
        this.listaUsuario = analisisHeuristicaDao.devolverUsuariosEvaluadores(this.selectedSitio.getCodigo());
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }
    
    
     
}
