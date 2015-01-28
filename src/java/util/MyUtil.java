/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author Juan Diego
 */
public class MyUtil {
    
    public static String baseurl(){
        
        return "http://localhost:8080/AplicacionHeuristica/";
    }
    
     public static String basepathlogin(){
        
        return "/AplicacionHeuristica/faces/";
    }
     
     public static String basePath(){
         
         return  "/faces/views/";
     }
     
     /**
      * Permite crear un archivo temporal de la imagen
      * @param bytes
      * @param nombreArchivo
      * @return 
      */
     public static String guardarBlobEnFicheroTemporal(byte[] bytes, String nombreArchivo){
         
        String ubicacionImagen = null;
        ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        
        String path = servletContext.getRealPath("") + File.separatorChar + "resources" + File.separator + "images" + File.separatorChar +
                "sitios" + File.separatorChar + nombreArchivo;
        
        File f = null;
        InputStream in = null;
         
         try {
             f = new File(path);
             in = new ByteArrayInputStream(bytes);
             FileOutputStream out = new FileOutputStream(f.getAbsolutePath());
             
             int c = 0;
             while ((c = in.read()) >= 0) {                 
                 out.write(c);
             }
             out.flush();
             out.close();
             ubicacionImagen = "../../resources/images/sitios/" + nombreArchivo;
         } catch (Exception e) {
             System.err.println("No se puedo cargar la imagen" + e.getMessage());
         }
         
         return ubicacionImagen;
         
     } 
             
            
}
