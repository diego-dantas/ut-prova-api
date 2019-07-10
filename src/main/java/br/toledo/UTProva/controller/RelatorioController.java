package br.toledo.UTProva.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.repository.ReportsRepository;
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
import br.toledo.UTProva.useful.CSVUtils;

@RestController
@RequestMapping(value = "/api/")
public class RelatorioController {

    @Autowired
    private PercentualDeAcertoRepository percentualDeAcertoRepository;
    @Autowired
    private GrupoCadastroRepository grupoCadastroRepository;
    @Autowired
    private ReportsDetalhadoRepository reportsDetalhadoRepository; 



	@PostMapping(value = "/01")
	public void teste01(@RequestBody String channel){
		System.out.println("chanel  " + channel);
	}


    @GetMapping(value = "/percentualDeAcerto/{idSimulado}/{tipo}")
    public ResponseEntity<PercentualDeAcertoCSV> reports(@PathVariable("idSimulado") Long idSimulado, @PathVariable("tipo") Long tipo) {
        
        try{
            return new ResponseEntity<>(percentualDeAcertoRepository.reportsCSV(idSimulado, tipo), HttpStatus.OK);
        }catch(Exception e ){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
        
    }

    @GetMapping(value = "/percentualDeAcertoDownload/{idSimulado}/{tipo}")
    public void percentualDeAcertoDownload(HttpServletResponse response, @PathVariable("idSimulado") Long idSimulado, @PathVariable("tipo") Long tipo) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        
        String name = "";
        if(tipo == 1){
            name = "Percentual-acerto-FORMAÇÃO-GERAL" + LocalDateTime.now() +  ".csv";
        }else if(tipo == 2){
            name = "Percentual-acerto-CONHECIMENTO-ESPECÍFICO" + LocalDateTime.now() +  ".csv";
        }else{
            name = "Percentual-acerto-GERAL" + LocalDateTime.now() +  ".csv";
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename=\"" + name + "\"");

        // StatefulBeanToCsv<PercentualDeAcerto> writer = new StatefulBeanToCsvBuilder<PercentualDeAcerto>(response.getWriter())
        //                     .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
        //                     .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
        //                     .build();
        StatefulBeanToCsv<PercentualDeAcertoCSV> writer = new StatefulBeanToCsvBuilder<PercentualDeAcertoCSV>(response.getWriter())
                                                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                                                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                                                .build();
        
        writer.write(percentualDeAcertoRepository.reportsCSV(idSimulado, tipo));
    }

    @GetMapping(value = "/relatorioHabilidadeEConteudo/{idSimulado}")
    public ResponseEntity<GrupoCadastro> reports1(@PathVariable("idSimulado") Long idSimulado){
        try {
            return new ResponseEntity<>(grupoCadastroRepository.repoGrupoCadastro(idSimulado), HttpStatus.OK);    
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
    }

    @GetMapping(value = "/relatorioDetalhado/{idSimulado}")
    public ResponseEntity<List<DetalhadoReports>> reports2(@PathVariable("idSimulado") Long idSimulado){
        List<DetalhadoReports> detalhadoReports = new ArrayList<>();

        DetalhadoReports dReports = new DetalhadoReports();
        dReports.setMatricula("1010");
        dReports.setAluno("Teste nome Aluno 1");
        dReports.setQtdAcertoFG(10);
        dReports.setNotaObjetivasFG(75.9);
        dReports.setQtdAcertoCE(5);
        dReports.setNotaObjetivasCE(64.9);
        dReports.setTotalAcertoObjetivas(15);
        dReports.setNotaObjetivasTotal(87.6);
        dReports.setNotaDiscursivaFG(30.7);
        dReports.setNotaDiscursivaCE(35.9);
        dReports.setNotaFinalFG(76.4);
        dReports.setNotaFinalCE(43.6);
        dReports.setNotaTotal(47.53);

        DetalhadoReports dReports2 = new DetalhadoReports();
        dReports2.setMatricula("2020");
        dReports2.setAluno("Teste nome Aluno 2");
        dReports2.setQtdAcertoFG(10);
        dReports2.setNotaObjetivasFG(75.9);
        dReports2.setQtdAcertoCE(5);
        dReports2.setNotaObjetivasCE(64.9);
        dReports2.setTotalAcertoObjetivas(15);
        dReports2.setNotaObjetivasTotal(87.6);
        dReports2.setNotaDiscursivaFG(30.7);
        dReports2.setNotaDiscursivaCE(35.9);
        dReports2.setNotaFinalFG(76.4);
        dReports2.setNotaFinalCE(43.6);
        dReports2.setNotaTotal(47.53);

        detalhadoReports.add(dReports);
        detalhadoReports.add(dReports2);
        
        return new ResponseEntity<>(reportsDetalhadoRepository.getDetalhado(idSimulado), HttpStatus.OK);
    }


    @GetMapping(value = "/relatorioHabilidadeEConteudoDownload/{idSimulado}")
    public void reports1(HttpServletResponse response, @PathVariable("idSimulado") Long idSimulado) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        
        String name = "Percentual-acerto-" + LocalDateTime.now() +  ".csv";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename=\"" + name + "\"");

        StatefulBeanToCsv<PercentualDeAcerto> writer = new StatefulBeanToCsvBuilder<PercentualDeAcerto>(response.getWriter())
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .build();
        
        writer.write(percentualDeAcertoRepository.listUsers());
    }

    @GetMapping(value = "/relatorioDetalhadoDownload/{idSimulado}")
    public void reports2(HttpServletResponse response, @PathVariable("idSimulado") Long idSimulado) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        
        String name = "Percentual-acerto-" + LocalDateTime.now() +  ".csv";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment; filename=\"" + name + "\"");

        StatefulBeanToCsv<PercentualDeAcerto> writer = new StatefulBeanToCsvBuilder<PercentualDeAcerto>(response.getWriter())
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                            .build();
        
        writer.write(percentualDeAcertoRepository.listUsers());
    }

   
}