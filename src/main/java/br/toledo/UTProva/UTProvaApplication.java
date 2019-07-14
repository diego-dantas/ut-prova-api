package br.toledo.UTProva;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.tomcat.jni.Time;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EntityScan(basePackageClasses = { UTProvaApplication.class, Jsr310JpaConverters.class })
public class UTProvaApplication {

	private int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB

	public static void main(String[] args) {
		SpringApplication.run(UTProvaApplication.class, args);
	}

	@GetMapping(value = "/")
	public String home() throws IOException, DocumentException {	
		return "API On ";
	}

}
