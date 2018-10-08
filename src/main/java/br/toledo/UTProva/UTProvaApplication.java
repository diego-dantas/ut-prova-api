package br.toledo.UTProva;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EntityScan(basePackageClasses = {
	UTProvaApplication.class,
	Jsr310JpaConverters.class
})
public class UTProvaApplication {

	//Definição do fuso horário para UTC
	@PostConstruct
	void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(UTProvaApplication.class, args);
	}

	@GetMapping(value = "/")
	public String home(){
		return "API ONLINE";
	}
}
