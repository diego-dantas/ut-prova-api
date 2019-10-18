package br.toledo.UTProva.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.repository.QuestaoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoRepository;
import br.toledo.UTProva.reports.dto.AcertosDetalhado;
import br.toledo.UTProva.reports.dto.AreaConhecimentoReports;
import br.toledo.UTProva.reports.dto.ConteudoReports;
import br.toledo.UTProva.reports.dto.DetalhadoReports;
import br.toledo.UTProva.reports.dto.GrupoCadastro;
import br.toledo.UTProva.reports.dto.HabilidadesReports;
import br.toledo.UTProva.reports.dto.PercentualDeAcerto;
import br.toledo.UTProva.reports.dto.PercentualDeAcertoCSV;
import br.toledo.UTProva.reports.repository.GrupoCadastroRepository;
import br.toledo.UTProva.reports.repository.PercentualDeAcertoRepository;
import br.toledo.UTProva.reports.repository.ReportsDetalhadoRepository;

@RestController
@RequestMapping(value = "/api/")
public class RelatorioController {

    @Autowired
    private PercentualDeAcertoRepository percentualDeAcertoRepository;
    @Autowired
    private GrupoCadastroRepository grupoCadastroRepository;
    @Autowired
    private ReportsDetalhadoRepository reportsDetalhadoRepository;
    @Autowired 
    private SimuladoRepository simuladoRepository;
    @Autowired
    private QuestaoRepository questaoRepository;

    private static String UPLOAD_DIR = System.getProperty("user.dir");
    public static String getUPLOAD_DIR() {
        return UPLOAD_DIR;
    }

    //Retorna o JSON do relatorio percentual de acerto
    @GetMapping(value = "/percentualDeAcerto/{idSimulado}/{tipo}")
    public ResponseEntity<PercentualDeAcertoCSV> reports(@PathVariable("idSimulado") Long idSimulado,
            @PathVariable("tipo") Long tipo) {

        try {
            return new ResponseEntity<>(percentualDeAcertoRepository.reportsCSV(idSimulado, tipo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }

    }

    //Gerar relatório percentual de acerto PDF 
    @GetMapping(value = "/percentualDeAcertoPDF/{idSimulado}/{tipo}")
    public void percentualDeAcertoPDF(@PathVariable("idSimulado") Long idSimulado,
            @PathVariable("tipo") Long tipo, HttpSession session,HttpServletResponse response) {

        try {
            Random random = new Random();
            String name = "Percentual_de_acerto_" + random.nextInt(1000) + ".pdf";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;


            Image imgSoc = Image.getInstance(getUPLOAD_DIR() + "/imgs/unitoledo.png");
            imgSoc.scaleToFit(100,100);

            Document document = new Document(PageSize.A4.rotate());
    
            PdfWriter.getInstance(document, new FileOutputStream(source));
            document.open();
            Font f = new Font(Font.FontFamily.COURIER, 15, Font.BOLD);

            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
            String titulo = "Taxa de Acerto por Aluno \n" + simulado.getNome();
            // if(tipo == 1) titulo = "Taxa de Acerto por Aluno \nFormação Geral | Simulado " + idSimulado;
            // if(tipo == 2) titulo = "Conhecimento Especifico | Simulado " + idSimulado;
            // if(tipo == 3) titulo = "Visão Geral | Simulado " + idSimulado;

            Paragraph paragraph = new Paragraph(titulo, f);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(imgSoc);
            paragraph.setSpacingAfter(20.0f);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(3);

            table.setWidthPercentage(100);
            table.setHorizontalAlignment(0);
            table.setSpacingAfter(10);
                
            
            Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK); 
                
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.ORANGE);
            cell.setPadding(1);
            cell.setBorder(1);

           
            
            PercentualDeAcertoCSV percentualDeAcertoCSV = percentualDeAcertoRepository.reportsCSV(idSimulado, tipo);
            cell.setPhrase(new Phrase("Media geral", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell.setPhrase(new Phrase(""));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell.setPhrase(new Phrase(String.valueOf(percentualDeAcertoCSV.getMediaSimulado()) + "%", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell.setPhrase(new Phrase("ID", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Nome do Aluno", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Percentual", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            Font font2 = FontFactory.getFont(FontFactory.COURIER, 10); 
                
            PdfPCell cell2 = new PdfPCell(imgSoc, true);
            cell2.setPadding(2);
            cell2.setBorder(1);

           
            percentualDeAcertoCSV.getAlunos().forEach(n -> {
                cell2.setPhrase(new Phrase(n.getIdAluno(), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(n.getNomeAluno(), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(String.valueOf(n.getTaxaAcerto()) + "%", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
            });


            document.add(table);
            document.close();

            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+name); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @GetMapping(value = "/percentualDeAcertoDownload/{idSimulado}/{tipo}")
    public void percentualDeAcertoDownload(HttpServletResponse response, @PathVariable("idSimulado") Long idSimulado,
            @PathVariable("tipo") Long tipo)
            throws IOException{

        try {
            String name = "";
            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
            if (tipo == 1) {
                name = "Percentual-acerto-FORMAÇÃO_GERAL_" + simulado.getNome().replace(" ", "_").replace("/", "-") + ".xlsx";
            } else if (tipo == 2) {
                name = "Percentual-acerto-CONHECIMENTO-ESPECÍFICO" + simulado.getNome().replace(" ", "_").replace("/", "-") + ".xlsx";
            } else {
                name = "Percentual-acerto-GERAL" + simulado.getNome().replace(" ", "_").replace("/", "-") + ".xlsx";
            }

        
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;

                
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("sheet 1");

            int rownum = 0;
            int cellnum = 0;
                
            Row row = sheet1.createRow(rownum++); 
                                
            Cell cell = row.createCell(cellnum++);                                
            cell.setCellValue("RA");
            cell = row.createCell(cellnum++);                                
            cell.setCellValue("Nome do Aluno");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Percentual");  
            
            PercentualDeAcertoCSV percentualDeAcertoCSV = percentualDeAcertoRepository.reportsCSV(idSimulado, tipo);
            for(PercentualDeAcerto n : percentualDeAcertoCSV.getAlunos()){
                cellnum = 0;
                              
                row = sheet1.createRow(rownum++); 
                                
                Cell cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(n.getIdAluno());
                cellItem = row.createCell(cellnum++);   
                cellItem.setCellValue(n.getNomeAluno());
                cellItem = row.createCell(cellnum++);   
                cellItem.setCellValue(n.getTaxaAcerto());
            }

            FileOutputStream out = new FileOutputStream(new File(source));
            wb.write(out);
            out.close();

            // export file
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+name); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
            
        } catch (Exception e) {
            //TODO: handle exception
        }

       


    }

    @GetMapping(value = "/relatorioHabilidadeEConteudo/{idSimulado}")
    public ResponseEntity<GrupoCadastro> reports1(@PathVariable("idSimulado") Long idSimulado) {
        try {
            return new ResponseEntity<>(grupoCadastroRepository.repoGrupoCadastro(idSimulado), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/relatorioHabilidadeEConteudoPDF/{idSimulado}")
    public void relatorioHabilidadeEConteudoPDF(@PathVariable("idSimulado") Long idSimulado, HttpSession session,HttpServletResponse response) {
        try {
            Random random = new Random();
            String name = "habilidades_conteudos_" + random.nextInt(1000) + ".pdf";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;


            Image imgSoc = Image.getInstance(getUPLOAD_DIR() + "/imgs/unitoledo.png");
            imgSoc.scaleToFit(100,100);

            Document document = new Document(PageSize.A4.rotate());
    
            PdfWriter.getInstance(document, new FileOutputStream(source));
            document.open();
            Font f = new Font(Font.FontFamily.COURIER, 15, Font.BOLD);
            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
            Paragraph paragraph = new Paragraph("Taxa de Acerto por Área de Conhecimento, Habilidade e Conteúdo \n" + simulado.getNome(), f);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(imgSoc);
            paragraph.setSpacingAfter(20.0f);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(3);

            table.setWidthPercentage(100);
            table.setHorizontalAlignment(0);
            table.setSpacingAfter(10);
                
            
            Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK); 
                
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.ORANGE);
            cell.setPadding(1);
            cell.setBorder(1);


            cell.setPhrase(new Phrase("TIPO", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Descrição", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Percentual", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);


            Font font2 = FontFactory.getFont(FontFactory.COURIER, 10); 
                
            PdfPCell cell2 = new PdfPCell(imgSoc, true);
            cell2.setPadding(2);
            cell2.setBorder(1);


            GrupoCadastro grupoCadastro = grupoCadastroRepository.repoGrupoCadastro(idSimulado);

            grupoCadastro.getAreaConhecimento().forEach(n -> {

                cell2.setPhrase(new Phrase("Área de Conhecimento", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(n.getDescricao(), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(String.valueOf(n.getPercentual()) + "%", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
            });


            grupoCadastro.getConteudos().forEach(n -> {

                cell2.setPhrase(new Phrase("Conteudo", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(n.getDescricao(), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(String.valueOf(n.getPercentual()) + "%", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
            });

            grupoCadastro.getHabilidades().forEach(n -> {

                cell2.setPhrase(new Phrase("Habilidades", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(n.getDescricao(), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(String.valueOf(n.getPercentual()) + "%", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
            });


            document.add(table);
            document.close();
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+name); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();

           
        } catch (Exception e) {
           e.printStackTrace();
        }

    }

    @GetMapping(value = "/relatorioHabilidadeEConteudoDownload/{idSimulado}")
    public void reports1(HttpServletResponse response, @PathVariable("idSimulado") Long idSimulado) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        
        try{

            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
                
            String name = "habilidades_conteudos_" + simulado.getNome().replace(" ", "_").replace("/", "-") + ".xlsx";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;

                
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("sheet 1");

            int rownum = 0;
            int cellnum = 0;
                
            Row row = sheet1.createRow(rownum++); 
                                
            Cell cell = row.createCell(cellnum++);                
            cell.setCellValue("TIPO");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Descrição");            
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Percentual");  
            
            GrupoCadastro grupoCadastro = grupoCadastroRepository.repoGrupoCadastro(idSimulado);

            for(AreaConhecimentoReports a : grupoCadastro.getAreaConhecimento()){
                cellnum = 0;                              
                row = sheet1.createRow(rownum++); 

                Cell cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue("Área de Conhecimento");
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(a.getDescricao());
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(a.getPercentual());
            }
            for(ConteudoReports a : grupoCadastro.getConteudos()){
                cellnum = 0;                              
                row = sheet1.createRow(rownum++); 
                                            
                Cell cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue("Conteudo");
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(a.getDescricao());
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(a.getPercentual());
            }
            for(HabilidadesReports a : grupoCadastro.getHabilidades()){
                cellnum = 0;                              
                row = sheet1.createRow(rownum++); 
                                            
                Cell cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue("Habilidades");
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(a.getDescricao());
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(a.getPercentual());
            }

            FileOutputStream out = new FileOutputStream(new File(source));
            wb.write(out);
            out.close();

            // export file
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+name); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
          
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/relatorioDetalhado/{idSimulado}")
    public ResponseEntity<List<DetalhadoReports>> reportsJSON(@PathVariable("idSimulado") Long idSimulado){
       
        try{
            return new ResponseEntity<>(reportsDetalhadoRepository.getDetalhado(idSimulado), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        
    }

    @GetMapping(value = "/relatorioDetalhadoPDF/{idSimulado}")
    public void reports2(@PathVariable("idSimulado") Long idSimulado, HttpSession session,HttpServletResponse response)
            throws DocumentException, MalformedURLException, IOException {

        Map<String, Object> map = new HashMap<String, Object>();
       
        try {
            Random random = new Random();
            String name = "Detalhado_" + random.nextInt(1000) + ".pdf";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;


            Image imgSoc = Image.getInstance(getUPLOAD_DIR() + "/imgs/unitoledo.png");
            imgSoc.scaleToFit(100,100);

            Document document = new Document(PageSize.A4.rotate());
    
            PdfWriter.getInstance(document, new FileOutputStream(source));
            document.open();
            Font f = new Font(Font.FontFamily.COURIER, 15, Font.BOLD);
            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
            Paragraph paragraph = new Paragraph("Relatório Detalhado \n" + simulado.getNome(), f);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(imgSoc);
            paragraph.setSpacingAfter(20.0f);
            document.add(paragraph);

            PdfPTable table = new PdfPTable(13);

            table.setWidthPercentage(100);
            table.setHorizontalAlignment(0);
            table.setSpacingAfter(10);
                
            
            Font font = FontFactory.getFont(FontFactory.COURIER, 6, BaseColor.BLACK); 
                
            PdfPCell cell = new PdfPCell(imgSoc, true);
            cell.setBackgroundColor(BaseColor.ORANGE);
            cell.setPadding(1);
            cell.setBorder(1);

    
            // write table header
            cell.setPhrase(new Phrase("Matrícula", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            
            cell.setPhrase(new Phrase("Nome do Aluno", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Qtd de acertos em FG", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
                
            cell.setPhrase(new Phrase("Nota FG Objetivas(%)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
                
            cell.setPhrase(new Phrase("Qtd de acertos em CE", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
                
            cell.setPhrase(new Phrase("Nota CE Objetivas(%)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Total de acertos objetivas", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Taxa de acertos total objetivas", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Nota FG discursivas(%)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Nota CE discursivas(%)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Nota Final FG(%)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Nota Final CE(%)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            cell.setPhrase(new Phrase("Nota Final Total(%)", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
    
            Font font2 = FontFactory.getFont(FontFactory.COURIER, 6); 
                
            PdfPCell cell2 = new PdfPCell(imgSoc, true);
            cell2.setPadding(2);
            cell2.setBorder(1);
                
    
            reportsDetalhadoRepository.getDetalhado(idSimulado).forEach(n -> {
                    
                    
                cell2.setPhrase(new Phrase(n.getMatricula(), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(n.getAluno(), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
                
                cell2.setPhrase(new Phrase(String.valueOf(n.getQtdAcertoFG()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaObjetivasFG()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getQtdAcertoCE()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaObjetivasCE()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getTotalAcertoObjetivas()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaObjetivasTotal()) + "%", font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
    
                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaDiscursivaFG()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaDiscursivaCE()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaFinalFG()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaFinalCE()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);

                cell2.setPhrase(new Phrase(String.valueOf(n.getNotaTotal()), font2));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell2);
    
            });
                
            document.add(table);
            document.close();
            
            
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+name); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            map.put("Status", "Error");
            
        }        
    }

    @GetMapping(value = "/relatorioDetalhadoDownload/{idSimulado}")
    public void reports2(HttpServletResponse response, @PathVariable("idSimulado") Long idSimulado) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        try {

            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
            
            String name = "Percentual-acerto-" + simulado.getNome().replace(" ", "_").replace("/", "-") + ".xlsx";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;

                
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet("sheet 1");

            int rownum = 0;
            int cellnum = 0;
                
            Row row = sheet1.createRow(rownum++); 
                                
            Cell cell = row.createCell(cellnum++);                
            cell.setCellValue("Matrícula");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nome do Aluno");            
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Qtd de acertos em FG");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nota FG Objetivas(%)");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Qtd de acertos em CE");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nota CE Objetivas(%)");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Total de acertos objetivas");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Taxa de acertos total objetivas");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nota FG discursivas(%)");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nota CE discursivas(%)");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nota Final FG(%)");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nota Final CE(%)");
            cell = row.createCell(cellnum++);                
            cell.setCellValue("Nota Final Total(%)");
            

            

            List<DetalhadoReports> detalhadoReports =  reportsDetalhadoRepository.getDetalhado(idSimulado);
            for(DetalhadoReports d : detalhadoReports){
                cellnum = 0;
                              
                row = sheet1.createRow(rownum++); 
                                
                Cell cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(d.getMatricula());
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(d.getAluno());            
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getQtdAcertoFG()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaObjetivasFG()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getQtdAcertoCE()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaObjetivasCE()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getTotalAcertoObjetivas()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaObjetivasTotal()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaDiscursivaFG()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaDiscursivaCE()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaFinalFG()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaFinalCE()));
                cellItem = row.createCell(cellnum++);                
                cellItem.setCellValue(String.valueOf(d.getNotaTotal()));
            };

                                                          
            FileOutputStream out = new FileOutputStream(new File(source));
            wb.write(out);
            out.close();
            System.out.println("Criei o arquivo");
            // export file
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+name); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   

    @GetMapping(value = "/excel/acertoDetalhado/{idSimulado}")
    public void excelRel(@PathVariable("idSimulado") Long idSimulado, HttpSession session,HttpServletResponse response)
                throws DocumentException, MalformedURLException, IOException {        

        try {

            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
            String name = simulado.getNome().replace(" ", "_").replace("/", "-") + ".xlsx";
            File directory = new File(getUPLOAD_DIR() + "/reports/");

            if (!directory.exists()) {
                directory.mkdir();
            }
            String source = getUPLOAD_DIR() + "/reports/" + "" + name;

            
            XSSFWorkbook wb = new XSSFWorkbook();                        
            XSSFSheet sheet1 = wb.createSheet("Detalhado");
            XSSFSheet sheet2 = wb.createSheet("Resumo");

            List<AcertosDetalhado> acertosDetalhados =  reportsDetalhadoRepository.reportsAcertosDetalhados(idSimulado);

            int rownum = 0;
            int cellnum = 0;

            final CellStyle style = sheet1.getWorkbook ().createCellStyle ();
            style.setAlignment(HorizontalAlignment.CENTER);            
            style.setFillForegroundColor(IndexedColors.ORANGE.getIndex()); 
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);   
            org.apache.poi.ss.usermodel.Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font); 
            
                
            Row row = sheet1.createRow(rownum++); 
                                
            Cell headIdAluno = row.createCell(cellnum++);                
            headIdAluno.setCellValue("RA");
            headIdAluno.setCellStyle(style);

            Cell headNomeAluno = row.createCell(cellnum++);
            headNomeAluno.setCellValue("Nome Aluno");
            headNomeAluno.setCellStyle(style);

            Cell headConteudo = row.createCell(cellnum++);
            headConteudo.setCellValue("Disciplina");
            headConteudo.setCellStyle(style);

            Cell headIdQuestao = row.createCell(cellnum++);
            headIdQuestao.setCellValue("Questão");
            headIdQuestao.setCellStyle(style);

            Cell headCorreta = row.createCell(cellnum++);
            headCorreta.setCellValue("Correta");
            headCorreta.setCellStyle(style);
            

            for(AcertosDetalhado a : acertosDetalhados){
                cellnum = 0;
                
                row = sheet1.createRow(rownum++); 
                                
                Cell idAluno = row.createCell(cellnum++);                
                idAluno.setCellValue(a.getIdAluno());

                Cell nomeAluno = row.createCell(cellnum++);
                nomeAluno.setCellValue(a.getNomeAluno());

                Cell conteudo = row.createCell(cellnum++);
                conteudo.setCellValue(a.getConteudo());

                Cell idQuestao = row.createCell(cellnum++);
                idQuestao.setCellValue(a.getIdQuestao());

                Cell correta = row.createCell(cellnum++);
                correta.setCellValue(a.getCorreta());
               
            }
           
            List<AcertosDetalhado> acertosDetalhadosResumo =  reportsDetalhadoRepository.reportsAcertosDetalhadosResumo(idSimulado);
            
            rownum = 0;
            cellnum = 0;
                
            row = sheet2.createRow(rownum++); 
                                
            Cell headIdAluno2 = row.createCell(cellnum++);                
            headIdAluno2.setCellValue("RA");
            headIdAluno2.setCellStyle(style);

            Cell headNomeAluno2 = row.createCell(cellnum++);
            headNomeAluno2.setCellValue("Nome Aluno");
            headNomeAluno2.setCellStyle(style);

            Cell headConteudo2 = row.createCell(cellnum++);
            headConteudo2.setCellValue("Disciplina");
            headConteudo2.setCellStyle(style);


            Cell headCorreta2 = row.createCell(cellnum++);
            headCorreta2.setCellValue("Correta");
            headCorreta2.setCellStyle(style);
            

            for(AcertosDetalhado a : acertosDetalhadosResumo){
                cellnum = 0;
                
                row = sheet2.createRow(rownum++); 
                                
                Cell idAluno = row.createCell(cellnum++);                
                idAluno.setCellValue(a.getIdAluno());

                Cell nomeAluno = row.createCell(cellnum++);
                nomeAluno.setCellValue(a.getNomeAluno());

                Cell conteudo = row.createCell(cellnum++);
                conteudo.setCellValue(a.getConteudo());

                Cell correta = row.createCell(cellnum++);
                correta.setCellValue(a.getCorreta());
               
            }
            
            FileOutputStream out = new FileOutputStream(new File(source));
            wb.write(out);
            out.close();            

            
            String filePathToBeServed = source;
            File fileToDownload = new File(filePathToBeServed);

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+name); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();

            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}