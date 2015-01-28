/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import model.Estadoprueba;

/**
 *
 * @author Juan Diego
 */
public interface EstadoPruebaDao {
    
    public List<Estadoprueba> selectItems();    
}
