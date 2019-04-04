package br.toledo.UTProva.model.dao.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileRepository {

    //private static String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/files/";
    private static String UPLOAD_DIR = System.getProperty("user.dir") + "/imgs/";
    public static String getUPLOAD_DIR() {
        return UPLOAD_DIR;
    }

      //metodo para realizar o upload da imagem
    public static Map fileUpload(String local, MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            
            File directory = new File(getUPLOAD_DIR());

            if (!directory.exists()) {
                directory.mkdir();
            }

            Random random = new Random();
            String nome = FilenameUtils.getBaseName(file.getOriginalFilename());
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String newNome = nome + "_" + random.nextInt(1000) + "." + extension;


            byte[] bytes = file.getBytes();
            String source = getUPLOAD_DIR() + "" + newNome;
            Path path = Paths.get(source);
            Files.write(path, bytes);

            map.put("nome", newNome);
            map.put("url",  source);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar imagem no servidor " + e);
            return map;
        }
        
    }
}