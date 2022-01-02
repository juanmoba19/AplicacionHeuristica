/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import Report.CriterioHasSitioEvaluacionReport;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import model.Criteriohijo;
import model.CriteriohijoHasSitioevaluacion;
import model.Criteriopadre;
import model.Sitioevaluacion;
import model.Usuario;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
public class HeuristicaDaoImpl implements HeuristicaDao{
    
    // Usuario en sesion 
    Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj"); 
    
    
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
    public List<Sitioevaluacion> findAll(Integer estadoPrueba) { 
        
        List<Sitioevaluacion> listado = null;
        List<Integer> sitiosPermitidos = sitiosPermitidos();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Integer idUsuario = this.usuario.getId();
        String sql = "";
        if ( estadoPrueba != null && estadoPrueba == 3){
            sql =  "FROM Sitioevaluacion s left join fetch s.estadoprueba e  WHERE (s.codigo IN (:ids) OR s.idUsuarioCreador = :idUsuario) AND e.codigo = :estadoPrueba";
        }else if (sitiosPermitidos != null && sitiosPermitidos.size() >= 1){
            sql =  "FROM Sitioevaluacion s left join fetch s.estadoprueba WHERE s.codigo IN (:ids) OR s.idUsuarioCreador = :idUsuario";
        }else{
            sql =  "FROM Sitioevaluacion s left join fetch s.estadoprueba WHERE s.idUsuarioCreador = :idUsuario";
        }        
        try {
            session.beginTransaction();
             Query query = session.createQuery(sql);
             if (sitiosPermitidos != null && sitiosPermitidos.size() >= 1){
              query.setParameterList("ids",sitiosPermitidos);   
             }
             if ( estadoPrueba != null && estadoPrueba == 3){
                 query.setInteger("estadoPrueba", estadoPrueba);
             }
             query.setInteger("idUsuario", idUsuario);
            listado = query.list();
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
        Integer idUsuario = this.usuario.getId();
        String sql =  "FROM CriteriohijoHasSitioevaluacion chs left join fetch chs.criteriohijo  as ch join fetch  chs.criteriopadre as cp left join fetch chs.id as d WHERE d.sitioevaluacionCodigo = '"+sitioevaluacion.getCodigo()+"' AND chs.usuario.id = '"+idUsuario+"'";
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
    public boolean updateCriterioSitio(Integer puntuacion, Integer criterioHijo, Integer sitioEvaluacion, String comentario, Date fechaActual, String frecuencia) {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String hql="";
        Integer idUsuario = this.usuario.getId();
        
        try {
            
            session.beginTransaction();
            if (  puntuacion != null )
            hql = "UPDATE CriteriohijoHasSitioevaluacion set puntuacion_escala = :puntuacion, comentario = :comentario, fecha = :fechaActual, frecuencia = :frecuencia where criteriohijo_codigo = :criterioHijo and sitioevaluacion_codigo = :sitioEvaluacion and usuario.id = :idUsuario";
            else
            hql = "UPDATE CriteriohijoHasSitioevaluacion set puntuacion_escala = null, comentario = null where criteriohijo_codigo = :criterioHijo and sitioevaluacion_codigo = :sitioEvaluacion and usuario.id = :idUsuario";                 
            Query query = session.createQuery(hql);
            if ( puntuacion != null )
            query.setInteger("puntuacion", puntuacion);
            query.setInteger("criterioHijo", criterioHijo);
            query.setInteger("sitioEvaluacion", sitioEvaluacion);
            query.setString("comentario", comentario);
            query.setInteger("idUsuario", idUsuario);
            query.setDate("fechaActual", fechaActual);
            query.setString("frecuencia", frecuencia);
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
            Query query = session.createSQLQuery("INSERT INTO criteriohijo_has_sitioevaluacion (criteriohijo_codigo, sitioevaluacion_codigo, usuario_id, criteriopadre_codigo) values (:codigoHijo, :codigoSitio, :codigoUsuario, :padre)");
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

    @Override
    public boolean InsertarEvaluadoresColaboradores(Integer codigoHijo, Integer codigoSitio, Integer padre, Integer usuario) {
         
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        boolean flag;
        
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("INSERT INTO criteriohijo_has_sitioevaluacion (criteriohijo_codigo, sitioevaluacion_codigo, usuario_id, criteriopadre_codigo) values (:codigoHijo, :codigoSitio, :codigoUsuario, :padre)");
            query.setInteger("codigoHijo", codigoHijo);
            query.setInteger("codigoSitio", codigoSitio);
            query.setInteger("codigoUsuario", usuario);
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
    public void agregarTotalEvaluadores(Integer codigoSitio, Integer cantidad){
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
       String hql="";

       try {
           
           session.beginTransaction();
           hql = "UPDATE Sitioevaluacion set evaluadoresTotal = :cantidad where codigo = :sitioEvaluacion";          
           Query query = session.createQuery(hql);
           query.setInteger("cantidad", cantidad);
           query.setInteger("sitioEvaluacion", codigoSitio);          
           query.executeUpdate();
           session.beginTransaction().commit();
           
           } catch (Exception e) {
           session.beginTransaction().rollback();
       }
       
    }

    @Override
    public List<Integer> sitiosPermitidos() {
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();         
        Integer codigoUsuario = this.usuario.getId();
        List<Integer> listado = null;
        String sql =  "SELECT distinct sitioevaluacion.codigo FROM CriteriohijoHasSitioevaluacion WHERE usuario_id = '"+codigoUsuario+"'";  
              
        try {
            session.beginTransaction();
            listado = session.createQuery(sql).list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return listado;
       
    }
    
    public boolean cambiarEstadoPrueba(Integer estado, Integer idSitio){
        
       Session session = HibernateUtil.getSessionFactory().getCurrentSession();
       String hql="";

       try {

           session.beginTransaction();
           hql = "UPDATE Sitioevaluacion set estadoPrueba_codigo = :estado where codigo = :sitioEvaluacion";          
           Query query = session.createQuery(hql);
           query.setInteger("estado", estado);
           query.setInteger("sitioEvaluacion", idSitio);          
           query.executeUpdate();
           session.beginTransaction().commit();

       } catch (Exception e) {
           session.beginTransaction().rollback();
       }
       return true;
    }
    
    public boolean isDueñoSitio(Integer idSitio){
        
        Integer idUsuario = this.usuario.getId();
        boolean flag=false;
        Sitioevaluacion model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Sitioevaluacion WHERE codigo = :idSitio and idUsuarioCreador = :idUsuario";
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            query.setInteger("idSitio", idSitio);
            query.setInteger("idUsuario", idUsuario);
            model = (Sitioevaluacion)query.uniqueResult();
            session.beginTransaction().commit();
            if( model != null ){
                flag=true;
            }else{
                flag=false;
            }
           
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        
        return flag;
        
    }

    @Override
    public List<CriterioHasSitioEvaluacionReport> devolverCriteriosAEvaluar(Integer codigoSitioEvaluacion) {
        
        if (codigoSitioEvaluacion == null || codigoSitioEvaluacion < 0){
            return null;
        }
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Integer idUsuario = this.usuario.getId();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT chs.criteriopadre_codigo AS codigoPadre, ");
        strSql.append(" cp.nombre AS descripcionPadre, ");
        strSql.append(" chs.criteriohijo_codigo AS codigoHijo, ");
        strSql.append(" ch.descripcion AS descripcionHijo, ");
        strSql.append(" chs.puntuacion_escala AS puntuacion, ");
        strSql.append(" chs.comentario AS comentario, ");
        strSql.append(" chs.sitioevaluacion_codigo AS codigoSitio, ");
        strSql.append(" chs.frecuencia AS frecuencia ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion chs ");
        strSql.append(" INNER JOIN criteriopadre cp ON chs.criteriopadre_codigo = cp.codigo ");
        strSql.append(" INNER JOIN criteriohijo ch ON chs.criteriohijo_codigo = ch.codigo ");
        strSql.append(" WHERE chs.sitioevaluacion_codigo = :codigoSitioEvaluacion ");
        strSql.append(" AND chs.usuario_id = :idUsuario ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setResultTransformer(Transformers.aliasToBean(CriterioHasSitioEvaluacionReport.class));
        
        query.setParameter("codigoSitioEvaluacion", codigoSitioEvaluacion);
        query.setParameter("idUsuario", idUsuario);
        
        query.addScalar("codigoPadre",StandardBasicTypes.INTEGER);
        query.addScalar("descripcionPadre",StandardBasicTypes.STRING);
        query.addScalar("codigoHijo",StandardBasicTypes.INTEGER);
        query.addScalar("descripcionHijo",StandardBasicTypes.STRING);
        query.addScalar("puntuacion",StandardBasicTypes.INTEGER);
        query.addScalar("comentario",StandardBasicTypes.STRING);
        query.addScalar("codigoSitio",StandardBasicTypes.INTEGER);
        query.addScalar("frecuencia",StandardBasicTypes.STRING);
        
        return query.list();
    }

    @Override
    public Integer consultarTotalEvaluadores(Integer codigoSitio) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT evaluadoresTotal AS total ");
        strSql.append(" FROM sitioevaluacion ");
        strSql.append(" WHERE codigo = :idSitio ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("idSitio", codigoSitio);
        query.addScalar("total",StandardBasicTypes.INTEGER);
        
        Integer result = (Integer) query.uniqueResult();
        if ( result == null)
        result = 0;
        
        return result;
    }

    @Override
    public Integer consultarEvaluadoresParcial(Integer codigoSitio) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT evaluadoresConfirm AS total ");
        strSql.append(" FROM sitioevaluacion ");
        strSql.append(" WHERE codigo = :idSitio ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("idSitio", codigoSitio);
        query.addScalar("total",StandardBasicTypes.INTEGER);
        
        Integer result = (Integer) query.uniqueResult();
        if ( result == null)
        result = 0;
        
        return result;
    }

    @Override
    public void agregarEvaluadorParcial(Integer codigoSitio, Integer cantidad) {
       
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
       String hql="";

       try {
           
           session.beginTransaction();
           hql = "UPDATE Sitioevaluacion set evaluadoresConfirm = :cantidad where codigo = :sitioEvaluacion";          
           Query query = session.createQuery(hql);
           query.setInteger("cantidad", cantidad);
           query.setInteger("sitioEvaluacion", codigoSitio);          
           query.executeUpdate();
           session.beginTransaction().commit();
           
           } catch (Exception e) {
           session.beginTransaction().rollback();
       }
    }
    
}
