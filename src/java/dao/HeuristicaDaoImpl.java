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
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
public class HeuristicaDaoImpl implements HeuristicaDao{

    @Override
    public Sitioevaluacion findBySitio(Sitioevaluacion sitio) {
       
        Sitioevaluacion model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Sitioevaluacion WHERE codigo = '"+sitio.getCodigo()+"'";
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
    public List<Sitioevaluacion> findAll() {
       
        List<Sitioevaluacion> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Sitioevaluacion s left join fetch s.estadoprueba";
        try {
            session.beginTransaction();
            listado = session.createQuery(sql).list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return listado;
    }

    @Override
    public boolean create(Sitioevaluacion sitio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(Sitioevaluacion sitio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(Sitioevaluacion sitio) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CriteriohijoHasSitioevaluacion> findBySitioEvaluacion(Sitioevaluacion sitioevaluacion) {
        List<CriteriohijoHasSitioevaluacion> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM CriteriohijoHasSitioevaluacion chs left join fetch chs.criteriohijo  as ch join fetch  chs.criteriopadre as cp left join fetch chs.id as d WHERE d.sitioevaluacionCodigo = '"+sitioevaluacion.getCodigo()+"'";
        try {
            session.beginTransaction();
            listado = session.createQuery(sql).list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.beginTransaction().rollback();
        }
        return listado;
    }

    @Override
    public boolean updateCriterioSitio(Integer puntuacion, Integer criterioHijo, Integer sitioEvaluacion) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String hql="";
        
        try {
            
            session.beginTransaction();
            if (  puntuacion != null )
            hql = "UPDATE CriteriohijoHasSitioevaluacion set puntuacion_escala = :puntuacion where criteriohijo_codigo = :criterioHijo and sitioevaluacion_codigo = :sitioEvaluacion ";
            else
            hql = "UPDATE CriteriohijoHasSitioevaluacion set puntuacion_escala = null where criteriohijo_codigo = :criterioHijo and sitioevaluacion_codigo = :sitioEvaluacion ";                 
            Query query = session.createQuery(hql);
            if ( puntuacion != null )
            query.setInteger("puntuacion", puntuacion);
            query.setInteger("criterioHijo", criterioHijo);
            query.setInteger("sitioEvaluacion", sitioEvaluacion);
            query.executeUpdate();
            session.beginTransaction().commit();
            
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return true;
    }
    
    
}
