/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;
import dao.SitioDao;
import dao.SitioDaoImpl;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.Sitioevaluacion;
import org.primefaces.event.FileUploadEvent;
import org.springframework.util.StringUtils;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "sitioModificarBean")
@SessionScoped 
public class sitioModificarBean implements Serializable{

    private Sitioevaluacion selectedSitio;
    private Date dateEvaluacion;
    private String imagenProducto;
    transient SitioDao sitioDao;
    private Map<String, String> tipoSitio;

    /**
     * Creates a new instance of sitioBean
     */
    public sitioModificarBean() {     
        
        this.selectedSitio = new Sitioevaluacion();
        sitioDao = new SitioDaoImpl();
        this.selectedSitio.setNombre("Click para Ingresar Nombre Sitio");
        this.selectedSitio.setUrl("Click para escribir Url");
        this.selectedSitio.setTipo("Click para seleccionar el tipo");
        tipoSitio = new HashMap<String, String>();
        tipoSitio.put("Blog", "Blog");
        tipoSitio.put("Comercio Electrónico", "Comercio Electrónico");
        tipoSitio.put("Descargas", "Descargas");
        tipoSitio.put("Desarrollo","Desarrollo");
        tipoSitio.put("Instituacional", "Instituacional");
        tipoSitio.put("Comunidad Virtual", "Comunidad Virtual");
        tipoSitio.put("Información", "Información");
        tipoSitio.put("Personal", "Personal");
        tipoSitio.put("Wiki", "Wiki");
        tipoSitio.put("Educativo", "Educativo");
        tipoSitio.put("Portal", "Portal");
        tipoSitio.put("Subasta", "Subasta");
        tipoSitio.put("Spam", "Spam");
         HashMap mapResultado = new LinkedHashMap();
        List misMapKeys = new ArrayList(tipoSitio.keySet());
        List misMapValues = new ArrayList(tipoSitio.values());
        TreeSet conjuntoOrdenado = new TreeSet(misMapValues);
        Object[] arrayOrdenado = conjuntoOrdenado.toArray();
        int size = arrayOrdenado.length;
        for (int i = 0; i < size; i++) {
            mapResultado.put(misMapKeys.get(
                    misMapValues.indexOf(
                            arrayOrdenado[i])
            ), arrayOrdenado[i]);
        }
        this.tipoSitio = mapResultado;
    }
    
    public Map<String, String> getTipoSitio() {
        return tipoSitio;
    }

    public void setTipoSitio(Map<String, String> tipoSitio) {
        this.tipoSitio = tipoSitio;
    }

    public Sitioevaluacion getSelectedSitio() {
        return selectedSitio;
    }

    public void setSelectedSitio(Sitioevaluacion selectedUsuario) {
        this.selectedSitio = selectedUsuario;
    }

    public Date getDateEvaluacion() {
        return dateEvaluacion;
    }

    public void setDateEvaluacion(Date dateEvaluacion) {
        this.dateEvaluacion = dateEvaluacion;
    }

    public String getImagenProducto() {
        if(!StringUtils.isEmpty(this.selectedSitio.getRutaImagen()))
        this.imagenProducto = MyUtil.guardarBlobEnFicheroTemporal(this.selectedSitio.getRutaImagen(), this.selectedSitio.getNombreImagen());
        return this.imagenProducto;
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
        this.selectedSitio.setRutaImagen(event.getFile().getContents());
        this.selectedSitio.setNombreImagen(event.getFile().getFileName());
        imagenProducto = MyUtil.guardarBlobEnFicheroTemporal(this.selectedSitio.getRutaImagen(), event.getFile().getFileName());
        mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
        mensaje.setSummary("Imagen Subida Exitosamente");
        } catch (Exception e) {
            mensaje.setSeverity(FacesMessage.SEVERITY_ERROR);
            mensaje.setSummary("Problemas al subir la imagen");
        }
        FacesContext.getCurrentInstance().addMessage("Mensaje", mensaje);
    }
    
    public void btnModificarSitio(Action actionEvent) throws IOException{
        
        String msg;
        if (this.sitioDao.actualizarSitio(this.selectedSitio)){
            msg = "Se modifico correctamente el Sitio";
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
           FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            msg = "Error al modificar el Usuario";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }  
         ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/views/heuristica/index.xhtml");
    }
    
    public void seguirSitio(ActionEvent event){
         String ruta = "";
         
         Sitioevaluacion sitioevaluacion = this.sitioDao.findBySitio(this.selectedSitio);
         if (sitioevaluacion != null){
             this.imagenProducto = "";
             ruta = MyUtil.basepathlogin()+"views/heuristica/modificar_sitio.xhtml";
             FacesContext contex = FacesContext.getCurrentInstance();
             try {
                  contex.getExternalContext().redirect(ruta);
             } catch(IOException ex) {
                 Logger.getLogger(sitioModificarBean.class.getName()).log(Level.SEVERE, null, ex);
             }
            
         }else{
             if (this.selectedSitio == null ){
                 this.selectedSitio = new Sitioevaluacion();
         } 
    }
    }
    
    public void seguirForoBySitio(ActionEvent event){
         String ruta = "";
         
             ruta = MyUtil.basepathlogin()+"views/foro/index.xhtml";
             FacesContext contex = FacesContext.getCurrentInstance();
             try {
                  contex.getExternalContext().redirect(ruta);
             } catch(IOException ex) {
                 Logger.getLogger(sitioModificarBean.class.getName()).log(Level.SEVERE, null, ex);
             }
            
    }  
    
}
