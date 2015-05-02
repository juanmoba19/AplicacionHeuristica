/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import Report.CriterioHasSitioEvaluacionReport;
import dao.HeuristicaDao;
import dao.HeuristicaDaoImpl;
import dao.UsuarioDao;
import dao.UsuarioDaoImpl;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    //lista de los criterios para evaluar en el sitio elegido
    transient List<CriterioHasSitioEvaluacionReport> criteriosByEvaluacionSitio;
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
    // ids de Integer para agregar los evaluadores a prueba
    private List<Integer> idsColaboradores;    
    // Variable para saber si es dueño de la prueba del sitio
    private boolean isDueñoSitio;
    // dao de heuristica dao
    transient HeuristicaDao heuristicaDao;
    
    
    public List<CriteriohijoHasSitioevaluacion> getCriteriosHijosSitio(){
        
        this.criteriosHijosSitio = this.heuristicaDao.findBySitioEvaluacion(this.selectedSitioEvaluacion);        
               
        return this.criteriosHijosSitio;        
    } 

    public List<Sitioevaluacion> getSitios(Integer estadoPrueba) { 
        
        this.sitios = this.heuristicaDao.findAll(estadoPrueba);
        return sitios;
    }

    public List<CriterioHasSitioEvaluacionReport> getCriteriosByEvaluacionSitio() {
        this.criteriosByEvaluacionSitio = this.heuristicaDao.devolverCriteriosAEvaluar(this.selectedSitioEvaluacion.getCodigo());
        return criteriosByEvaluacionSitio;
    }

    public void setCriteriosByEvaluacionSitio(List<CriterioHasSitioEvaluacionReport> criteriosByEvaluacionSitio) {
        this.criteriosByEvaluacionSitio = criteriosByEvaluacionSitio;
    }
    
    

    public void setSitios(List<Sitioevaluacion> sitios) {
        this.sitios = sitios;
    }

    /**
     * Creates a new instance of heuristicaBean
     */
    public heuristicaBean() {
        
        this.heuristicaDao = new HeuristicaDaoImpl();
        this.codigosCriteriopadre = new ArrayList<Integer>();
        this.sitios = new ArrayList<Sitioevaluacion>();
        this.selectedSitioEvaluacion = new Sitioevaluacion();
        this.idsColaboradores = new ArrayList<Integer>();
        puntajes = new HashMap<String,Integer>();       
        puntajes.put("1 - No es un problema",1);
        puntajes.put("2 - Problema sin importancia",2);
        puntajes.put("3 - Problema de poca importancia",3);
        puntajes.put("4 - Problema grave",4);
        puntajes.put("5 - Catástrofe: obligatorio arreglarlo",5);
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
    

    public void addMessage(Integer codigoHijo,Integer codigoSitio) {
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
        
        HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
        if(this.value2)
        heuristicaDao.updateCriterioSitio(this.puntaje, codigoHijo, codigoSitio, this.comentarioCriterioSitio,actual);
        else
        heuristicaDao.updateCriterioSitio(null, codigoHijo, codigoSitio, null, null);
        }        
        this.comentarioCriterioSitio = null;
        this.puntaje = null;
        this.value2 = false;
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
       
       
    }
    
    public void ejecutarHeuristica(ActionEvent event){
         
         String ruta = "";
         this.comentarioCriterioSitio = null;
         this.puntaje = null;
         this.value2 = false;
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
    
    public List<Usuario> getUsuarios(Integer num) {
        
        UsuarioDao usuarioDao = new UsuarioDaoImpl();
        this.usuarios = usuarioDao.findAll(num);
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
        List<Usuario> usuarios = getUsuarios(1);
        
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
    
   public void agregarEvaluadoresParticipantes(){
        String msg;
        HeuristicaDao heuristicaDao = new HeuristicaDaoImpl();
     
        Set<Integer> idsUsuario = this.nomUsuarios.keySet();
       String[] selectedUsuarios = this.selectedUsuario;
       if(selectedUsuarios.length < 5){
       for (String su : selectedUsuarios) {
           for (Integer o : this.nomUsuarios.keySet()) {
               if (this.nomUsuarios.get(o).equals(su)) {
                   this.idsColaboradores.add(o);
                   break;
               }
           }
       }
        for(Integer idUsuario: this.idsColaboradores){
            for (Integer criterioPadre : this.codigosCriteriopadre) {
            List<Integer> criteriosHijos = heuristicaDao.obtenerIdsCriteriosHijos(criterioPadre);
            for (Integer criterioHijo : criteriosHijos) {
                
                if (heuristicaDao.InsertarEvaluadoresColaboradores(criterioHijo,this.selectedSitioEvaluacion.getCodigo(),criterioPadre,idUsuario)) {
                   
                } else {
                   msg = "Error al agregar los Criterios al Sitio";
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg));
                   break;
                }
            }
            
        }
        }
       }else{
           this.selectedUsuario = null;
           msg = "Se recomienda elegir 4 o menos evaluadores";
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
           FacesContext.getCurrentInstance().addMessage(null, message);
       }
       
       this.value3 = false;
       if(selectedUsuarios.length < 5){
        if (heuristicaDao.updateEstadoSitio(this.selectedSitioEvaluacion.getCodigo(), 2)){
            msg = "Se han agregado los Criterios y cambiado el estado al Sitio exitosamente";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.selectedUsuario = null;
        }else{
            msg = "Error al cambiar esl estado del Sitio";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            this.selectedUsuario = null;
            return;
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
   
   public void seguirAnalizarHeuristica(){
       String ruta = "";
       try {
            FacesContext context = FacesContext.getCurrentInstance();
            ruta = MyUtil.basepathlogin()+"views/estadistica/index.xhtml";
       } catch (Exception e) {
            e.printStackTrace();
       }
   }
  
} 