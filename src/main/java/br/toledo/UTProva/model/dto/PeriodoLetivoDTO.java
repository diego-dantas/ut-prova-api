package br.toledo.UTProva.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeriodoLetivoDTO {

    private int idColigada;
    private int id;
    private int idNivelEnsino;
    private String codigo;
    private String descricao;

    public int getIdColigada() {
        return idColigada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public int getIdNivelEnsino() {
        return idNivelEnsino;
    }

    public void setIdNivelEnsino(int idNivelEnsino) {
        this.idNivelEnsino = idNivelEnsino;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setIdColigada(int idColigada) {
        this.idColigada = idColigada;
    }
    
}