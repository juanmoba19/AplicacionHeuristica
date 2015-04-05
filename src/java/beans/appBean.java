/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import model.Usuario;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@ManagedBean
@ApplicationScoped
public class appBean {

    // rol en sesion para los permisos
    private int rol;

    public int getRol() {
        this.rol =  (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("rol");
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    /**
     * Creates a new instance of appBean
     */
    public appBean() {
        
    }
    
    public String getBaseUrl(){
        
        return MyUtil.baseurl();
    }
    
    public String getBasePath(){
        
        return MyUtil.basePath();
    }
            
}
