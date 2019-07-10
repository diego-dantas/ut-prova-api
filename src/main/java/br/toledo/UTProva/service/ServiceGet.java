package br.toledo.UTProva.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ServiceGet {
    
    public static String userInfo(String token) {
        String retorno = "";
        try {
            //URL da requisição
            URL url = new URL(Endpoint.SERVICE_TOLEDO+"/sistema/rest/seguranca/userinfo");
            
            //Header da requisição
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent",     Endpoint.USER_AGENT);
            con.setRequestProperty("Content-Type",   Endpoint.CONTENT_TYPE);
            con.setRequestProperty ("Authorization", "Bearer " + token);
            
            //recupera o codigo de retorno da requisição e valida, caso success, chama o metodo getResponse()
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = Useful.getResponse(con);
            } else {
                System.out.println("GET request not worked User Info");
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return retorno;
    }

    public static String userContexto(String token) {
        String retorno = "";
        try {
            //URL da requisição
            URL url = new URL(Endpoint.SERVICE_TOLEDO+Endpoint.SISTEMA_CONTEXTO);
            
            //Header da requisição
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent",     Endpoint.USER_AGENT);
            con.setRequestProperty("Content-Type",   Endpoint.CONTENT_TYPE);
            con.setRequestProperty ("Authorization", "Bearer " + token);
            
            //recupera o codigo de retorno da requisição e valida, caso success, chama o metodo getResponse()
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = Useful.getResponse(con);
            } else {
                System.out.println("GET request not worked Contexto");
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return retorno;
    }

    public static String userInfoGeral(String token) {
        String retorno = "";
        try {
            //URL da requisição
            URL url = new URL(Endpoint.SERVICE_TOLEDO+Endpoint.SISTEMA_USER_INFO);
            
            //Header da requisição
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent",     Endpoint.USER_AGENT);
            con.setRequestProperty("Content-Type",   Endpoint.CONTENT_TYPE);
            con.setRequestProperty ("Authorization", "Bearer " + token);
            
            //recupera o codigo de retorno da requisição e valida, caso success, chama o metodo getResponse()
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = Useful.getResponse(con);
            } else {
                System.out.println("GET request not worked Contexto");
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return retorno;
    }

    public static void getPeriodoCoordenador(String token) {

        try {
            //String url = Endpoint.SERVICE_TOLEDO+"/academico/rest/coordenadores/periodosLetivos";
            String url = Endpoint.SERVICE_TOLEDO+"/academico/rest/professores/disciplinas";
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + token);
            
            int responseCode = con.getResponseCode();
            // if (responseCode == HttpURLConnection.HTTP_OK) { // success
            //     System.out.println(Useful.getResponse(con));
            // } else {
            //     System.out.println("ERROR NA HORA DE PEGA O PERIODO LETIVO " + responseCode);
            // }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
    }


    public static ResponseEntity<String>  getUser(String token, String user) {

        try {
            //URL da requisição
            URL obj = new URL(Endpoint.SERVICE_TOLEDO+"/sistema/rest/comum/pessoas/pesquisa/"+user);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + token);
            
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                
                String retono = Useful.getResponse(con);
                
                //Prepara o Header para retorno do json
                HttpHeaders responseHeaders = new HttpHeaders();
                List<String> exposeHeaders = new ArrayList<>();
                exposeHeaders.add("Access-Token");
                responseHeaders.setAccessControlExposeHeaders(exposeHeaders);
                responseHeaders.set("Access-Token", "Bearer " + con.getHeaderField("Access-Token"));
                  
                return new ResponseEntity<String>(retono, responseHeaders, HttpStatus.OK);
            } else {
                System.out.println("GET request not worked 2");
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



}