package br.toledo.UTProva.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DisciplinasDTO {
    
    private int idColigada;
    private int idFilial;
    private int idPeriodoLetivo;
    private String idTurma;
    private String id;
    private String nome;
    private int idTurno;
    private int idNivelEnsino;

    public int getIdColigada() {
        return idColigada;
    }

 
    public int getIdNivelEnsino() {
        return idNivelEnsino;
    }

 
    public void setIdNivelEnsino(int idNivelEnsino) {
        this.idNivelEnsino = idNivelEnsino;
    }

    public int getIdTurno() {
        return idTurno;
    }

 
    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
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

    public String getIdTurma() {
        return idTurma;
    }

  
    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    public int getIdPeriodoLetivo() {
        return idPeriodoLetivo;
    }

  
    public void setIdPeriodoLetivo(int idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }

    public int getIdFilial() {
        return idFilial;
    }

  
    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

  
    public void setIdColigada(int idColigada) {
        this.idColigada = idColigada;
    }


}