/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import model.Estadoprueba;
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
public class sitioEvaluacionTest {

    Sitioevaluacion sitio;
    
    @Before
    public void setUp() {
        sitio = new Sitioevaluacion();
    }
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void agregarSitioEvaluacion() {
        
        sitio.setCodigo(123);
        sitio.setDescripcion("Sitio para");
        sitio.setEstadoprueba(new Estadoprueba("ejecucion", null));
        sitio.setIdUsuarioCreador(1);
        sitio.setTipo("ayuda");
        
        assertEquals("ejecucion", sitio.getEstadoprueba().getDescripcion());
        assertEquals("ayuda", sitio.getTipo());
    }
}
