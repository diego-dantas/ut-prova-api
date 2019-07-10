package br.toledo.UTProva.model.dao.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import br.toledo.UTProva.useful.CSVUtils;

@Service
public class ReportsRepository {


    private static String UPLOAD_DIR = System.getProperty("user.dir") + "/reports/";

    public static String getUPLOAD_DIR() {
        return UPLOAD_DIR;
    }

    public FileWriter reports(String nameReports) throws IOException {
        File directory = new File(getUPLOAD_DIR());

        if (!directory.exists()) {
            directory.mkdir();
        }
        FileWriter writer = new FileWriter(directory + "/" + nameReports +  ".csv");
        return writer;
    

       
    }

}