/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import Report.CriterioHasSitioEvaluacionReport;
import java.util.Date;
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
    public List<Sitioevaluacion> findAll(Integer estadoPrueba);
    public List<CriteriohijoHasSitioevaluacion> findBySitioEvaluacion(Sitioevaluacion sitioevaluacion);
    public List<CriterioHasSitioEvaluacionReport> devolverCriteriosAEvaluar(Integer codigoSitioEvaluacion);
    public boolean create (Sitioevaluacion sitio);
    public boolean update (Sitioevaluacion sitio);
    public boolean delete (Sitioevaluacion sitio);
    public boolean updateCriterioSitio(Integer puntuacion, Integer criterioHijo, Integer sitioEvaluacion, String comentario,Date fechaActual, String frecuencia);
    public List<Criteriopadre> findAllCriteriosPadre();
    public List<Integer> obtenerIdsCriteriosHijos(Integer idCriterioPadre);
    public boolean insertarCriteriosSitio(Integer codigoHijo, Integer codigoSitio, Integer padre);
    public boolean InsertarEvaluadoresColaboradores(Integer codigoHijo, Integer codigoSitio, Integer padre,Integer usuario);
    public Criteriopadre findByCriterioPadre (Integer idCriterioPadre);
    public Criteriohijo findByCriterioHijo (Integer idCriterioHijo);
    public boolean updateEstadoSitio(Integer codigoSitios, Integer estadoSitio);
    public List<Integer> sitiosPermitidos();
    public boolean isDueñoSitio(Integer idSitio);
    public boolean cambiarEstadoPrueba(Integer estado, Integer idSitio);
    public void agregarTotalEvaluadores(Integer codigoSitio, Integer cantidad);
    public Integer consultarTotalEvaluadores(Integer codigoSitio);
    public Integer consultarEvaluadoresParcial(Integer codigoSitio);
    public void agregarEvaluadorParcial(Integer codigoSitio, Integer cantidad);
}
