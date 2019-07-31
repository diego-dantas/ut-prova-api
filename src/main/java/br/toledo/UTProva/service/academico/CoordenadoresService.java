package br.toledo.UTProva.service.academico;

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

import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.DisciplinasDTO;
import br.toledo.UTProva.model.dto.PeriodoLetivoDTO;
import br.toledo.UTProva.model.dto.TurmasDTO;
import br.toledo.UTProva.service.Endpoint;
import br.toledo.UTProva.service.Gateway;

public class CoordenadoresService {

    public List<PeriodoLetivoDTO> getPeriodosLetivos(String token){
        List<PeriodoLetivoDTO> retorno = new ArrayList<>();
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           
            HttpEntity<PeriodoLetivoDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<PeriodoLetivoDTO>> response = restTemplate.exchange(
                Gateway.SERVICE_TOLEDO + Endpoint.ACADEMICO_PERIODOS_LETIVOS, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<PeriodoLetivoDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA O PERIODO LETIVO " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public List<CursosDTO> getCurso(String token, int periodo){
        List<CursosDTO> retorno = new ArrayList<>();
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           

            HttpEntity<CursosDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<CursosDTO>> response = restTemplate.exchange(
                Gateway.SERVICE_TOLEDO + Endpoint.ACADEMICO_COORDENADOR_CURSO+periodo, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<CursosDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA OS CURSOS " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public List<TurmasDTO> getTurmas(String token, int periodo){
        List<TurmasDTO> retorno = new ArrayList<>();
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           

            HttpEntity<TurmasDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<TurmasDTO>> response = restTemplate.exchange(
                Gateway.SERVICE_TOLEDO + Endpoint.ACADEMICO_COORDENADOR_TURMAS+periodo, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<TurmasDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA AS TURMAS " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public List<DisciplinasDTO> getDisciplinas(String token, int periodo){
        List<DisciplinasDTO> retorno = new ArrayList<>();
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           

            HttpEntity<DisciplinasDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<DisciplinasDTO>> response = restTemplate.exchange(
                Gateway.SERVICE_TOLEDO + Endpoint.ACADEMICO_COORDENADOR_DISCIPLINAS+periodo, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<DisciplinasDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA AS DISCIPLINAS " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}