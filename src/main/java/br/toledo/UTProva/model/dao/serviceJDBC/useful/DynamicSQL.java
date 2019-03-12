package br.toledo.UTProva.model.dao.serviceJDBC.useful;

import java.util.List;

public class DynamicSQL {


    public static String createInString(List<String> ids){
        int size = 0;    
        String in = "";
        try {
            in = " in(";
        while( size < ids.size()){
            in += "'" + ids.get(size).toString() + "'";
            size++;
            if(size < ids.size()){
                in += ", ";
            }
        }
        in += ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
     }
}