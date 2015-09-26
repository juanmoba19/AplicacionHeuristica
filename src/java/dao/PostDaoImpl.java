/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import javax.faces.context.FacesContext;
import model.Comentariopost;
import model.Post;
import model.Sitioevaluacion;
import model.Usuario;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
public class PostDaoImpl implements PostDao{

    // Usuario en sesion 
    Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj");
    
    @Override
    public Post findByUsuarioPost(Post post) {
       
        Usuario usuario = null;
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        
        Post model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Post WHERE usuario = '"+usuario.getUsuario()+"'";
        try {
            session.beginTransaction();
            model = (Post) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public List<Post> findAll(Integer codigoBySitio) {
        
        List<Integer> idSitios = idsSitiosByUsuario(this.usuario.getId());
        List<Post> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql ="";
        if (codigoBySitio == null){
           sql =  "FROM Post p left join fetch p.usuario u left join fetch p.sitioevaluacion s WHERE s.codigo IN (:idSitios) order by p.id desc";         
        }else{
            sql = "FROM Post p left join fetch p.usuario u left join fetch p.sitioevaluacion s left join fetch p.sitioevaluacion WHERE s.codigo = :codigoBySitio order by p.id desc";
        }
         
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            if(codigoBySitio == null){                
                query.setParameterList("idSitios", idSitios);
            }else{
                query.setParameter("codigoBySitio", codigoBySitio);
            }
            
            listado = query.list();
            
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return listado;
    }
    
     @Override
     public Post findByPost(Post post) {
        
        Post model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Post WHERE id = '"+post.getId()+"'";
        try {
            session.beginTransaction();
            model = (Post) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public List<Comentariopost> findComentariosByPost(Post post) {
                
        List<Comentariopost> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Comentariopost c left join fetch c.usuario WHERE post_id = '"+post.getId()+"'";
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
    public boolean createComentario(Comentariopost comentario) {
       
        boolean flag;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(comentario);
            session.beginTransaction().commit();
            flag=true;
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;
    }
    
    @Override
    public boolean create(Post post) {
        
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj"); 
        
        post.setUsuario(usuario);
        
        boolean flag;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(post);
            session.beginTransaction().commit();
            flag=true;
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;
    }
    
    @Override
    public Sitioevaluacion findBySitio(Integer sitioevaluacion) {
        
        Sitioevaluacion model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Sitioevaluacion WHERE codigo = '"+sitioevaluacion+"'";
          try {
            session.beginTransaction();
            model = (Sitioevaluacion) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
        
    }
    
     public List<Integer> idsSitiosByUsuario(Integer idUsuario) {
        
        if (idUsuario == null || idUsuario < 0){
            return null;
        }
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" SELECT DISTINCT(sitioevaluacion_codigo) idSitio ");
        strSql.append(" FROM criteriohijo_has_sitioevaluacion ");
        strSql.append(" WHERE usuario_id = :idUsuario ");
        
        session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("idUsuario", idUsuario);
        query.addScalar("idSitio",StandardBasicTypes.INTEGER);
        
        return query.list();
    }
     
    public List<Sitioevaluacion> sitiosPermitidos(){
        
        List<Sitioevaluacion> listado = null;
        List<Integer> idSitios = idsSitiosByUsuario(this.usuario.getId());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql = "FROM Sitioevaluacion s left join fetch s.estadoprueba e WHERE s.codigo IN (:idSitios) AND e.codigo != 1 ";
                
        try {
            session.beginTransaction();
             Query query = session.createQuery(sql);
             query.setParameterList("idSitios", idSitios);
            listado = query.list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return listado;
    }

    @Override
    public boolean delete(Integer id) {
        boolean flag;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Comentariopost comentario = (Comentariopost) session.load(Comentariopost.class, id);
            session.delete(comentario);
            session.beginTransaction().commit();
            flag=true;
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;    
    }

}
