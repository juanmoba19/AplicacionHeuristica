/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import javax.faces.context.FacesContext;
import model.Post;
import model.Sitioevaluacion;
import model.Usuario;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
public class SitioDaoImpl implements  SitioDao{

    @Override
    public boolean createSitio(Sitioevaluacion sitioEvaluacion) {
        boolean flag;
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj"); 
        Integer codigoUsuario = usuario.getId();
        sitioEvaluacion.setIdUsuarioCreador(codigoUsuario);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(sitioEvaluacion);
            session.beginTransaction().commit();
            flag=true;
        } catch (Exception e) {
            flag = false;
            session.beginTransaction().rollback();
        }
        return flag;
    }

    @Override
    public Sitioevaluacion findBySitio(Sitioevaluacion sitioevaluacion) {
        
        Sitioevaluacion model = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Sitioevaluacion WHERE codigo = '"+sitioevaluacion.getCodigo()+"'";
          try {
            session.beginTransaction();
            model = (Sitioevaluacion) session.createQuery(sql).uniqueResult();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return model;
        
    }
    
}
