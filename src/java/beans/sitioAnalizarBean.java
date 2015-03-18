/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import Report.EvaluacionDetalladaUsuarioReport;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import static com.lowagie.text.pdf.codec.BmpImage.getImage;
import dao.AnalisisHeuristicaDao;
import dao.AnalisisHeuristicaDaoImpl;
import dao.SitioDao;
import dao.SitioDaoImpl;
import enumerator.promCriteriosPadre;
import java.io.File;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import model.Estadisticaprompuntaje;
import model.Sitioevaluacion;
import model.Usuario;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "sitioAnalizarBean")
@SessionScoped
public class sitioAnalizarBean implements Serializable {

    private Sitioevaluacion selectedSitio;
    private SitioDao sitioDao;
    private AnalisisHeuristicaDao analisisHeuristicaDao;
    // Model que nos permite crear las graficas de estadistica 
    private BarChartModel barModel;
    // Lista de los usuarios implicados en al evaluacion
    private List<Usuario> listaUsuario;
    // usuario seleccionado del grid
    private Usuario selectedUsuario;
    // Lista de las evaluaciones personalizadas por usuario
    private List<EvaluacionDetalladaUsuarioReport> listaEvalDetalleUsuario;
    /**
     * Creates a new instance of sitioAnalizarBean
     */
    
    
    public sitioAnalizarBean() {
        this.sitioDao = new SitioDaoImpl();
        this.analisisHeuristicaDao = new AnalisisHeuristicaDaoImpl();
        this.selectedSitio = new Sitioevaluacion();
        this.selectedUsuario = new Usuario();
    }

    public Sitioevaluacion getSelectedSitio() {
        return selectedSitio;
    }

    public void setSelectedSitio(Sitioevaluacion selectedSitio) {
        this.selectedSitio = selectedSitio;
    }
    
     public void seguirSitio(ActionEvent event){
      RequestContext context = RequestContext.getCurrentInstance();
      boolean isRuta;
      String ruta = "";
      
      Sitioevaluacion sitioevaluacion = this.sitioDao.findBySitio(this.selectedSitio);
      if (sitioevaluacion != null){
          isRuta = true;
          createBarModels();
          ruta = MyUtil.basepathlogin()+"views/estadistica/analisis_sitio.xhtml";
      }else{
          isRuta = false;
          if (this.selectedSitio == null ){
              this.selectedSitio = new Sitioevaluacion();
          }
      }
     context.addCallbackParam("isRuta", isRuta);
     context.addCallbackParam("ruta", ruta);   
 }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }
     
     private void createBarModels() {
        createBarModel();
    }
     
     private void createBarModel() {
        barModel = initBarModel();
         
        barModel.setTitle("Promedio Puntajes Evaluacion Heuristica");
        barModel.setLegendPosition("ne");
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Criterios Heuristicos");
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Puntaje");
        yAxis.setMin(0);
        yAxis.setMax(5);
    }
     
     private BarChartModel initBarModel() {
        
        Integer idSitio = this.selectedSitio.getCodigo();
        Estadisticaprompuntaje estadisticaprompuntaje = null; 
        boolean existeAnalisis = analisisHeuristicaDao.isSitioEstadisticaPromPuntaje(idSitio);
        if(existeAnalisis == false){
          analisisHeuristicaDao.agregarPromHeuristicos(idSitio);
        }        
        estadisticaprompuntaje = analisisHeuristicaDao.buscarEstadisticaPromPuntaje(idSitio);
                
        BarChartModel model = new BarChartModel();
        ChartSeries criterios = new ChartSeries();
        criterios.setLabel("Criterio Padre");
        criterios.set(promCriteriosPadre.CRITERIO1.getCriterioPadre(), estadisticaprompuntaje.getCriterio1());
        criterios.set(promCriteriosPadre.CRITERIO2.getCriterioPadre(), estadisticaprompuntaje.getCriterio2());
        criterios.set(promCriteriosPadre.CRITERIO3.getCriterioPadre(), estadisticaprompuntaje.getCriterio3());
        criterios.set(promCriteriosPadre.CRITERIO4.getCriterioPadre(), estadisticaprompuntaje.getCriterio4());
        criterios.set(promCriteriosPadre.CRITERIO5.getCriterioPadre(), estadisticaprompuntaje.getCriterio5());
        criterios.set(promCriteriosPadre.CRITERIO6.getCriterioPadre(), estadisticaprompuntaje.getCriterio6());
        criterios.set(promCriteriosPadre.CRITERIO7.getCriterioPadre(), estadisticaprompuntaje.getCriterio7());
        criterios.set(promCriteriosPadre.CRITERIO8.getCriterioPadre(), estadisticaprompuntaje.getCriterio8());
        criterios.set(promCriteriosPadre.CRITERIO9.getCriterioPadre(), estadisticaprompuntaje.getCriterio9());
        criterios.set(promCriteriosPadre.CRITERIO10.getCriterioPadre(),estadisticaprompuntaje.getCriterio10());
        model.addSeries(criterios);
         
        return model;
    }

    public List<Usuario> getListaUsuario() {
        this.listaUsuario = analisisHeuristicaDao.devolverUsuariosEvaluadores(this.selectedSitio.getCodigo());
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario selectedUsuario) {
        this.selectedUsuario = selectedUsuario;
    }

    public List<EvaluacionDetalladaUsuarioReport> getListaEvalDetalleUsuario() {
        if(this.selectedUsuario.getId() != null && this.selectedSitio.getCodigo() != null)
        this. listaEvalDetalleUsuario = analisisHeuristicaDao.devolverDetallesEvaluaUsiario(this.selectedUsuario.getId(), this.selectedSitio.getCodigo());
        return listaEvalDetalleUsuario;
    }

    public void setListaEvalDetalleUsuario(List<EvaluacionDetalladaUsuarioReport> listaEvalDetalleUsuario) {
        this.listaEvalDetalleUsuario = listaEvalDetalleUsuario;
    }
    
    public void preProcessPDF(Object document) throws IOException, DocumentException {

        final Document pdf = (Document) document;

        HeaderFooter header = new HeaderFooter(new Phrase("Informe Evaluacion Heuristica Usuario: "+ this.selectedUsuario.getUsuario()), false);
        pdf.setHeader(header);
        
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.open();

        PdfPTable pdfTable = new PdfPTable(1);
        pdfTable.addCell(getImage("Uniqlog.png"));
        
        pdfTable.setWidthPercentage(10f);
        pdfTable.setHorizontalAlignment(0);
        pdf.add(pdfTable);

    }
    
    public void postProcessPDF(Object document) throws IOException, DocumentException {
        final Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A4.rotate());

    }
    
    private Image getImage(String imageName) throws IOException, BadElementException {
        final Image image = Image.getInstance(getAbsolutePath(imageName));
        image.scalePercent(90f);
        return image;
    }
    
    private String getAbsolutePath(String imageName) {
        final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        final StringBuilder logo = new StringBuilder().append(servletContext.getRealPath(""));
        logo.append(File.separator).append("resources");
                logo.append(File.separator).append("images");
        logo.append(File.separator).append(imageName);
        return logo.toString();
    }
    
     
}
