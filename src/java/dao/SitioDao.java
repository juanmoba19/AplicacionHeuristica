/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import model.Sitioevaluacion;

/**
 *
 * @author Juan Diego
 */
public interface SitioDao {
    
    public boolean createSitio(Sitioevaluacion sitioEvaluacion);
    public Sitioevaluacion findBySitio(Sitioevaluacion sitioevaluacion);
    
}
