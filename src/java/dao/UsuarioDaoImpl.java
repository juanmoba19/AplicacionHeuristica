/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import javax.faces.context.FacesContext;
import model.Usuario;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
public class UsuarioDaoImpl implements UsuarioDao{

    // Usuario en sesion 
    Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj"); 
    
    @Override
    public Usuario findByUsuario(Usuario usuario) {
        
        Usuario model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Usuario u left join fetch u.rol WHERE usuario = '"+usuario.getUsuario()+"'";
        try {
            session.beginTransaction();
            model = (Usuario) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public Usuario login(Usuario usuario) {
        
        Usuario model = this.findByUsuario(usuario);
        if (model != null){
            if(!usuario.getClave().equals(model.getClave())){
                
                return null;
            }
        }
        return model;
    }

    @Override
    public List<Usuario> findAll(Integer num) {
        
        List<Usuario> listado = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql ="";
        if (num == null){
            sql = "FROM Usuario u left join fetch u.rol";
        }else{
            sql = "FROM Usuario u left join fetch u.rol WHERE u.id != :idUsuario ";
        }
         
        try {
            session.beginTransaction();
            Query query = session.createQuery(sql);
            if (num != null){
             query.setParameter("idUsuario", this.usuario.getId());   
            }            
            listado = query.list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return listado;
    }

    @Override
    public boolean create(Usuario usuario) {
        
        boolean flag;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(usuario);
            session.beginTransaction().commit();
            flag=true;
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;
    }

    @Override
    public boolean update(Usuario usuario) {
        
        boolean flag;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Usuario usuariodb = (Usuario) session.load(Usuario.class, usuario.getId());
            usuariodb.setEmail(usuario.getEmail());
            usuariodb.setUsuario(usuario.getUsuario());
            usuariodb.setRol(usuario.getRol());
            usuariodb.setEstado(usuario.getEstado());
            usuariodb.setDescripcion(usuario.getDescripcion());
            session.update(usuariodb);
            session.beginTransaction().commit();
            flag=true;
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;
    }

    @Override
    public boolean delete(Integer id) {
       
        boolean flag;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Usuario usuario = (Usuario) session.load(Usuario.class, id);
            session.delete(usuario);
            session.beginTransaction().commit();
            flag=true;
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;
    }

    @Override
    public Usuario findByContrasena(Usuario usuario) {
        Usuario model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Usuario u WHERE u.clave = '"+usuario.getClave()+"'";
        try {
            session.beginTransaction();
            model = (Usuario) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
    }

    @Override
    public boolean actualizarContrasena(String contraNueva) {
        
        boolean flag = false;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        
        StringBuilder strSql = new StringBuilder();
        strSql.append(" UPDATE usuario set clave = :contraNueva WHERE id = :id ");
        
        try {
            session.beginTransaction();
        SQLQuery query = session.createSQLQuery(strSql.toString());
        query.setParameter("contraNueva", contraNueva);
        query.setParameter("id", usuario.getId());
        query.executeUpdate();
        session.beginTransaction().commit();
        flag = true;
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        
        return flag;
    }
    
}
