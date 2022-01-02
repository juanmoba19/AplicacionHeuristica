/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;
import model.CriteriohijoHasSitioevaluacion;
import model.Puntuacion;
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
public class criterioSitioTest {
    
    CriteriohijoHasSitioevaluacion criterioSitio;
    @Before
    public void setUp() {
        
        criterioSitio = new CriteriohijoHasSitioevaluacion();
    }
   

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void agregarCriterioSitio() {
    
        criterioSitio.setComentario("puntuacion");
        criterioSitio.setFecha(new Date("2015/02/01"));
        criterioSitio.setFrecuencia("Alta");
        criterioSitio.setPuntuacion(new Puntuacion(2, "bajo", null));
        
        assertEquals(2, criterioSitio.getPuntuacion().getEscala());
        assertEquals("Alta", criterioSitio.getFrecuencia());
    }
}
