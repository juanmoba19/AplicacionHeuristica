/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import model.Criteriohijo;
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
    public boolean updateCriterioSitio(Integer puntuacion, Integer criterioHijo, Integer sitioEvaluacion);
    public List<Criteriopadre> findAllCriteriosPadre();
    public List<Integer> obtenerIdsCriteriosHijos(Integer idCriterioPadre);
    public boolean insertarCriteriosSitio(Integer codigoHijo, Integer codigoSitio, Integer padre);
    public Criteriopadre findByCriterioPadre (Integer idCriterioPadre);
    public Criteriohijo findByCriterioHijo (Integer idCriterioHijo);
    public boolean updateEstadoSitio(Integer codigoSitios, Integer estadoSitio);
}
