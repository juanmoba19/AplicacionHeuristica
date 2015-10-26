/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.UsuarioDao;
import dao.UsuarioDaoImpl;
import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Usuario;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "usuarioBean")
@RequestScoped
public class usuarioBean {

    /**
     * Creates a new instance of usuarioBean
     */
    private List<Usuario> usuarios;
    private Usuario selectedUsuario;
    private String contraActual;
    private String contraNueva;
    private String contraConfNueva;
    
    
    public usuarioBean() {
        
        this.usuarios = new ArrayList<Usuario>();
        this.selectedUsuario = new Usuario();
    }

    public List<Usuario> getUsuarios() {
        
        UsuarioDao usuarioDao = new UsuarioDaoImpl();
        this.usuarios = usuarioDao.findAll(null);
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }
    
    public void btnCreateUsuario(Action actionEvent){
        
        UsuarioDao usuarioDao = new UsuarioDaoImpl();
        String msg;
        this.selectedUsuario.setClave(this.selectedUsuario.getUsuario());
        this.selectedUsuario.setUsuariocreacion("admin");        
        Date hoy = new Date();
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(hoy);
        this.selectedUsuario.setFechacreacion(java.sql.Date.valueOf(fecha));
        if(usuarioDao.create(this.selectedUsuario)){
           msg = "Se guardo correctamente el Usuario"; 
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
           FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            msg = "Error al crear el Usuario";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } 
    }
    
    public void btnUpdateUsuario(Action actionEvent){
        
        UsuarioDao usuarioDao = new UsuarioDaoImpl();
        String msg;
        if(usuarioDao.update(this.selectedUsuario)){
           msg = "Se modificó correctamente el Usuario"; 
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message); 
        }else{
            msg = "Error al modificar el Usuario";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    public void btnDeleteUsuario(Action actionEvent){
        
        UsuarioDao usuarioDao = new UsuarioDaoImpl();
        String msg;
        if(usuarioDao.delete(this.selectedUsuario.getId())){
           msg = "Se eliminó correctamente el Usuario"; 
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
           FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            msg = "Error al eliminar el Usuario";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }      
        
    }

    public String getContraActual() {
        return contraActual;
    }

    public void setContraActual(String contraActual) {
        this.contraActual = contraActual;
    }

    public String getContraNueva() {
        return contraNueva;
    }

    public void setContraNueva(String contraNueva) {
        this.contraNueva = contraNueva;
    }

    public String getContraConfNueva() {
        return contraConfNueva;
    }

    public void setContraConfNueva(String contraConfNueva) {
        this.contraConfNueva = contraConfNueva;
    }
    
    public void cambiarContrasena(ActionEvent event){
        
        UsuarioDao usuarioDao = new UsuarioDaoImpl();
        String msg = "";
        Usuario usuario = new Usuario();
        usuario.setClave(contraActual);
        FacesContext contex = FacesContext.getCurrentInstance();
        
        if( usuarioDao.findByContrasena(usuario) != null ){
            
            if(contraNueva.equals(contraConfNueva)){
                
                if(usuarioDao.actualizarContrasena(contraNueva)){
                    msg = "Se Cambio correctamente la contraseña";
                }
            }else {
                    msg = "No coincide la contraseña nueva con la confirmacin"; 
            }
        }else{
                    msg = "La contraseña antigua no es correcta";
        }
        
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
         FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
}
