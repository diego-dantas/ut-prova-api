package br.toledo.UTProva.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import br.toledo.UTProva.model.dto.PeriodoLetivoDTO;

public class Useful {

    /**
     * @param Map
     * @return String de  parametros para requisição
     * @throws UnsupportedEncodingException
     */
    public static String getParamsString(Map<String, String> params) 
      throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
 
        for (Map.Entry<String, String> entry : params.entrySet()) {
          result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          result.append("=");
          result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
          result.append("&");
        }
 
        String resultString = result.toString();
        return resultString.length() > 0
          ? resultString.substring(0, resultString.length() - 1)
          : resultString;
    }

    
     /**
     * Metodo para extrair o body da requisição e retonra o json
     * @param HttpURLConnection
     * @return Json  
     */
    public static String getResponse(HttpURLConnection con) {
        
        String retorno = "";
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            retorno =  response.toString();
            in.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return retorno;
    }

    public static List<PeriodoLetivoDTO> getResponseObject(HttpURLConnection con) {
        
        String retorno = "";
        List<PeriodoLetivoDTO> pe = new ArrayList<>();
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                PeriodoLetivoDTO per = new PeriodoLetivoDTO();
                Gson gson = new Gson();
                per = gson.fromJson(inputLine, PeriodoLetivoDTO.class);
                //response.append(inputLine);
                pe.add(per);
            }

            retorno =  response.toString();
            in.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return pe;
    }
}