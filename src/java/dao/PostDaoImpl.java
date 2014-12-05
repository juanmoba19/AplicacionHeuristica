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
import model.Usuario;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
public class PostDaoImpl implements PostDao{

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
    public List<Post> findAll() {
        
        List<Post> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Post p left join fetch p.usuario order by p.id desc";
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
     public Post findByPost(Post post) {
        
        Post model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Post WHERE id = 1";
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
    
}
