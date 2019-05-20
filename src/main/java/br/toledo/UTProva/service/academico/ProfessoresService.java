package br.toledo.UTProva.service.academico;

import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.DisciplinasDTO;
import br.toledo.UTProva.model.dto.TurmasDTO;
import br.toledo.UTProva.service.Endpoint;
import br.toledo.UTProva.service.Gateway;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class ProfessoresService {

    public List<CursosDTO> getCurso(String token){
        List<CursosDTO> retorno = new ArrayList<>();
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           

            HttpEntity<CursosDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<CursosDTO>> response = restTemplate.exchange(
                "https://servicos.toledo.br" + Endpoint.ACADEMICO_PROFESSOR_CURSOS, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<CursosDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA OS CURSOS DE PROFESSOR " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public List<TurmasDTO> getTurmas(String token){
        List<TurmasDTO> retorno = new ArrayList<>();
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           

            HttpEntity<TurmasDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<TurmasDTO>> response = restTemplate.exchange(
                "https://servicos.toledo.br" + Endpoint.ACADEMICO_PROFESSOR_TURMAS, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<TurmasDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA AS TURMAS DO PROFESSOR " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public List<DisciplinasDTO> getDisciplinas(String token){
        List<DisciplinasDTO> retorno = new ArrayList<>();
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           

            HttpEntity<DisciplinasDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<DisciplinasDTO>> response = restTemplate.exchange(
                "https://servicos.toledo.br" + Endpoint.ACADEMICO_PROFESSOR_DISCIPLINAS, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<DisciplinasDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA AS DISCIPLICAS DO PROFESSOR " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}