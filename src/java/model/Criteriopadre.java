package model;
// Generated 07-ene-2015 13:43:48 by Hibernate Tools 3.6.0


import java.util.HashSet;
import java.util.Set;

/**
 * Criteriopadre generated by hbm2java
 */
public class Criteriopadre  implements java.io.Serializable {


     private Integer codigo;
     private String nombre;
     private String descripcion;
     private Set criteriohijos = new HashSet(0);
     private Set criteriohijoHasSitioevaluacions = new HashSet(0);

    public Criteriopadre() {
    }

    public Criteriopadre(String nombre, String descripcion, Set criteriohijos, Set criteriohijoHasSitioevaluacions) {
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.criteriohijos = criteriohijos;
       this.criteriohijoHasSitioevaluacions = criteriohijoHasSitioevaluacions;
    }
   
    public Integer getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Set getCriteriohijos() {
        return this.criteriohijos;
    }
    
    public void setCriteriohijos(Set criteriohijos) {
        this.criteriohijos = criteriohijos;
    }
    public Set getCriteriohijoHasSitioevaluacions() {
        return this.criteriohijoHasSitioevaluacions;
    }
    
    public void setCriteriohijoHasSitioevaluacions(Set criteriohijoHasSitioevaluacions) {
        this.criteriohijoHasSitioevaluacions = criteriohijoHasSitioevaluacions;
    }




}


