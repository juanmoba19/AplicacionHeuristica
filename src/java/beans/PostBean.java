/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.PostDao;
import dao.PostDaoImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import model.Post;
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
    private String eje;

    public String getEje() {
        return eje;
    }

    public void setEje(String eje) {
        this.eje = eje;
    }
    
    public PostBean() {
        this.posts = new ArrayList<Post>();
        this.postDao = new PostDaoImpl();
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
    
   
}
