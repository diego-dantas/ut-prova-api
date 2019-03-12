package br.toledo.UTProva.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class Gateway {

    public static final String SERVICE_TOLEDO    = "https://servicos.toledo.br";
    private static final String USER_AGENT        = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0";
    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private String token;

    public static HttpURLConnection createHttpConnect(String method, String path, String token) {
        // Header da requisição
        HttpURLConnection con = null;
        try {
            // URL da requisição
            URL url = new URL(SERVICE_TOLEDO + path);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", CONTENT_TYPE_FORM);
            if(token  != null) con.setRequestProperty ("Authorization", "Bearer " + token);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }


    public static HttpHeaders createHeaders(String token){
        HttpHeaders headers = null;
        try {
            headers = new HttpHeaders();
            Charset utf8 = Charset.forName("UTF-8");
            MediaType mediaType = new MediaType("json", "html", utf8);
            headers.setContentType(mediaType);
            headers.add("Content-Type", CONTENT_TYPE_FORM);
            headers.add("User-Agent", USER_AGENT);
            if(token  != null) headers.add("Authorization", "Bearer " + token);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}