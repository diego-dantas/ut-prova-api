package br.toledo.UTProva.service.sistema;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.toledo.UTProva.model.dto.ContextoDTO;
import br.toledo.UTProva.model.dto.UsuarioDTO;
import br.toledo.UTProva.service.Endpoint;
import br.toledo.UTProva.service.Gateway;
import br.toledo.UTProva.service.ServiceGet;
import br.toledo.UTProva.service.Useful;
public class Login {

    /**
     * Metodo para realização de login da aplicação.
     * O metodo vai consumir um serviço da toledo que realiza a validação do usuario,
     * se o usuario for validado, o metodo vai chamar o metodo userInfo(),
     * que vai retornar os previlegios do usuario logado
     * @param username
     * @param password
     * @return
     */
    public static ResponseEntity<String> login(String username, String password) {

        try {
            //Header da requisição
            HttpURLConnection con = Gateway.createHttpConnect("POST", Endpoint.SISTEMA_LOGIN, null);
            
            
            //Parametros da requisição 
            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", username);
    	    parameters.put("password", password);
            
            //ralização da requisição 
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(Useful.getParamsString(parameters));
            out.flush();
            out.close();
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Login http ok " + con.getHeaderField("Access-Token"));
                String retono = ServiceGet.userInfo(con.getHeaderField("Access-Token"));

                //Prepara o Header para retorno do json
                HttpHeaders responseHeaders = new HttpHeaders();
                List<String> exposeHeaders = new ArrayList<>();
                exposeHeaders.add("Access-Token");
                responseHeaders.setAccessControlExposeHeaders(exposeHeaders);
                responseHeaders.set("Access-Token", con.getHeaderField("Access-Token"));
                
                return new ResponseEntity<String>(retono, responseHeaders, HttpStatus.OK);
            }
			
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}