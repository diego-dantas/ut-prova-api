package br.toledo.UTProva;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
 
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@SpringBootApplication
@RestController
@EntityScan(basePackageClasses = { UTProvaApplication.class, Jsr310JpaConverters.class })
public class UTProvaApplication {

	private int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB

	public static void main(String[] args) {
		SpringApplication.run(UTProvaApplication.class, args);
	}

	private static String UPLOAD_DIR = System.getProperty("user.dir");
    public static String getUPLOAD_DIR() {
        return UPLOAD_DIR;
    }

	@GetMapping(value = "/")
	public String home() {
		
		try {

			String source = getUPLOAD_DIR() + "/reports/HTMLtoPDF.pdf";
			String source2 = getUPLOAD_DIR() + "/imgs/unitoledo.png";
			OutputStream file = new FileOutputStream(new File(source));
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            StringBuilder htmlString = new StringBuilder();
            htmlString.append(new String("<p>Vamos ver como vai ficar isso depois&nbsp;</p>"));
            htmlString.append(new String("<p style='text-align: center'><img src='http://localhost:5000/api/getFile?name=teste.png' alt='undefined' style='float:right;height: 150px;width: 150px'/></p>"));               
			htmlString.append(new String("<p>isso tem que da certo em kkkk&nbsp;</p><p></p>"));
			htmlString.append(new String("<p style='text-align: center'><img src='http://localhost:5000/api/getFile?name=teste.png' alt='undefined' style='float:left;height: 200px;width: 200px'/></p>"));
			htmlString.append(new String("<p>agora vai da certo  senão ferrou</p><p></p>"));
			htmlString.append(new String("<p style='text-align: center'><img src='http://localhost:5000/api/getFile?name=teste.png' alt='undefined' style='float:none;height: 250px;width: 250px'/></p>"));
			htmlString.append(new String("<p>acabamos aqui&nbsp;</p>"));


                             
            document.open();
            InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            file.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "API On ";
	}

}
