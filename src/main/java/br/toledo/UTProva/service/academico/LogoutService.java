package br.toledo.UTProva.service.academico;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import br.toledo.UTProva.service.Endpoint;
import br.toledo.UTProva.service.Gateway;

public class LogoutService {


    public static int logout(String token){     
        try {

            HttpHeaders headers = Gateway.createHeaders(token);

            HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<Object> response = restTemplate.exchange(
                "https://servicos.toledo.br" + Endpoint.ACADEMICO_LOGOUT, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Object>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                return response.getStatusCodeValue();    
            } else {
                System.out.println("ERROR NA HORA FAZER O LOGOUT DO SISTEMA " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 400;
    }

}