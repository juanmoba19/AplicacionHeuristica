/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import Report.EvaluacionDetalladaUsuarioReport;
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
    public Estadisticaprompuntaje buscarEstadisticaPromPuntaje(Integer sitioEvaluacion);
    public List<Integer> idsUsuariosBySitio(Integer idSitio);
    public List<Usuario> devolverUsuariosEvaluadores(Integer sitioEvaluacion);
    public List<EvaluacionDetalladaUsuarioReport> devolverDetallesEvaluaUsiario(Integer idUsuario, Integer codigoSitio);
}
