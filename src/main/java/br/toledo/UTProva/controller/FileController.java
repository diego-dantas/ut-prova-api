
package br.toledo.UTProva.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import java.io.*;
import java.net.URLConnection;


import br.toledo.UTProva.model.dao.repository.FileRepository;
import br.toledo.UTProva.reports.ExcelUseful;

@RestController
@RequestMapping(path = "/api")
public class FileController {

   @Autowired
   ExcelUseful excelUseful;

    @PostMapping(path = "/upload/{origin}")
    public ResponseEntity<Map> uploadFilePlan(@PathVariable("origin") String origin,  @RequestParam("files") List<MultipartFile> files) throws IOException{
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            map = FileRepository.fileUpload(origin, files.get(0));
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value="/getFile")
    @ResponseBody
    public ResponseEntity<byte[]> filesTeste(@RequestParam("name") String name) {

        try {

            HttpHeaders headers = new HttpHeaders();

            File file = new File(FileRepository.getUPLOAD_DIR() + "/" +name);

            InputStream is = new BufferedInputStream(new FileInputStream(file));

            String mimeType = URLConnection.guessContentTypeFromStream(is);

            headers.setContentType(MediaType.valueOf(mimeType));

            return new ResponseEntity<byte[]>(IOUtils.toByteArray(is), headers, HttpStatus.OK);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value="/getPDF")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getPDF(@RequestParam("name") String name) {

        try {
            ClassPathResource pdfFile = new ClassPathResource("/reports/Detalhado-2019-07-15T19:20:07.045.pdf");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            headers.add("Content-Disposition", "filename=" + name);
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            
            headers.setContentLength(pdfFile.contentLength());
            ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
                new InputStreamResource(pdfFile.getInputStream()), headers, HttpStatus.OK);
            return response;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/upload/excel")
    public void uploadExcel(@RequestParam("uploadexcel") MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        excelUseful.parseExcelToFile(file);
        System.out.println(file.getOriginalFilename());
    }


    
   



}