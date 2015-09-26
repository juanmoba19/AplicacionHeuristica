/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import Report.EvaluacionDetalladaUsuarioReport;
import Report.PromedioUsuarioReport;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import util.MyUtil;

/**
 *
 * @author Juan Diego
 */
@Named(value = "sitioAnalizarBean")
@SessionScoped
public class sitioAnalizarBean implements Serializable {

    private Sitioevaluacion selectedSitio;
    transient SitioDao sitioDao;
    transient AnalisisHeuristicaDao analisisHeuristicaDao;
    // Model que nos permite crear las graficas de estadistica 
    private BarChartModel barModel;
    // Lista de los usuarios implicados en al evaluacion
    private List<Usuario> listaUsuario;
    // usuario seleccionado del grid
    private Usuario selectedUsuario;
    // Lista de las evaluaciones personalizadas por usuario
    private List<EvaluacionDetalladaUsuarioReport> listaEvalDetalleUsuario;
    // Model Linear chart
    private LineChartModel lineModel2;
    // promedio para saber si es un sitio usable o no
    private Double punSitioConcl;
    // Cantidad de criterios heuristicos tenidos en cuenta
    private Integer cantidadCriteriosEvaluados;
    /**
     * Creates a new instance of sitioAnalizarBean
     */
    
    
    public sitioAnalizarBean() {
        this.sitioDao = new SitioDaoImpl();
        this.analisisHeuristicaDao = new AnalisisHeuristicaDaoImpl();
        this.selectedSitio = new Sitioevaluacion();
        this.selectedUsuario = new Usuario();
        this.punSitioConcl = 0.0;
        this.cantidadCriteriosEvaluados = 0;
    }

    public Sitioevaluacion getSelectedSitio() {
        return selectedSitio;
    }

    public void setSelectedSitio(Sitioevaluacion selectedSitio) {
        this.selectedSitio = selectedSitio;
    }
    
     public void seguirSitio(ActionEvent event) throws IOException {

        String ruta = "";

        Sitioevaluacion sitioevaluacion = this.sitioDao.findBySitio(this.selectedSitio);
        if (sitioevaluacion != null) {
            createBarModels();
            createLineModels();
            ruta = MyUtil.basepathlogin() + "views/estadistica/analisis_sitio.xhtml";
            FacesContext contex = FacesContext.getCurrentInstance();
            contex.getExternalContext().redirect(ruta);
        } else {
            if (this.selectedSitio == null) {
                this.selectedSitio = new Sitioevaluacion();
            }
        }
    }
   
     
    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }
    
    public LineChartModel getLineModel2() {
        return lineModel2;
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
     
     private void createLineModels() {
                
        lineModel2 = initCategoryModel();
        lineModel2.setTitle("Promedio Puntajes Por usuario");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Criterios Heuristicos"));
        Axis yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Puntuacion");
        yAxis.setMin(0);
        yAxis.setMax(5);
    }
     
     private LineChartModel initCategoryModel() {
        Integer idSitio = this.selectedSitio.getCodigo();        
        LineChartModel model = new LineChartModel();
        boolean isAnalisis = analisisHeuristicaDao.isSitioEstadisticaPromPuntajeByCriterioByUsuario(idSitio);
         List<Integer> idsUsuarios = analisisHeuristicaDao.idsUsuariosBySitio(idSitio);
        if(isAnalisis == false){           
            analisisHeuristicaDao.agregarPromHeuristicoByCriteriByUsuario(idSitio, idsUsuarios);
        }
        
         for (Integer idUsuario : idsUsuarios) {
             
             PromedioUsuarioReport usuarioReport = analisisHeuristicaDao.buscarPromHeuristicoByCriteriByUsuario(idSitio, idUsuario);
             ChartSeries boys = new ChartSeries();
             boys.setLabel(usuarioReport.getUsuario());
             boys.set(promCriteriosPadre.CRITERIO1.getCriterioPadre(),usuarioReport.getCriterio1() );
             boys.set(promCriteriosPadre.CRITERIO2.getCriterioPadre(), usuarioReport.getCriterio2());
             boys.set(promCriteriosPadre.CRITERIO3.getCriterioPadre(), usuarioReport.getCriterio3());
             boys.set(promCriteriosPadre.CRITERIO4.getCriterioPadre(), usuarioReport.getCriterio4());
             boys.set(promCriteriosPadre.CRITERIO5.getCriterioPadre(), usuarioReport.getCriterio5());
             boys.set(promCriteriosPadre.CRITERIO6.getCriterioPadre(), usuarioReport.getCriterio6());
             boys.set(promCriteriosPadre.CRITERIO7.getCriterioPadre(), usuarioReport.getCriterio7());
             boys.set(promCriteriosPadre.CRITERIO8.getCriterioPadre(), usuarioReport.getCriterio8());
             boys.set(promCriteriosPadre.CRITERIO9.getCriterioPadre(), usuarioReport.getCriterio9());
             boys.set(promCriteriosPadre.CRITERIO10.getCriterioPadre(), usuarioReport.getCriterio10());
             model.addSeries(boys);
         }
       
         
        return model;
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
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio1();
        if (estadisticaprompuntaje.getCriterio1() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO2.getCriterioPadre(), estadisticaprompuntaje.getCriterio2());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio2();
        if (estadisticaprompuntaje.getCriterio2() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO3.getCriterioPadre(), estadisticaprompuntaje.getCriterio3());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio3();
        if (estadisticaprompuntaje.getCriterio3() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO4.getCriterioPadre(), estadisticaprompuntaje.getCriterio4());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio4();
        if (estadisticaprompuntaje.getCriterio4() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO5.getCriterioPadre(), estadisticaprompuntaje.getCriterio5());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio5();
        if (estadisticaprompuntaje.getCriterio5() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO6.getCriterioPadre(), estadisticaprompuntaje.getCriterio6());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio6();
        if (estadisticaprompuntaje.getCriterio6() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO7.getCriterioPadre(), estadisticaprompuntaje.getCriterio7());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio7();
        if (estadisticaprompuntaje.getCriterio7() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO8.getCriterioPadre(), estadisticaprompuntaje.getCriterio8());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio8();
        if (estadisticaprompuntaje.getCriterio8() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO9.getCriterioPadre(), estadisticaprompuntaje.getCriterio9());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio9();
        if (estadisticaprompuntaje.getCriterio9() != 0){
            cantidadCriteriosEvaluados++;
        }
        criterios.set(promCriteriosPadre.CRITERIO10.getCriterioPadre(),estadisticaprompuntaje.getCriterio10());
        this.punSitioConcl+= estadisticaprompuntaje.getCriterio10();
        if (estadisticaprompuntaje.getCriterio10() != 0){
            cantidadCriteriosEvaluados++;
        }
        model.addSeries(criterios);
        
        this.punSitioConcl = this.punSitioConcl / cantidadCriteriosEvaluados;
        
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
        
        pdf.setPageSize(PageSize.A4);
        pdf.open();
        
        pdf.add(getImage("Uniqlog.PNG"));
        
    }
    
    private Image getImage(String imageName) throws IOException, BadElementException {
        final Image image = Image.getInstance(getAbsolutePath(imageName));
        image.scalePercent(30f);
        image.setWidthPercentage(30f);
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

    public Double getPunSitioConcl() {
        return punSitioConcl;
    }

    public void setPunSitioConcl(Double punSitioConcl) {
        this.punSitioConcl = punSitioConcl;
    }
    
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
         
        HSSFCellStyle cellStyle = wb.createCellStyle();  
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            HSSFCell cell = header.getCell(i);
             
            cell.setCellStyle(cellStyle);
        }
    }
    
     
}
