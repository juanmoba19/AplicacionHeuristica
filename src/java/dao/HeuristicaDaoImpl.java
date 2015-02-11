/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import javax.faces.context.FacesContext;
import model.Criteriohijo;
import model.CriteriohijoHasSitioevaluacion;
import model.Criteriopadre;
import model.Sitioevaluacion;
import model.Usuario;
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

    @Override
    public List<Criteriopadre> findAllCriteriosPadre() {
        
        List<Criteriopadre> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Criteriopadre c";
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
    public List<Integer> obtenerIdsCriteriosHijos(Integer idCriterioPadre) {
        
        List<Integer> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "SELECT codigo FROM Criteriohijo WHERE criteriopadre_codigo = :idCriterioPadre";
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            query.setInteger("idCriterioPadre", idCriterioPadre);
            listado = query.list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return listado;
    }
    
    @Override
    public boolean insertarCriteriosSitio(Integer codigoHijo, Integer codigoSitio, Integer padre){
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();  
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj"); 
        Integer codigoUsuario = usuario.getId();
        boolean flag;
        
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("INSERT INTO criteriohijo_has_sitioevaluacion (criteriohijo_codigo, sitioevaluacion_codigo, usuario, criteriopadre_codigo) values (:codigoHijo, :codigoSitio, :codigoUsuario, :padre)");
            query.setInteger("codigoHijo", codigoHijo);
            query.setInteger("codigoSitio", codigoSitio);
            query.setInteger("codigoUsuario", codigoUsuario);
            query.setInteger("padre", padre);
            query.executeUpdate();
            session.beginTransaction().commit();
            flag = true;            
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        
        return flag;
       
    }

    @Override
    public Criteriopadre findByCriterioPadre(Integer idCriterioPadre) {
        
        Criteriopadre model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Criteriopadre WHERE codigo = :idCriterioPadre";
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            query.setInteger("idCriterioPadre", idCriterioPadre);
            model = (Criteriopadre)query.uniqueResult();
            session.beginTransaction().commit();             
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public Criteriohijo findByCriterioHijo(Integer idCriterioHijo) {
     
        Criteriohijo model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Criteriohijo WHERE codigo = :idCriterioHijo";
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            query.setInteger("idCriterioHijo", idCriterioHijo);
            model = (Criteriohijo)query.uniqueResult();
            session.beginTransaction().commit();
           
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public boolean updateEstadoSitio(Integer codigoSitio,  Integer estadoSitio) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String hql = "";
        boolean flag;

        try {
             session.beginTransaction();
             hql = "UPDATE Sitioevaluacion set estadoPrueba_codigo = :estadoSitio WHERE codigo = :codigoSitio";
             Query query = session.createQuery(hql);
             query.setInteger("estadoSitio", estadoSitio);
             query.setInteger("codigoSitio", codigoSitio);
             query.executeUpdate();
             flag = true;
             session.beginTransaction().commit();
             
        }catch(Exception e){
            e.printStackTrace();
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;
    }
    
    
}
