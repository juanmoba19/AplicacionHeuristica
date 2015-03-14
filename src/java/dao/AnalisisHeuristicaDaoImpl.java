/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import model.Estadisticaprompuntaje;
import model.Sitioevaluacion;
import model.Usuario;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
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
        strSql.append(" SELECT AVG (internal.sum) AS prom ");
        strSql.append(" FROM ( ");
        strSql.append(" SELECT sum(puntuacion_escala) AS sum ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion ");
        strSql.append(" WHERE sitioevaluacion_codigo = :sitioEvaluacion ");
        strSql.append(" AND criteriopadre_codigo= :criterioPadre ");
        strSql.append(" GROUP BY puntuacion_escala ");
        strSql.append(" ) internal ");
        
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
        String sql =  "FROM Usuario u WHERE u.id IN (:idsUsuario)";
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
    
}
