/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.HeuristicaDao;
import dao.HeuristicaDaoImpl;
import dao.PostDao;
import dao.PostDaoImpl;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import model.Comentariopost;
import model.Post;
import model.Sitioevaluacion;
import model.Usuario;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "postBean")
@SessionScoped
public class PostBean implements Serializable{

    /**
     * Creates a new instance of PostBean
     */ 
    private List<Post> posts;
    private Post selectedPost;
    transient PostDao postDao;
    transient Comentariopost comentario;
    transient HeuristicaDao heuristicaDao;
    private List<SelectItem> selectOneItemsSitio;
    private int idSitio;
    private String titulo;
    private String contenido;
    // variable para saber si se filtra por sitio
    private Integer codigoBySitio;
    // Variable para saber si el usuario en sesion es el dueño del comentario
    private boolean isDueñoComentario;
    // id del comentario que se eliminara
    private Integer idComentario;

    public Integer getCodigoBySitio() {
        return codigoBySitio;
    }

    public void setCodigoBySitio(Integer codigoBySitio) {
        this.codigoBySitio = codigoBySitio;
    }    
        
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public void setIsDueñoComentario(boolean isDueñoComentario){
        this.isDueñoComentario = isDueñoComentario;
    }
    
    public boolean getIsDueñoComentario(){
        return isDueñoComentario;
    }

    public Integer getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }
    
    
    
    public int getIdSitio() {
        return idSitio;
    }

    public void setIdSitio(int idSitio) {
        this.idSitio = idSitio;
    }
    
    

    public Comentariopost getComentario() {
        return comentario;
    }

    public void setComentario(Comentariopost comentario) {
        this.comentario = comentario;
    }
            
    public PostBean() {
        this.posts = new ArrayList<Post>();
        this.postDao = new PostDaoImpl();
        this.heuristicaDao = new HeuristicaDaoImpl();
        this.comentario = new Comentariopost();
        if(this.selectedPost == null){
            this.selectedPost=new Post();
        }
    }

    public List<Post> getPosts() {
        this.posts = postDao.findAll(codigoBySitio);
        setCodigoBySitio(null);
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Post getSelectedPost() {
        return selectedPost;
    }
    
    public void setSelectedPost(Post selectedPost) {
        this.selectedPost = selectedPost;
    }
    
    public void seguirPost(ActionEvent event) {
        
        String ruta = ""; 
        
        Post po = this.postDao.findByPost(this.selectedPost);
        if(po != null ) {
            ruta = MyUtil.basepathlogin()+"views/foro/post_n.xhtml";
            FacesContext contex = FacesContext.getCurrentInstance();
             try {
                  contex.getExternalContext().redirect(ruta);
             } catch(IOException ex) {
                 Logger.getLogger(PostBean.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else {
            if(this.selectedPost == null){
            this.selectedPost=new Post();
        }
        }
    }
    
    public List<Comentariopost> getComentariosByPost() {
        
        List<Comentariopost> lista = this.postDao.findComentariosByPost(this.selectedPost);
        List<Boolean> isDueño = isDueñoComentario(lista);
        
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setIsDueñoComentario(isDueño.get(i));
        }
        
        return lista;
    }
    
    public void btnDejarComentario(ActionEvent actionEvent) throws ParseException{
        
        PostDao postDao = new PostDaoImpl();
        String msg;               
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = new Date();
        String hoy = sdf.format(date);
        Date actual = sdf.parse(hoy);
        this.comentario.setFecha(actual);
        this.comentario.setPost(this.selectedPost);
        Usuario usuario  = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj");
        this.comentario.setUsuario(usuario);
        if(postDao.createComentario(this.comentario)){
           msg = "Se guardo correctamente el Comentario"; 
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comentario Exitoso", msg);
           FacesContext.getCurrentInstance().addMessage(null, message);
           this.comentario.setComentario("");
        }else{
            msg = "Error al agregar el Comentario";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }         
    }
    
    public void btnCreateForo(Action actionEvent){
        
        String msg;
        
        if ( !this.titulo.equals("") || !this.contenido.equals("") || this.idSitio!=0 ){
          Date hoy = new Date();
        String mes = new SimpleDateFormat("MMMM").format(hoy);
        
        Calendar fechaActual = new GregorianCalendar();
        
        String anio = Integer.toString(fechaActual.get(Calendar.YEAR));
        String dia = Integer.toString(fechaActual.get(Calendar.DAY_OF_MONTH));
        String hora = Integer.toString(fechaActual.get(Calendar.HOUR_OF_DAY));
        String minuto = Integer.toString(fechaActual.get(Calendar.MINUTE));
        String horaMinuto = hora +":" + minuto;
        
        this.selectedPost.setDia(dia);
        this.selectedPost.setMes(mes);
        this.selectedPost.setAnio(anio);
        this.selectedPost.setHora(horaMinuto);
        this.selectedPost.setTitulo(this.titulo);
        this.selectedPost.setContenido(this.contenido);
        
        Sitioevaluacion sitioevaluacion = postDao.findBySitio(this.idSitio);
        this.selectedPost.setSitioevaluacion(sitioevaluacion);
        
        if(postDao.create(this.selectedPost)){
           msg = "Se guardo correctamente la Entrada al Foro"; 
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
           FacesContext.getCurrentInstance().addMessage(null, message);
            setTitulo("");
            setContenido("");
            setIdSitio(0);
        }else{
             setTitulo("");
            setContenido("");
            setIdSitio(0);
            msg = "Error al crear la Entrada al Foro";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }   
        }else{
             setTitulo("");
            setContenido("");
            setIdSitio(0);
           msg = "Por favor llenar todos los Campos Requeridos";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message); 
        }
        
    }

    public List<SelectItem> getSelectOneItemsSitio() {
        this.selectOneItemsSitio = new ArrayList<SelectItem>();
        List<Sitioevaluacion> sitios = this.postDao.sitiosPermitidos();
        if (sitios != null && sitios.size() > 0){
          for (Sitioevaluacion sitioevaluacion : sitios) {
            SelectItem selectItem = new SelectItem(sitioevaluacion.getCodigo(), sitioevaluacion.getNombre());
            this.selectOneItemsSitio.add(selectItem);
        }  
        }
        
         return selectOneItemsSitio;
    }
    
    
     
     public List<Boolean> isDueñoComentario(List<Comentariopost> lista) {
        
        List<Boolean> isDueño = new ArrayList<Boolean>();
        // Usuario en sesion 
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioObj"); 
        for(Comentariopost comentario : lista){
            if(comentario.getUsuario().getId() == usuario.getId()){
                isDueño.add(true);
            }else{
                isDueño.add(false);
            }
        }
        
        return isDueño;
    }
     
    public void eliminarComentario(Action actionEvent){
        
        Integer idComentario = this.idComentario;
        
        String msg;
        if(this.postDao.delete(idComentario)){
           msg = "Se eliminó correctamente el Comentario"; 
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
           FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            msg = "Error al eliminar el comentario";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }     
        
    }
    
}
