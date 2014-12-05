/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Usuario;
import org.primefaces.context.RequestContext;
import dao.UsuarioDao;
import dao.UsuarioDaoImpl;
import javax.servlet.http.HttpSession;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value="loginBean")
@SessionScoped
public class loginBean implements Serializable{

    private Usuario usuario;
    private UsuarioDao usuarioDao;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    /**
     * Creates a new instance of loginBean
     */
    public loginBean() {
        this.usuarioDao = new UsuarioDaoImpl();
        if(this.usuario == null){
            this.usuario=new Usuario();
        }
    }
    
    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message;
        boolean loggedIn;
        String ruta = ""; 
        
        this.usuario=this.usuarioDao.login(this.usuario);
        if(this.usuario != null ) {
            loggedIn = true;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", this.usuario.getUsuario());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioObj", this.usuario);
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", this.usuario.getUsuario());
            ruta = MyUtil.basepathlogin()+"views/inicio.xhtml";
        } else {
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Usuario Incorrecto");
            if(this.usuario == null){
            this.usuario=new Usuario();
        }
        }
         
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
        context.addCallbackParam("ruta", ruta);
    }
    
    public void logout(){
        
        String ruta = MyUtil.basepathlogin()+"login.xhtml";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.invalidate();
        
        context.addCallbackParam("loggetOut", true);
        context.addCallbackParam("ruta", ruta);
    }
            
    
}
