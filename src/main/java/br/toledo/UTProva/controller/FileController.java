
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
import org.springframework.http.*;

import java.io.*;
import java.net.URLConnection;


import br.toledo.UTProva.model.dao.repository.FileRepository;

@RestController
@RequestMapping(path = "/api")
public class FileController {


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


    
    public static ResponseEntity<byte[]> filesTes(String imagem) {

        try {

            HttpHeaders headers = new HttpHeaders();

            File file = new File(imagem);

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



}