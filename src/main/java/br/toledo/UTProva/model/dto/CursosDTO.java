package br.toledo.UTProva.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CursosDTO {
    
    private int idColigada;
    private String id;
    private String nome;
    private int idNivelEnsino;
    private int idPeriodoLetivo;

    public int getIdColigada() {
        return idColigada;
    }

    public int getIdNivelEnsino() {
        return idNivelEnsino;
    }


    public void setIdNivelEnsino(int idNivelEnsino) {
        this.idNivelEnsino = idNivelEnsino;
    }

    public void setIdColigada(int idColigada) {
        this.idColigada = idColigada;
    }

    public String getId() {
        return id;
    }

  
    public void setId(String id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

  
    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdPeriodoLetivo() {
        return idPeriodoLetivo;
    }

    public void setIdPeriodoLetivo(int idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }
    
}