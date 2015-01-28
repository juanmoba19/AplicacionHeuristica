/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import model.Estadoprueba;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author Juan Diego
 */
public class EstadoPruebaDaoImpl implements EstadoPruebaDao{

    @Override
    public List<Estadoprueba> selectItems() {
        
        List<Estadoprueba> listado = null; 
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        String sql =  "FROM Estadoprueba";
         try {
            session.beginTransaction();
            listado = session.createQuery(sql).list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        }
        return listado;
    }
    
}
