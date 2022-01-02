/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import beans.PostBean;
import model.Post;
import model.Sitioevaluacion;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jdmolina
 */
public class postTest {
    
    Post post;
    
  
    
    @Before
    public void setUp() {
        post = new Post();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void agregarPost() {
     
        post.setAnio("2015");
        post.setContenido("Que opina");
        post.setSitioevaluacion(new Sitioevaluacion(null, "google", "www.google.com", "buscador", null, null, null, null, "Sitio busqueda"));
        
        assertEquals("www.google.com", post.getSitioevaluacion().getUrl());
    }
}
