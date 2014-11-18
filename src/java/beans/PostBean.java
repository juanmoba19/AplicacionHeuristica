/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import dao.PostDao;
import dao.PostDaoImpl;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import model.Post;

/**
 *
 * @author Juan Diego
 */
@Named(value = "postBean")
@RequestScoped
public class PostBean {

    /**
     * Creates a new instance of PostBean
     */
    private List<Post> posts;
    private Post selectedPost;
    
    public PostBean() {
        this.posts = new ArrayList<Post>();
        this.selectedPost = new Post();
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
}
