/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import model.Usuario;
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
public class usuarioTest {
    
    Usuario usuario;
    
    @Before
    public void setUp() {
         usuario = new Usuario();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void agregarUsuario() {
        usuario.setUsuario("Juan");
        usuario.setClave("asd123");
        usuario.setDescripcion("Experto"); 
        
        assertEquals("Juan", usuario.getUsuario());
        assertEquals("asd123", usuario.getClave());
        assertEquals("Experto", usuario.getDescripcion());
    }
}
