/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import model.Estadisticaprompuntaje;
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
public class estadisticaPuntajeTest {
    
    Estadisticaprompuntaje estadistica;
    
    @Before
    public void setUp() {
        
        estadistica = new Estadisticaprompuntaje();
    }
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void agregarEstdisticaPromPuntaje() {
    
        estadistica.setCriterio1(12.5);
        estadistica.setCriterio3(2.3);
        estadistica.setIdestadisticaPromPuntaje(5);
        estadistica.setSitioevaluacion(new Sitioevaluacion(new Estadoprueba("Prueba", null)));
        
        assertEquals("Prueba", estadistica.getSitioevaluacion().getEstadoprueba().getDescripcion());
    }
}
