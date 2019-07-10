package br.toledo.UTProva.service;

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

public class ServicePost {


    public static ResponseEntity getContexto(String token, String idUser) {
        
        try {
            String url = Endpoint.SERVICE_TOLEDO+"/sistema/rest/comum/contextos/"+idUser;
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent",     Endpoint.USER_AGENT);
            con.setRequestProperty("Content-Type",   Endpoint.CONTENT_TYPE);
            con.setRequestProperty ("Authorization", "Bearer " + token);
            
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                //System.out.println(response.toString());
                String retorno =  response.toString();
                //Prepara o Header para retorno do json
                HttpHeaders responseHeaders = new HttpHeaders();
                List<String> exposeHeaders = new ArrayList<>();
                exposeHeaders.add("Access-Token");
                responseHeaders.setAccessControlExposeHeaders(exposeHeaders); //("Access-Control-Expose-Headers", "Access-Token");
                responseHeaders.set("Access-Token", "Bearer " + con.getHeaderField("Access-Token"));
                
                return new ResponseEntity<String>(retorno, responseHeaders, HttpStatus.OK);
                
            } else {
                System.out.println("GET request not worked 3");
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    private static void allowMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null/*static field*/, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void teste(String token, ContextoDTO contextoDTO) throws IOException {
        try {
            allowMethods("PATCH");
			String url = Endpoint.SERVICE_TOLEDO+"/sistema/rest/comum/contextos";
            URL obj = new URL(url);
            //HttpClient
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            con.setRequestMethod("PATCH");
            con.setRequestProperty("Content-Type",   Endpoint.CONTENT_TYPE_JSON);
            con.setRequestProperty ("Authorization", "Bearer " + token);
            
            Gson gson = new Gson();
            String json = gson.toJson(contextoDTO);
            
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(json);
            out.flush();
            out.close();
            int responseCode = con.getResponseCode();
			// System.out.println("Response Code Login : " + responseCode);
			
			
            
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
    }
    
    public static Gateway revalidToken(String token, ContextoDTO contextoDTO) {
        Gateway gateway = new Gateway();
        try {
            String url = Endpoint.SERVICE_TOLEDO+"/sistema/rest/comum/contextos"; 

            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = new MediaType("application", "json");
            headers.setContentType(mediaType);
            headers.add("Authorization", "Bearer " + token);
            

            
            HttpEntity<ContextoDTO> requestEntity = new HttpEntity<>(contextoDTO, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, String.class);
            String auto1 = response.getHeaders().getFirst("Access-Token");
            
            gateway.setToken(auto1);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return gateway;
	}
}