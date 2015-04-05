/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.util.List;
import model.Comentariopost;
import model.Post;
import model.Sitioevaluacion;

/**
 *
 * @author Juan Diego
 */
public interface PostDao {
    
    public Post findByUsuarioPost(Post post);
    public List<Post> findAll(Integer codigoBySitio);
    public Post findByPost(Post post);
    public List<Comentariopost> findComentariosByPost(Post post);
    public boolean createComentario(Comentariopost comentario);
    public boolean create(Post post);
    public Sitioevaluacion findBySitio(Integer sitioevaluacion);
    public List<Integer> idsSitiosByUsuario(Integer idUsuario);
    public List<Sitioevaluacion> sitiosPermitidos();
}
