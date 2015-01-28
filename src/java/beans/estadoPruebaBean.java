/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.EstadoPruebaDao;
import dao.EstadoPruebaDaoImpl;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import model.Estadoprueba;

/**
 *
 * @author Juan Diego
 */
@Named(value = "estadoPruebaBean")
@RequestScoped
public class estadoPruebaBean {

    private List<SelectItem> selectOneItemEstadoPrueba;
    /**
     * Creates a new instance of estadoPruebaBean
     */
    public estadoPruebaBean() {
    }

    public List<SelectItem> getSelectOneItemEstadoPrueba() {
        this.selectOneItemEstadoPrueba = new ArrayList<SelectItem>();
        EstadoPruebaDao estadoPruebaDao = new EstadoPruebaDaoImpl();
        List<Estadoprueba> estados = estadoPruebaDao.selectItems();
        for (Estadoprueba estadoprueba: estados){
            SelectItem selectItem = new SelectItem(estadoprueba.getCodigo(),estadoprueba.getDescripcion());
            this.selectOneItemEstadoPrueba.add(selectItem);
        }
        return selectOneItemEstadoPrueba;        
    }
    
    
}
