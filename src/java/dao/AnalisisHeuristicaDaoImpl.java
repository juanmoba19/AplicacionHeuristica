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
import javax.faces.context.FacesContext;
import model.Estadisticaprompuntaje;
import model.Estadisticaprompuntajebyusuario;
import model.EstadisticaprompuntajebyusuarioId;
import model.Sitioevaluacion;
import model.Usuario;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
@Component
public class AnalisisHeuristicaDaoImpl implements  AnalisisHeuristicaDao{

    // Usuario en sesion 
    Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj"); 
    
    @Override
    public void agregarPromHeuristicos(Integer sitioEvaluacion) {        
        
              
        Sitioevaluacion sitio = findBySitio(sitioEvaluacion);
        Estadisticaprompuntaje estadisticaprompuntaje = new Estadisticaprompuntaje(); 
        estadisticaprompuntaje.setSitioevaluacion(sitio);
        double prom = 0;
        
        prom = generarPromHeuristico(1, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio1(prom);
        
        prom = generarPromHeuristico(2, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio2(prom);
        
        prom = generarPromHeuristico(3, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio3(prom);
        
        prom = generarPromHeuristico(4, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio4(prom);
        
        prom = generarPromHeuristico(5, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio5(prom);
        
        prom = generarPromHeuristico(6, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio6(prom);
        
        prom = generarPromHeuristico(7, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio7(prom);
        
        prom = generarPromHeuristico(8, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio8(prom);
        
        prom = generarPromHeuristico(9, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio9(prom);
        
        prom = generarPromHeuristico(10, sitioEvaluacion);
        estadisticaprompuntaje.setCriterio10(prom);
        
        Session session  = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(estadisticaprompuntaje);
             session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
                
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double generarPromHeuristico(Integer criterioPadre, Integer sitioEvaluacion) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT AVG (puntuacion_escala) AS prom ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion ");
        strSql.append(" WHERE criteriopadre_codigo = :criterioPadre ");
        strSql.append(" AND sitioevaluacion_codigo = :sitioEvaluacion ");
        strSql.append(" GROUP BY criteriopadre_codigo ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("sitioEvaluacion", sitioEvaluacion);
        query.setParameter("criterioPadre", criterioPadre);
        query.addScalar("prom",StandardBasicTypes.DOUBLE);
        
        Double result = (Double) query.uniqueResult();
        if ( result == null)
        result = 0.0;
        
        return result;
              
    }
    
    public Sitioevaluacion findBySitio(Integer sitio) {
       
        Sitioevaluacion model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Sitioevaluacion WHERE codigo = '"+sitio+"'";
        try {
            session.beginTransaction();
            model = (Sitioevaluacion) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public boolean isSitioEstadisticaPromPuntaje(Integer sitioEvaluacion) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        boolean flag = false;
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT idestadisticaPromPuntaje AS id ");
        strSql.append(" FROM estadisticaprompuntaje ");
        strSql.append(" WHERE sitioevaluacion_codigo = :sitioEvaluacion ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("sitioEvaluacion", sitioEvaluacion);
        query.addScalar("id",StandardBasicTypes.INTEGER);
        
        Integer result = (Integer) query.uniqueResult();
        if ( result != null && result > 0)
        flag = true;
        
        return flag;
    }

    @Override
    public Estadisticaprompuntaje buscarEstadisticaPromPuntaje(Integer sitioEvaluacion) {
        Estadisticaprompuntaje model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Estadisticaprompuntaje e left join fetch e.sitioevaluacion as s WHERE s.codigo = :sitioEvaluacion";
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            query.setInteger("sitioEvaluacion", sitioEvaluacion);
            model = (Estadisticaprompuntaje)query.uniqueResult();
            session.beginTransaction().commit();
           
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public List<Integer> idsUsuariosBySitio(Integer idSitio) {
        
        if (idSitio == null || idSitio < 0){
            return null;
        }
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT DISTINCT(usuario_id) AS idUsuario ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion ");
        strSql.append(" WHERE sitioevaluacion_codigo = :idSitio ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("idSitio", idSitio);
        query.addScalar("idUsuario",StandardBasicTypes.INTEGER);
        
        return query.list();
    }

    @Override
    public List<Usuario> devolverUsuariosEvaluadores(Integer sitioEvaluacion) {
        
       if (sitioEvaluacion == null || sitioEvaluacion <= 0){
           return null;
       }
       
       List<Integer> listaIdsUsuario =  idsUsuariosBySitio(sitioEvaluacion);
       
       List<Usuario> model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Usuario u left join fetch u.rol WHERE u.id IN (:idsUsuario)";
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            query.setParameterList("idsUsuario", listaIdsUsuario);
            model = query.list();
            session.beginTransaction().commit();
           
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public List<EvaluacionDetalladaUsuarioReport> devolverDetallesEvaluaUsiario(Integer idUsuario, Integer codigoSitio) {
        if (idUsuario == null || idUsuario < 0 || codigoSitio == null || codigoSitio < 0){
            return null;
        }
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT ch.descripcion AS criterio,   ");
        strSql.append(" chs.puntuacion_escala AS puntuacion, ");
        strSql.append(" chs.fecha AS fecha,  ");
        strSql.append(" chs.comentario AS comentario ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion chs  ");
        strSql.append(" INNER JOIN criteriohijo ch ON chs.criteriohijo_codigo = ch.codigo ");
        strSql.append(" WHERE chs.usuario_id= :idUsuario  ");
        strSql.append(" AND chs.sitioevaluacion_codigo = :codigoSitio ");

        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setResultTransformer(Transformers.aliasToBean(EvaluacionDetalladaUsuarioReport.class));
        
        query.setParameter("codigoSitio", codigoSitio);
        query.setParameter("idUsuario", idUsuario);
        
        query.addScalar("criterio",StandardBasicTypes.STRING);
        query.addScalar("puntuacion",StandardBasicTypes.INTEGER);
        query.addScalar("fecha",StandardBasicTypes.DATE);
        query.addScalar("comentario",StandardBasicTypes.STRING);
        
        return query.list();
    }

    @Override
    public boolean isSitioPromPuntajeByUsuario(Integer sitioEvaluacion) {
        
        Integer idUsuario = usuario.getId();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        boolean flag = false;
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT puntuacion_escala AS id ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion ");
        strSql.append(" WHERE sitioevaluacion_codigo = :sitioEvaluacion ");
        strSql.append(" AND sitioevaluacion_codigo = :usuario_id ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("sitioEvaluacion", sitioEvaluacion);
        query.setParameter("usuario_id", idUsuario);
        query.addScalar("id",StandardBasicTypes.INTEGER);
        
        Integer result = (Integer) query.uniqueResult();
        if ( result != null && result > 0)
        flag = true;
        
        return flag;
        
    }
    
    public Double buscarPromHeuristicoByUsuario(Integer idSitio, Integer idUsuario) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT promedio AS prom ");
        strSql.append(" FROM sitioevaluacion_has_usuario_prom_eval ");
        strSql.append(" WHERE sitioevaluacion_codigo = :idSitio ");
        strSql.append(" AND usuario_id = :idUsuario ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("idSitio", idSitio);
        query.setParameter("idUsuario", idUsuario);
        query.addScalar("prom",StandardBasicTypes.DOUBLE);
        
        Double result = (Double) query.uniqueResult();
        if ( result == null)
        result = 0.0;
        
        return result;
              
    }
    
    public void agregarPromHeuristicoByUsuario(Integer idSitio, List<Integer> idsUsuarios){
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        
        for (Integer idUsuario : idsUsuarios) {
            StringBuilder strSql = new StringBuilder();
            strSql.append(" INSERT INTO sitioevaluacion_has_usuario_prom_eval ");
            strSql.append(" (sitioevaluacion_codigo,usuario_id,promedio) ");
            strSql.append(" SELECT :idSitio, :idUsuario,  ");
            strSql.append(" AVG(puntuacion_escala) ");
            strSql.append(" FROM criteriohijo_has_sitioevaluacion ");
            strSql.append(" WHERE sitioevaluacion_codigo = :idSitio AND usuario_id = :idUsuario ");

            session.beginTransaction();
            SQLQuery query = session.createSQLQuery(strSql.toString());
            query.setParameter("idSitio", idSitio);
            query.setParameter("idUsuario", idUsuario);
            query.executeUpdate();
            session.beginTransaction().commit();
        }
        
    }
    
    public void agregarPromHeuristicoByCriteriByUsuario(Integer sitioEvaluacion, List<Integer>idsUsuario) {        
        
              
        Sitioevaluacion sitio = findBySitio(sitioEvaluacion);
        Estadisticaprompuntajebyusuario estadisticaprompuntaje = new Estadisticaprompuntajebyusuario(); 
        estadisticaprompuntaje.setSitioevaluacion(sitio);        
        double prom = 0;
        
        for (Integer idUsuario : idsUsuario) {
       
        Usuario miUsuario = findByUsuario(idUsuario);
        estadisticaprompuntaje.setUsuario(miUsuario);
           
        prom = generarPromHeuristicoByCriteriByUsuario(1, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio1(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(2, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio2(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(3, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio3(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(4, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio4(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(5, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio5(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(6, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio6(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(7, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio7(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(8, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio8(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(9, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio9(prom);
        
        prom = generarPromHeuristicoByCriteriByUsuario(10, sitioEvaluacion, idUsuario);
        estadisticaprompuntaje.setCriterio10(prom);
        
        EstadisticaprompuntajebyusuarioId objectUsuario = new EstadisticaprompuntajebyusuarioId(idUsuario, sitioEvaluacion);
        estadisticaprompuntaje.setId(objectUsuario);
        
        Session session  = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(estadisticaprompuntaje);
             session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        }       
    }
    
    public Double generarPromHeuristicoByCriteriByUsuario(Integer criterioPadre, Integer sitioEvaluacion, Integer idUsuario) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT IFNULL(AVG(puntuacion_escala),0) AS prom ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion ");
        strSql.append(" WHERE usuario_id = :idUsuario ");
        strSql.append(" AND sitioevaluacion_codigo = :sitioEvaluacion ");
        strSql.append(" AND criteriopadre_codigo = :criterioPadre ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("sitioEvaluacion", sitioEvaluacion);
        query.setParameter("criterioPadre", criterioPadre);
        query.addScalar("prom",StandardBasicTypes.DOUBLE);
        
        return (Double)query.uniqueResult();
              
    }
    
    public Usuario findByUsuario(Integer idUsuario) {
        
        Usuario model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Usuario WHERE id = '"+idUsuario+"'";
        try {
            session.beginTransaction();
            model = (Usuario) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }
    
    public PromedioUsuarioReport buscarPromHeuristicoByCriteriByUsuario(Integer sitioEvaluacion, Integer idUsuario){
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT u.usuario AS usuario, ");
        strSql.append(" criterio1 AS criterio1, criterio2 AS criterio2,  ");
        strSql.append(" criterio3 AS criterio3, criterio4 AS criterio4,  ");
        strSql.append(" criterio5 AS criterio5, criterio6 AS criterio6,  ");
        strSql.append(" criterio7 AS criterio7, criterio8 AS criterio8,  ");
        strSql.append(" criterio9 AS criterio9, criterio10 AS criterio10  ");
        strSql.append(" FROM estadisticaprompuntajebyusuario e ");
        strSql.append(" INNER JOIN usuario u ");
        strSql.append(" ON e.usuario_id = u.id ");
        strSql.append(" WHERE e.usuario_id = :idUsuario ");
        strSql.append(" AND e.sitioevaluacion_codigo = :sitioEvaluacion ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setResultTransformer(Transformers.aliasToBean(PromedioUsuarioReport.class));
        query.setParameter("idUsuario", idUsuario);
        query.setParameter("sitioEvaluacion", sitioEvaluacion);        
        query.addScalar("usuario",StandardBasicTypes.STRING);
        query.addScalar("criterio1",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio2",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio3",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio4",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio5",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio6",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio7",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio8",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio9",StandardBasicTypes.DOUBLE);
        query.addScalar("criterio10",StandardBasicTypes.DOUBLE);
        
        return (PromedioUsuarioReport)query.uniqueResult();
    }
    
    public boolean isSitioEstadisticaPromPuntajeByCriterioByUsuario(Integer idSitio){
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        boolean flag = false;
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT sitioevaluacion_codigo AS id ");
        strSql.append(" FROM estadisticaprompuntajebyusuario ");
        strSql.append(" WHERE sitioevaluacion_codigo = :idSitio ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("idSitio", idSitio);
        query.addScalar("id",StandardBasicTypes.INTEGER);
        
        Integer result = (Integer) query.uniqueResult();
        if ( result != null && result > 0)
        flag = true;
        
        return flag;
    }

    @Override
    public List<EvaluacionComentariosCriteriosReport> verComentariosConsolidadosSitio(Integer idSitio) {
        
        if (idSitio == null || idSitio <= 0){
            return null;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT criteriohijo.descripcion AS criterio,   ");
        strSql.append(" GROUP_CONCAT(criteriositio.comentario) AS comentario ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion criteriositio  ");
        strSql.append(" INNER JOIN criteriohijo criteriohijo ");
        strSql.append(" ON criteriositio.criteriohijo_codigo = criteriohijo.codigo  ");
        strSql.append(" WHERE sitioevaluacion_codigo = :sitioCodigo ");
        strSql.append(" GROUP BY criteriositio.criteriohijo_codigo ");

        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setResultTransformer(Transformers.aliasToBean(EvaluacionDetalladaUsuarioReport.class));
        
        query.setParameter("sitioCodigo", idSitio);
        
        query.addScalar("criterio",StandardBasicTypes.STRING);
        query.addScalar("comentario",StandardBasicTypes.STRING);
        
        return query.list();
    }
    
}
