/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package enumerator;

/**
 *
 * @author Juan Diego
 */
public enum promCriteriosPadre {
    
CRITERIO1("Crto 1","Entendibilidad-Facilidad"),
CRITERIO2("Crto 2","Identidad e informacion"),
CRITERIO3("Crto 3","Aspectos Generales"),
CRITERIO4("Crto 4","Estructura-Navegacion"),
CRITERIO5("Crto 5","Rotulado"),
CRITERIO6("Crto 6","Layout"),
CRITERIO7("Crto 7","Retroalimentacion"),
CRITERIO8("Crto 8","Multimedia"),
CRITERIO9("Crto 9","Busqueda"),
CRITERIO10("Crto 10","Ayuda");

private final String criterioPadre;
private final String descripcion;

private promCriteriosPadre(String criterioPadre, String descripcion) {
    this.criterioPadre = criterioPadre;
    this.descripcion = descripcion;
}

    public String getCriterioPadre() {
        return criterioPadre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
}
