/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.io.Serializable;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Sitioevaluacion;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "sitioBean")
@RequestScoped
public class sitioBean {
    
    private String nombre = "PrimeFaces";
    private String url = "www.google.com";
    private Sitioevaluacion selectedUsuario;
    private String tipo = "tipo";
    private Date dateEvaluacion;
    private String imagenProducto;

    /**
     * Creates a new instance of sitioBean
     */
    public sitioBean() {
        this.selectedUsuario = new Sitioevaluacion();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Sitioevaluacion getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Sitioevaluacion selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDateEvaluacion() {
        return dateEvaluacion;
    }

    public void setDateEvaluacion(Date dateEvaluacion) {
        this.dateEvaluacion = dateEvaluacion;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }
    
    /**
     * Metodo para subir la imagen al servidor 
     * @param event 
     */
    public void subirImagen(FileUploadEvent event){
        FacesMessage mensaje = new FacesMessage();
        try {
        selectedUsuario.setRutaImagen(event.getFile().getContents());
        imagenProducto = MyUtil.guardarBlobEnFicheroTemporal(selectedUsuario.getRutaImagen(), event.getFile().getFileName());
        mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
        mensaje.setSummary("Imagen Subida Exitosamente");
        } catch (Exception e) {
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Problemas al subir la imagen");
        }
        FacesContext.getCurrentInstance().addMessage("Mensaje", mensaje);
    }
    
}
