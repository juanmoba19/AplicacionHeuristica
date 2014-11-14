/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.RolDao;
import dao.RolDaoImpl;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.model.SelectItem;
import model.Rol;

/**
 *
 * @author Juan Diego
 */
@Named(value = "rolBean")
@RequestScoped
public class rolBean {

    private List<SelectItem> selectOneItemsRol;

    public List<SelectItem> getSelectOneItemsRol() {
            this.selectOneItemsRol = new ArrayList<SelectItem>();
        RolDao rolDao = new RolDaoImpl();
        List<Rol> roles = rolDao.selectItems();
        for (Rol rol : roles) {
            SelectItem selectItem = new SelectItem(rol.getId(), rol.getNombre());
            this.selectOneItemsRol.add(selectItem);
        }
        return selectOneItemsRol;
    }
  
    /**
     * Creates a new instance of rolBean
     */
    public rolBean() {
       
    }
    
}
