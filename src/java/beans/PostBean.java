/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.PostDao;
import dao.PostDaoImpl;
import java.awt.Desktop.Action;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.Comentariopost;
import model.Post;
import model.Usuario;
import org.primefaces.context.RequestContext;
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
    private PostDao postDao;
    private Comentariopost comentario;

    public Comentariopost getComentario() {
        return comentario;
    }

    public void setComentario(Comentariopost comentario) {
        this.comentario = comentario;
    }
            
    public PostBean() {
        this.posts = new ArrayList<Post>();
        this.postDao = new PostDaoImpl();
        this.comentario = new Comentariopost();
        if(this.selectedPost == null){
            this.selectedPost=new Post();
        }
    }

    public List<Post> getPosts() {
        PostDao postDao = new PostDaoImpl();
        this.posts = postDao.findAll();
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
        RequestContext context = RequestContext.getCurrentInstance();
        boolean isPost;
        String ruta = ""; 
        
        Post po = this.postDao.findByPost(this.selectedPost);
        if(po != null ) {
            isPost = true;
            ruta = MyUtil.basepathlogin()+"views/foro/post_n.xhtml";
        } else {
            isPost = false;
            if(this.selectedPost == null){
            this.selectedPost=new Post();
        }
        }
        context.addCallbackParam("isPost", isPost);
        context.addCallbackParam("ruta", ruta);
    }
    
    public List<Comentariopost> getComentariosByPost() {
        
        List<Comentariopost> lista = this.postDao.findComentariosByPost(this.selectedPost);
        
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
        
        if(postDao.create(this.selectedPost)){
           msg = "Se guardo correctamente la Entrada al Foro"; 
           FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
           FacesContext.getCurrentInstance().addMessage(null, message);
           this.selectedPost = null;
        }else{
            msg = "Error al crear la Entrada al Foro";
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } 
    }
          
}
