/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Juan Diego
 */
@Named(value = "post_nBean")
@RequestScoped
public class Post_nBean {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * Creates a new instance of Post_nBean
     */
    @ManagedProperty("#{postBean}")
    private PostBean postBean;

    public PostBean getPostBean() {
        return postBean;
    }

    public void setPostBean(PostBean postBean) {
        this.postBean = postBean;
    }
    
    public Post_nBean() {
    }
    
}
