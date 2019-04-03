package br.toledo.UTProva.model.dao.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileRepository {

    private static String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/files/";
    public static String getUPLOAD_DIR() {
        return UPLOAD_DIR;
    }

      //metodo para realizar o upload da imagem
    public static String fileUpload(String local, MultipartFile file) throws IOException {
        
        try {
            byte[] bytes = file.getBytes();
            String source = getUPLOAD_DIR()+""+local+"/"+file.getOriginalFilename();
            Path path = Paths.get(source);
            Files.write(path, bytes);

            return source;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar imagem no servidor " + e);
            return "";
        }
        
    }
}