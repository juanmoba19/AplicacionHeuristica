/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import Report.EvaluacionComentariosCriteriosReport;
import Report.EvaluacionDetalladaUsuarioReport;
import Report.PromedioUsuarioReport;
import java.util.List;
import model.Estadisticaprompuntaje;
import model.Usuario;


/**
 *
 * @author Juan Diego
 */
public interface AnalisisHeuristicaDao {
    
    public Double generarPromHeuristico(Integer criterioPadre, Integer sitioEvaluacion);
    public void agregarPromHeuristicos(Integer sitioEvaluacion);
    public boolean isSitioEstadisticaPromPuntaje(Integer sitioEvaluacion);
    public boolean isSitioPromPuntajeByUsuario(Integer sitioEvaluacion);
    public Estadisticaprompuntaje buscarEstadisticaPromPuntaje(Integer sitioEvaluacion);
    public List<Integer> idsUsuariosBySitio(Integer idSitio);
    public List<Usuario> devolverUsuariosEvaluadores(Integer sitioEvaluacion);
    public List<EvaluacionDetalladaUsuarioReport> devolverDetallesEvaluaUsiario(Integer idUsuario, Integer codigoSitio);
    public Double buscarPromHeuristicoByUsuario(Integer idSitio, Integer idUsuario);
    public void agregarPromHeuristicoByUsuario(Integer idSitio, List<Integer> idsUsuarios);
    public Double generarPromHeuristicoByCriteriByUsuario(Integer criterioPadre, Integer sitioEvaluacion, Integer idUsuario);
    public void agregarPromHeuristicoByCriteriByUsuario(Integer sitioEvaluacion, List<Integer>idsUsuario);        
    public Usuario findByUsuario(Integer idUsuario);
    public PromedioUsuarioReport buscarPromHeuristicoByCriteriByUsuario(Integer sitioEvaluacion, Integer idUsuario);
    public boolean isSitioEstadisticaPromPuntajeByCriterioByUsuario(Integer idSitio);
    public List<EvaluacionComentariosCriteriosReport> verComentariosConsolidadosSitio(Integer idSitio);
}
