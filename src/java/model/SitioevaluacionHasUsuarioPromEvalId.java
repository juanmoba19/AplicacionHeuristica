package model;
// Generated 17-mar-2015 20:14:39 by Hibernate Tools 3.6.0



/**
 * SitioevaluacionHasUsuarioPromEvalId generated by hbm2java
 */
public class SitioevaluacionHasUsuarioPromEvalId  implements java.io.Serializable {


     private int sitioevaluacionCodigo;
     private int usuarioId;

    public SitioevaluacionHasUsuarioPromEvalId() {
    }

    public SitioevaluacionHasUsuarioPromEvalId(int sitioevaluacionCodigo, int usuarioId) {
       this.sitioevaluacionCodigo = sitioevaluacionCodigo;
       this.usuarioId = usuarioId;
    }
   
    public int getSitioevaluacionCodigo() {
        return this.sitioevaluacionCodigo;
    }
    
    public void setSitioevaluacionCodigo(int sitioevaluacionCodigo) {
        this.sitioevaluacionCodigo = sitioevaluacionCodigo;
    }
    public int getUsuarioId() {
        return this.usuarioId;
    }
    
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SitioevaluacionHasUsuarioPromEvalId) ) return false;
		 SitioevaluacionHasUsuarioPromEvalId castOther = ( SitioevaluacionHasUsuarioPromEvalId ) other; 
         
		 return (this.getSitioevaluacionCodigo()==castOther.getSitioevaluacionCodigo())
 && (this.getUsuarioId()==castOther.getUsuarioId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getSitioevaluacionCodigo();
         result = 37 * result + this.getUsuarioId();
         return result;
   }   


}


