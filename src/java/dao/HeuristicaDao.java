/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import model.CriteriohijoHasSitioevaluacion;
import model.Criteriopadre;
import model.Sitioevaluacion;

/**
 *
 * @author Juan Diego
 */
public interface HeuristicaDao {
    
    public Sitioevaluacion findBySitio(Sitioevaluacion sitio);
    public List<Sitioevaluacion> findAll();
    public List<CriteriohijoHasSitioevaluacion> findBySitioEvaluacion(Sitioevaluacion sitioevaluacion);
    public boolean create (Sitioevaluacion sitio);
    public boolean update (Sitioevaluacion sitio);
    public boolean delete (Sitioevaluacion sitio);
}
