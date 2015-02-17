/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.HeuristicaDao;
import dao.HeuristicaDaoImpl;
import dao.UsuarioDao;
import dao.UsuarioDaoImpl;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import model.CriteriohijoHasSitioevaluacion;
import model.Criteriopadre;
import model.Sitioevaluacion;
import model.Usuario;
import org.primefaces.context.RequestContext;
import util.MyUtil;

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
    private String comentarioCriterioSitio;
    // Lista de todos los usuarios
    private List<Usuario> usuarios;
    // Nombre de los usuarios para mostrar en el checkbox
    private HashMap<Integer,String> nomUsuarios;
    // Arreglo de los nombres seleccionados
    private String [] selectedUsuario;
    
    
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
        this.progress = 0;
        
    }

    public Sitioevaluacion getSelectedSitioEvaluacion() {
        this.puntaje = null;
        this.value2 = false;
        this.progress = 15;
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
    

    public void addMessage(Integer codigoHijo,Integer codigoSitio, String cometario) {
       cometario = this.comentarioCriterioSitio;
       if(this.puntaje == null){
              String summary =  "Por favor elegir un puntaje valido";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        this.value2 = false;
        }else{
        String summary = value2 ? "Criterio Evaluado" : "Criterio NO Evaluado";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
        HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
        if(this.value2)
        heuristicaDao.updateCriterioSitio(this.puntaje, codigoHijo, codigoSitio, this.comentarioCriterioSitio);
        else
        heuristicaDao.updateCriterioSitio(null, codigoHijo, codigoSitio, null);
        }        
       this.comentarioCriterioSitio = null;
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
        this.value3 = false;
        if (heuristicaDao.updateEstadoSitio(this.selectedSitioEvaluacion.getCodigo(), 2)){
            msg = "Se han agregado los Criterios y cambiado el estado al Sitio exitosamente";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
        }else{
            msg = "Error al cambiar esl estado del Sitio";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
        }
       
    }
    
    public void ejecutarHeuristica(ActionEvent event){
         RequestContext context = RequestContext.getCurrentInstance();
         boolean isRuta;
         String ruta = "";
         
         Sitioevaluacion sitioevaluacion = this.selectedSitioEvaluacion;
         if (sitioevaluacion != null){
             isRuta = true;
             ruta = MyUtil.basepathlogin()+"views/heuristica/ejecucion_prueba.xhtml";
         }else{
             isRuta = false;
             if (this.selectedSitioEvaluacion == null ){
                 this.selectedSitioEvaluacion = new Sitioevaluacion();
             }
         }
        context.addCallbackParam("isRuta", isRuta);
        context.addCallbackParam("ruta", ruta);   
    }

    public String getComentarioCriterioSitio() {
        return comentarioCriterioSitio;
    }

    public void setComentarioCriterioSitio(String comentarioCriterioSitio) {
        this.comentarioCriterioSitio = comentarioCriterioSitio;
    }
    
    private Integer progress;
 
    public Integer getProgress() {
        if(progress == null) {
            progress = 0;
        }
        else {
            progress = progress + (int)(Math.random() * 40);
             
            if(progress > 100)
                progress = 100;
        }
         
        return progress;
    }
 
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
     
    public void onComplete() {
        this.progress= 0;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Progress Completed"));
    }
    
    public void inicializarProgress(){
        this.progress = 5;
    }
    
    public List<Usuario> getUsuarios() {
        
        UsuarioDao usuarioDao = new UsuarioDaoImpl();
        this.usuarios = usuarioDao.findAll();
        return usuarios;
    }

    public void setCriteriosHijosSitio(List<CriteriohijoHasSitioevaluacion> criteriosHijosSitio) {
        this.criteriosHijosSitio = criteriosHijosSitio;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public HashMap<Integer,String> getNomUsuarios() {
        llenarListaCheckbox();
        return nomUsuarios;
    }

    public void setNomUsuarios(HashMap<Integer,String> nomUsuarios) {
        this.nomUsuarios = nomUsuarios;
    }
    
    public void llenarListaCheckbox(){
        nomUsuarios = new HashMap<Integer,String>();
        List<Usuario> usuarios = getUsuarios();
        
        for(Usuario usuario : usuarios){
            nomUsuarios.put(usuario.getId(),usuario.getUsuario());
        }
        
    }

    public String[] getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(String[] selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }
    
    
  
} 