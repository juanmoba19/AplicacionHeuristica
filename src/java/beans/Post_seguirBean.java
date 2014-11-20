/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import model.Post;

/**
 *
 * @author Juan Diego
 */
@Named(value = "post_seguirBean")
@RequestScoped
public class Post_seguirBean {

    /**
     * Creates a new instance of Post_seguirBean
     */
    Post post;

    public Post getPost() {
       PostBean postBean = (PostBean) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(),
			"#{postBean}", PostBean.class);
       this.post = postBean.getSelectedPost();
       return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
    public Post_seguirBean() {
        if(this.post == null){
            this.post=new Post();
        }
    }
    
}
