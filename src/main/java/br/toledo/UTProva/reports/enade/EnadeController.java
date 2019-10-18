package br.toledo.UTProva.reports.enade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.DocumentException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.sl.usermodel.Background;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartAxis;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.LineChartData;
import org.apache.poi.ss.usermodel.charts.LineChartSeries;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.repository.SimuladoRepository;
import br.toledo.UTProva.model.dao.serviceJDBC.AlunoJDBC;
import br.toledo.UTProva.model.dto.SimuladoDTO;

@RestController
@RequestMapping(value = "/api")
public class EnadeController {

    private static String UPLOAD_DIR = System.getProperty("user.dir");
    public static String getUPLOAD_DIR() {
        return UPLOAD_DIR;
    }

    @Autowired
    EnadeService enadeService;

    @Autowired
    AlunoJDBC alunoJDBC;

    @Autowired
    SimuladoRepository simuladoRepository;

    @GetMapping(value = "/getPlanilhaEnade")
    public ResponseEntity<?> getPlanilhaEnade() {
        try {            
            return new ResponseEntity<>(enadeService.getAllPlanilhasEnade(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/enade/import")
    public ResponseEntity<?> importSpreadsheet(@RequestParam("file") MultipartFile file) throws IOException {
        try {            
            System.out.println(file.getOriginalFilename());
            enadeService.convertExcelToVO(file);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/deletePlanilha/{ano}/{codAread}")
    public ResponseEntity<?> deletePlanilha(@PathVariable("ano") String ano, @PathVariable("codAread") Long codAread) {
        try {
            int delete = enadeService.deletePlanilha(ano, codAread);
            if(delete != -1){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }             
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/relatorio/conceitoEnadeAluno")
    public ResponseEntity<?> geraRelatorio(@RequestBody EnadeReportsVO enadeReportsVO, HttpSession session,HttpServletResponse response)
                                throws DocumentException, MalformedURLException, IOException{
        Map<String, Object> map = new HashMap<>();

        try {
            List<EnadeAlunoVO> enadeAlunoVOs =  alunoJDBC.getAlunoConceitoEnade(enadeReportsVO.getSimulados());

            Random random = new Random();
            String name = "Conceito_enade_por_aluno_"+ random.nextInt(10000)+ ".xlsx";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("sheet-1");

            int rownum = 0;
            int cellnum = 0;

            final CellStyle styleH = sheet1.getWorkbook ().createCellStyle ();
            styleH.setAlignment(HorizontalAlignment.CENTER);            
            styleH.setFillForegroundColor(IndexedColors.ORANGE.getIndex()); 
            styleH.setFillPattern(FillPatternType.SOLID_FOREGROUND);    
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            styleH.setFont(font);

            Row row = sheet1.createRow(0); 
            Cell cell = row.createCell(cellnum++);             
            cell.setCellValue("RA"); 
            cell.setCellStyle(styleH);  
            cell = row.createCell(cellnum++);             
            cell.setCellValue("Nome"); 
            cell.setCellStyle(styleH);  
            for(SimuladoDTO simulado : enadeReportsVO.getSimulados()){ 
                SimuladoEntity simuladoEntity = simuladoRepository.getOne(simulado.getId());
                cell = row.createCell(cellnum++);
                cell.setCellValue("Simulado " + simuladoEntity.getNome());
                cell.setCellStyle(styleH);         
            }          
            rownum++;

            final CellStyle style = sheet1.getWorkbook ().createCellStyle ();
            style.setAlignment(HorizontalAlignment.CENTER);                        
            for(EnadeAlunoVO n : enadeAlunoVOs){
                cellnum = 0;
                row = sheet1.createRow(rownum); 
                if(cellnum == 0){                    
                    cell = row.createCell(cellnum);                 
                    cell.setCellValue(n.getRaAluno());  
                    cell.setCellStyle(style);  
                    cellnum++;                                                 
                }
                if(cellnum == 1){                  
                    cell = row.createCell(cellnum);                 
                    cell.setCellValue(n.getNomeAluno());                                    
                    cellnum++;                                        
                }                
                if(cellnum > 1){
                    for(SimuladoDTO simulado : enadeReportsVO.getSimulados()){                  
                        List<EnadeConceitoVO> enadeConceitoVO = this.enadeService.conceitoEnadeAluno(simulado.getId());
                        int faixa = -1;
                        for(EnadeConceitoVO enade: enadeConceitoVO){                            
                            if(n.getRaAluno().equals(enade.getCod()) && simulado.getId() == enade.getIdSimulado()){                                
                                faixa = enade.getFaixa();                                                            
                            }                  
                        }  
                        if(faixa != -1){
                            cell = row.createCell(cellnum);
                            cell.setCellValue(faixa); 
                            cell.setCellStyle(style);  
                        }else{
                            cell = row.createCell(cellnum);
                            cell.setCellValue("Não Fez"); 
                            cell.setCellStyle(style);  
                        }                         
                        cellnum++;            
                    }                    
                }
                rownum++;
            }
            
            FileOutputStream out = new FileOutputStream(new File(source));
            wb.write(out);
            out.close();
            map.put("status", "success");
            map.put("file", name);

            return new ResponseEntity<>(map, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "error");            
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/relatorio/conceitoEnadeAluno/{nomeRelatorio}")
    public void conceitoEnadeAlunoDowload(@PathVariable("nomeRelatorio") String nomeRelatorio, HttpSession session,HttpServletResponse response)
                                throws DocumentException, MalformedURLException, IOException{
        
        try {                                   
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + nomeRelatorio;           

            // export file
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+nomeRelatorio); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    @PostMapping(value = "/relatorio/conceitoEnade")
    public ResponseEntity<?> conceitoEnade(@RequestBody EnadeReportsVO enadeReportsVO, HttpSession session,HttpServletResponse response)
                                throws DocumentException, MalformedURLException, IOException{
        Map<String, Object> map = new HashMap<>();
        try {
            
            List<EnadeConceitoVO> enadeConceitoVOs = new ArrayList<>();                        
            for(SimuladoDTO n : enadeReportsVO.getSimulados()){                                
                EnadeConceitoVO enadeConceitoVO = this.enadeService.conceitoEnade(n.getId());
                enadeConceitoVOs.add(enadeConceitoVO);
            }

            Random random = new Random();
            String name = "Conceito_enade_por_curso_"+ random.nextInt(10000)+ ".xlsx";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("sheet-1");

            int rownum = 0;
            int cellnum = 0;

            final CellStyle style = sheet1.getWorkbook ().createCellStyle ();
            style.setAlignment(HorizontalAlignment.CENTER);
                
        
            Row row = sheet1.createRow(rownum++); 
            Cell cell = row.createCell(cellnum++); 
            cell.setCellValue("Tipo");
            for(EnadeConceitoVO n : enadeConceitoVOs){
                SimuladoEntity simuladoEntity = simuladoRepository.getOne(n.getIdSimulado());
                cell = row.createCell(cellnum++);
                cell.setCellValue("Simulado " + simuladoEntity.getNome());
                cell.setCellStyle(style);         
            }
           
            //Alimenta a segunda linha do excell
            cellnum = 0;
            row = sheet1.createRow(rownum++); 
            cell = row.createCell(cellnum++); 
            cell.setCellValue("Nota Enade");

            for(EnadeConceitoVO n : enadeConceitoVOs){
                cell = row.createCell(cellnum++);
                cell.setCellValue(n.getFaixa());
                cell.setCellStyle(style);         
            }
            
            
            FileOutputStream out = new FileOutputStream(new File(source));
            wb.write(out);
            out.close();

            //export file
            String filePathToBeServed = source;
            File fileToDownload = new File(source);
            InputStream input = new FileInputStream(fileToDownload);

            XSSFWorkbook book = getExcelFile(input, enadeReportsVO.getSimulados().size());
            FileOutputStream out2 = new FileOutputStream(new File(source));
            book.write(out2);
            out.close();

            

            map.put("status", "success");
            map.put("file", name);

            return new ResponseEntity<>(map, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "error");            
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/relatorio/conceitoEnade/{nomeRelatorio}")
    public void conceitoEnadeDowload(@PathVariable("nomeRelatorio") String nomeRelatorio, HttpSession session,HttpServletResponse response)
                                throws DocumentException, MalformedURLException, IOException{
        
        try {                       
            String name = "Conceito_enade_por_curso.xlsx";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + nomeRelatorio;           

            // export file
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+nomeRelatorio); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @GetMapping(value = "/grafico")
    public ResponseEntity<String> exportExcelGrafico(HttpServletResponse response){

        try{            
            
            String source = getUPLOAD_DIR() + "/reports/enadeSimulado.xlsx";
            File fileToDownload = new File(source);
            InputStream input = new FileInputStream(fileToDownload);
            
            XSSFWorkbook book = getExcelFile(input, 1);
            System.out.println("Peguei o arquivo");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\""
                            + "Gerando Gráfico excel"
                            + ".xlsx\"");

            book.write(response.getOutputStream());
            System.out.println("gerei o arquivo");
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }   
    }

    public XSSFWorkbook getExcelFile(InputStream fileTemplate, int col) throws Exception{

        try{
            XSSFWorkbook book = new XSSFWorkbook(fileTemplate);
            XSSFSheet abaSheet = book.getSheet("sheet-1");
            int numRows = abaSheet.getPhysicalNumberOfRows();

            XSSFDrawing designer = abaSheet.createDrawingPatriarch();
            // criamos a posição do gráfico
            XSSFClientAnchor position = designer.createAnchor(0, 0, 0, 0, 0, numRows+1, 12, numRows +12);
            XSSFChart graphic = designer.createChart(position);
            // aqui definimos aonde serão exibidos as categorias dos gastos
            XSSFChartLegend legend = graphic.getOrCreateLegend();
            legend.setPosition(LegendPosition.BOTTOM);

            // aqui definimos o título que é apresentado dentro do gráfico
            graphic.setTitle("Conceito enade por Curso");
            
            // aqui criamos o tipo do gráfico, no caso um gráfico de linha
            LineChartData data = graphic.getChartDataFactory().createLineChartData();
            
            
            // aqui criamos o eixo y  que exibirá os valores minímos e máximos na horizontal
            ValueAxis yAxis = graphic.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);


            // aqui criamos o eixo x que exibirá os meses do semestre  para marcar os valores no gráfico
            //  de cada categoria 
            ChartAxis xAxis = graphic.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
            yAxis.setMinimum(0);
        
            /*
                Aqui  criamos uma referência para  as categorias através da posição de cada uma delas.
                Como o nome does meses estão presentes na primeira linha da planilha e temos apenas 6 meses,
                definimos um range da linha 0  começando da coluna 1 , até a 6 coluna
            */
            ChartDataSource<String> columns = DataSources.fromStringCellRange(abaSheet, new CellRangeAddress(0, 0, 1, col));

            /*
                Agora iremos , através do contador i utilizado para percorrer as lnnhas,
                 selecionar as linhas das categorias com os dados de cada mes.
            */
            for(int i = 1 ; i < numRows; i++){
                // aqui  criamos referência aos dados de cada linha da categoria de gastos
                // novamente utilizamos um range de colunas de 1 a 6 , pois é onde os gastos de cadas mes estão
                ChartDataSource<Number> valueLinhas = DataSources.fromNumericCellRange(abaSheet, new CellRangeAddress(i, i, 1, col));
                
                /*
                    Nesta linha adicionamos os valores da categoria de gastos , relacionados com os meses definidos
                    na variável columns, e atribuímos a váriavel série uma referência a categoria de dados que acabou de 
                    ser colocada nos dados do gráfico
                */
                LineChartSeries serie = data.addSeries(columns, valueLinhas);
                /*
                 * Aqui criamos referência a célula que contém o título da categoria de gastos e atríbuimos a varíavel de referência 'serie'
                 * Observação : utilizamos a string 'gastos' porque é o nome da aba em que estamos trabalhando,
                 *  se colocarmos nomes nas abas do arquivo , é necessário que criemos a referência da célula,
                 *  com o nome da aba em que ela se encontra
                 */
                CellReference titleSerie = new CellReference("Conceito",i,0,true,true);
                serie.setTitle(titleSerie);
            }

            /*            
                E   por último atribuímos a váriavel gráphic todads as definições de dados do gráfico
                com os eixos definidos.
            */
            graphic.plot(data, new ChartAxis[] { xAxis , yAxis });
            return book;
        }catch(IOException ex){
            System.out.print("Erro ao carregar arquivo :" + ex.getMessage());
            throw ex;
        }
       
    }
}