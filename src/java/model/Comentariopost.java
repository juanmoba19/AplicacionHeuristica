package model;
// Generated 23-nov-2014 9:48:59 by Hibernate Tools 3.6.0


import java.util.Date;

/**
 * Comentariopost generated by hbm2java
 */
public class Comentariopost  implements java.io.Serializable {


     private Integer id;
     private Usuario usuario;
     private Post post;
     private String comentario;
     private Date fecha;

    public Comentariopost() {
    }

	
    public Comentariopost(Usuario usuario, Post post) {
        this.id = 0;
        this.usuario = new Usuario();
        this.post = new Post();
    }
    public Comentariopost(Usuario usuario, Post post, String comentario, Date fecha) {
       this.usuario = usuario;
       this.post = post;
       this.comentario = comentario;
       this.fecha = fecha;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Post getPost() {
        return this.post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    public String getComentario() {
        return this.comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }




}


