/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.HeuristicaDao;
import dao.HeuristicaDaoImpl;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import model.Criteriohijo;
import model.CriteriohijoHasSitioevaluacion;
import model.Criteriopadre;
import model.Sitioevaluacion;
import model.Usuario;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "heuristicaBean")
@SessionScoped
public class heuristicaBean implements Serializable{
    
    private List<Sitioevaluacion> sitios;
    private Sitioevaluacion selectedSitioEvaluacion;
    private List<CriteriohijoHasSitioevaluacion> criteriosHijosSitio;
    private Map<String,Integer> puntajes;
    private List<Criteriopadre> listaCriteriosPadre;
    private Integer puntaje;
    private boolean value2;
    private boolean value1;
    // booleano para el selectBoolean de los criterios padres
    private boolean value3;
    // Lista para guardar los codigos de los criterios padres que se anexaran 
    private List<Integer> codigosCriteriopadre;
    
    public List<CriteriohijoHasSitioevaluacion> getCriteriosHijosSitio(){
        
        HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
        this.criteriosHijosSitio = heuristicaDao.findBySitioEvaluacion(this.selectedSitioEvaluacion);
               
        return this.criteriosHijosSitio;        
    } 

    public List<Sitioevaluacion> getSitios() { 
        HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
        this.sitios = heuristicaDao.findAll();
        return sitios;
    }

    public void setSitios(List<Sitioevaluacion> sitios) {
        this.sitios = sitios;
    }

    /**
     * Creates a new instance of heuristicaBean
     */
    public heuristicaBean() {
        
        this.codigosCriteriopadre = new ArrayList<Integer>();
        this.sitios = new ArrayList<Sitioevaluacion>();
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

    public Sitioevaluacion getSelectedSitioEvaluacion() {
        return selectedSitioEvaluacion;
    }

    public void setSelectedSitioEvaluacion(Sitioevaluacion selectedSitioEvaluacion) {
        this.selectedSitioEvaluacion = selectedSitioEvaluacion;
    }

    public Map<String, Integer> getPuntajes() {
        return puntajes;
    }

    public void setPuntajes(Map<String, Integer> puntajes) {
        this.puntajes = puntajes;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public boolean isValue2() {
        return value2;
    }

    public void setValue2(boolean value2) {
        this.value2 = value2;
    }
    
    public void fijarPuntuacion(){
        setPuntaje(this.puntaje);
    }
    

    public void addMessage(Integer codigoHijo,Integer codigoSitio) {
       
       if(this.puntaje == null){
              String summary =  "Por favor elegir un puntaje valido";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        this.value2 = false;
        }else{
        String summary = value2 ? "Criterio Evaluado" : "Criterio NO Evaluado";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
        if(this.value2)
        heuristicaDao.updateCriterioSitio(this.puntaje, codigoHijo, codigoSitio);
        else
        heuristicaDao.updateCriterioSitio(null, codigoHijo, codigoSitio);
        }        
       
    }
    
    public boolean fijarValue2(boolean value2){
        setValue2(value2);
        return value2;
    }

    public boolean isValue1() {
        return value1;
    }

    public void setValue1(boolean value1) {
        this.value1 = value1;
    }
    
    public void process (AjaxBehaviorEvent event){
        this.value2 = this.value1;
    }

    public List<Criteriopadre> getListaCriteriosPadre() {
       HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
        this.listaCriteriosPadre = heuristicaDao.findAllCriteriosPadre();
        return listaCriteriosPadre;
    }

    public void setListaCriteriosPadre(List<Criteriopadre> listaCriteriosPadre) {
        this.listaCriteriosPadre = listaCriteriosPadre;
    }

    public boolean isValue3() {
        return value3;
    }

    public void setValue3(boolean value3) {
        this.value3 = value3;
    }
    /**
     * Mensaje para cuadno se adicione un criterio padre a la prueba heuristica
     */
    public void addMessage1(Integer codigoCriterioPadre) {
        String summary = value3 ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        if(value3){
            codigosCriteriopadre.add(codigoCriterioPadre);
        }else{
            for(int i=0; i < codigosCriteriopadre.size();i++){
                if(codigosCriteriopadre.get(i)==codigoCriterioPadre){
                    codigosCriteriopadre.remove(i);
                    break;
                }
                    
            }
        }
    }

    public List<Integer> getCodigosCriteriopadre() {
        return codigosCriteriopadre;
    }

    public void setCodigosCriteriopadre(List<Integer> codigosCriteriopadre) {
        this.codigosCriteriopadre = codigosCriteriopadre;
    }
    
    public void agregarCriteriosPadres() throws ParseException{
        
        String msg;
        HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
        //int idSitioEvaluacion = this.selectedSitioEvaluacion.getCodigo();
                
       //SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
       // Date date = new Date();
       // String hoy = sdf.format(date);
       // Date actual = sdf.parse(hoy);
        //this.comentario.setFecha(actual);
       // Sitioevaluacion sitioevaluacion = (Sitioevaluacion) session.load(Sitioevaluacion.class, idSitioEvaluacion);
        
        // agregar los datos a la clase CriteriohijoHasSitioevaluacion
        
        for (Integer criterioPadre : this.codigosCriteriopadre) {
            List<Integer> criteriosHijos = heuristicaDao.obtenerIdsCriteriosHijos(criterioPadre);
            for (Integer criterioHijo : criteriosHijos) {
                
                if (heuristicaDao.insertarCriteriosSitio(criterioHijo,this.selectedSitioEvaluacion.getCodigo(),criterioPadre)) {
                   
                } else {
                   msg = "Error al agregar los Criterios al Sitio";
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
                   break;
                }
            }
            
        }
        if (heuristicaDao.updateEstadoSitio(this.selectedSitioEvaluacion.getCodigo(), 2)){
            msg = "Se han agregado los Criterios y cambiado el estado al Sitio exitosamente";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
        }else{
            msg = "Error al cambiar esl estado del Sitio";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
        }
       
    }
    
  
}