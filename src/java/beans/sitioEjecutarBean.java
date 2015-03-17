/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import Report.CriterioHasSitioEvaluacionReport;
import dao.HeuristicaDao;
import dao.HeuristicaDaoImpl;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.Sitioevaluacion;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "sitioEjecutarBean")
@SessionScoped
public class sitioEjecutarBean implements Serializable{

    // dao de heuristica dao
    private HeuristicaDao heuristicaDao;
    //lista de los criterios para evaluar en el sitio elegido
    private List<CriterioHasSitioEvaluacionReport> criteriosByEvaluacionSitio;
    // puntaje de la evaluacion heuristica
    private Integer puntaje;
    // Map de puntajes y su descripcion 
    private Map<String,Integer> puntajes;
    // Comentario del criterio evaluado
    private String comentarioCriterioSitio;
    // Flag pasara saber si fue o no seleccionado la puntuacion
    private boolean value2;
    // Sitio seleccionado
    private Sitioevaluacion selectedSitioEvaluacion;
    // flag para saber si el usuario es el dueño del sitio
    private boolean isDueñoSitio;
    
    /**
     * Creates a new instance of sitioEjecutarBean
     */
    public sitioEjecutarBean() {
        this.heuristicaDao = new HeuristicaDaoImpl();
        this.selectedSitioEvaluacion = new Sitioevaluacion();
        puntajes = new HashMap<String,Integer>();       
        puntajes.put("0 - No es un problema",0);
        puntajes.put("1 - Problema sin importancia",1);
        puntajes.put("2 - Problema de poca importancia",2);
        puntajes.put("3 - Problema grave",3);
        puntajes.put("4 - Catástrofe: obligatorio arreglarlo",4);
        HashMap mapResultado = new LinkedHashMap();
        List misMapKeys = new ArrayList(puntajes.keySet());
        List misMapValues = new ArrayList(puntajes.values());
        TreeSet conjuntoOrdenado = new TreeSet(misMapValues);
        Object[] arrayOrdenado = conjuntoOrdenado.toArray();
        int size = arrayOrdenado.length;
        for (int i = 0; i < size; i++) {
            mapResultado.put(misMapKeys.get(
                    misMapValues.indexOf(
                            arrayOrdenado[i])
            ), arrayOrdenado[i]);
        }
        this.puntajes = mapResultado;
    }
    
    public List<CriterioHasSitioEvaluacionReport> getCriteriosByEvaluacionSitio() {
        this.criteriosByEvaluacionSitio = this.heuristicaDao.devolverCriteriosAEvaluar(this.selectedSitioEvaluacion.getCodigo());
        return criteriosByEvaluacionSitio;
    }

    public void setCriteriosByEvaluacionSitio(List<CriterioHasSitioEvaluacionReport> criteriosByEvaluacionSitio) {
        this.criteriosByEvaluacionSitio = criteriosByEvaluacionSitio;
    }
    
    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }
    
    public void fijarPuntuacion(){
        setPuntaje(this.puntaje);
    }
    
    public String getComentarioCriterioSitio() {
        return comentarioCriterioSitio;
    }

    public void setComentarioCriterioSitio(String comentarioCriterioSitio) {
        try {
          this.comentarioCriterioSitio = comentarioCriterioSitio;  
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public boolean isValue2() {
        return value2;
    }

    public void setValue2(boolean value2) {
        this.value2 = value2;
    }
    
    public boolean fijarValue2(boolean value2){
        setValue2(value2);
        return value2;
    }
    
     public void addMessage(Integer codigoHijo,Integer codigoSitio) {
       String cometario = this.comentarioCriterioSitio;
       SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
       Date actual = null;
        try {
          Date date = new Date();
          String hoy = sdf.format(date);
          actual = sdf.parse(hoy);
        } catch (Exception e) {
            e.printStackTrace();
        }     
       
       if(this.puntaje == null){
        String summary =  "Por favor elegir un puntaje valido";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        this.value2 = false;
        }else{
           
        if(this.value2)
        heuristicaDao.updateCriterioSitio(this.puntaje, codigoHijo, codigoSitio, this.comentarioCriterioSitio,actual);
        else
        heuristicaDao.updateCriterioSitio(null, codigoHijo, codigoSitio, null, null);
        }        
        this.comentarioCriterioSitio = null;
    }
     
     public void ejecutarEvaluacionHeuristica(){
       
       boolean isDueño = this.isDueñoSitio;
       String ruta = "";
       if (isDueño){
           try {
            this.heuristicaDao.cambiarEstadoPrueba(3, this.selectedSitioEvaluacion.getCodigo()); 
            FacesContext context = FacesContext.getCurrentInstance();
             ruta = MyUtil.basepathlogin()+"views/heuristica/index.xhtml";
            context.getExternalContext().redirect(ruta);
           } catch (Exception e) {
               e.printStackTrace();
           }           
       }
   }
     
     public boolean isIsDueñoSitio() {
        
        this.isDueñoSitio = this.heuristicaDao.isDueñoSitio(this.selectedSitioEvaluacion.getCodigo());
        return isDueñoSitio;
    }
     
      public void setIsDueñoSitio(boolean isDueñoSitio) {
        this.isDueñoSitio = isDueñoSitio;
    }
      
      public Sitioevaluacion getSelectedSitioEvaluacion() {
        this.puntaje = null;
        this.value2 = false;        
        return selectedSitioEvaluacion;
        
    }

    public void setSelectedSitioEvaluacion(Sitioevaluacion selectedSitioEvaluacion) {
        this.selectedSitioEvaluacion = selectedSitioEvaluacion;
    }
    
    public void ejecutarHeuristica(ActionEvent event){
         
         String ruta = "";
         
         Sitioevaluacion sitioevaluacion = this.selectedSitioEvaluacion;
         if (sitioevaluacion != null){
             ruta = MyUtil.basepathlogin()+"views/heuristica/ejecucion_prueba.xhtml";
             FacesContext contex = FacesContext.getCurrentInstance();
             try {
                 contex.getExternalContext().redirect(ruta);
             } catch (IOException ex) {
                 Logger.getLogger(heuristicaBean.class.getName()).log(Level.SEVERE, null, ex);
             }
         }else{
             if (this.selectedSitioEvaluacion == null ){
                 this.selectedSitioEvaluacion = new Sitioevaluacion();
             }
         }
          
    }
}
