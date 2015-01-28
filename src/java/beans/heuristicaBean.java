/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.HeuristicaDao;
import dao.HeuristicaDaoImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import model.CriteriohijoHasSitioevaluacion;
import model.Criteriopadre;
import model.Sitioevaluacion;

/**
 *
 * @author Juan Diego
 */
@Named(value = "heuristicaBean")
@RequestScoped
public class heuristicaBean {
    
    private List<Sitioevaluacion> sitios;
    private Sitioevaluacion selectedSitioEvaluacion;
    private List<CriteriohijoHasSitioevaluacion> criteriosHijosSitio;
    private HeuristicaDao heuristicaDao;
    
    public List<CriteriohijoHasSitioevaluacion> getCriteriosHijosSitio(){        
        this.criteriosHijosSitio = this.heuristicaDao.findBySitioEvaluacion(this.selectedSitioEvaluacion);
        
        HashSet hs = new HashSet();
        hs.addAll(this.criteriosHijosSitio);
        this.criteriosHijosSitio.clear();
        this.criteriosHijosSitio.addAll(hs);
       
        return this.criteriosHijosSitio;        
    } 

    public List<Sitioevaluacion> getSitios() {        
        this.sitios = this.heuristicaDao.findAll();
        return sitios;
    }

    public void setSitios(List<Sitioevaluacion> sitios) {
        this.sitios = sitios;
    }

    /**
     * Creates a new instance of heuristicaBean
     */
    public heuristicaBean() {
        
        this.heuristicaDao = new HeuristicaDaoImpl();
        this.sitios = new ArrayList<Sitioevaluacion>();
        if(this.selectedSitioEvaluacion == null){
           this.selectedSitioEvaluacion = new Sitioevaluacion();
        }      
    }

    public Sitioevaluacion getSelectedSitioEvaluacion() {
        return selectedSitioEvaluacion;
    }

    public void setSelectedSitioEvaluacion(Sitioevaluacion selectedSitioEvaluacion) {
        this.selectedSitioEvaluacion = selectedSitioEvaluacion;
    }
    
}