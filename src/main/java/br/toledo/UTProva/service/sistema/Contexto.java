package br.toledo.UTProva.service.sistema;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import br.toledo.UTProva.model.dto.GrupoDTO;
import br.toledo.UTProva.model.dto.GruposDTO;
import br.toledo.UTProva.service.Endpoint;
import br.toledo.UTProva.service.Gateway;

public class Contexto {

    public static ResponseEntity getContexto(String token, String idUser) {
        System.out.println("TO no contexto ");
        try {
            HttpHeaders headers = Gateway.createHeaders(token);
            
            HttpEntity<GruposDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<GruposDTO> response = restTemplate.exchange(
                Gateway.SERVICE_TOLEDO + Endpoint.SISTEMA_CONTEXTO + idUser, 
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<GruposDTO>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                
                HttpHeaders responseHeaders = new HttpHeaders();
                List<String> exposeHeaders = new ArrayList<>();
                exposeHeaders.add("Access-Token");

                responseHeaders.setAccessControlExposeHeaders(exposeHeaders); 
                responseHeaders.set("Access-Token", "Bearer " + token);
                System.out.println("TO no contexto 200");
                return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
                
            } else {
                System.out.println("GET request not worked 4");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}