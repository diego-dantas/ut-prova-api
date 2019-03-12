package br.toledo.UTProva.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

    private String authorization;
    private String password;

    public Quote() {
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization(){
        return this.authorization;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }
    
    @Override
    public String toString() {
        return "Quote{" +
                "type='" + authorization + '\'' +
                ", value=" + password +
                '}';
    }
}