package br.toledo.UTProva.model.dto;

public class TurmasDTO {
    private int idColigada;
    private int idFilial;
    private int idPeriodoLetivo;
    private String id;
    private String nome;
    private int idNivelEnsino;
    private String idCurso;
    private String idProximaTurma;

    public int getIdColigada() {
        return idColigada;
    }

 
    public String getIdCurso() {
        return idCurso;
    }


    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }

    public String getIdProximaTurma() {
        return idProximaTurma;
    }

 
    public void setIdProximaTurma(String idProximaTurma) {
        this.idProximaTurma = idProximaTurma;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public int getIdNivelEnsino() {
        return idNivelEnsino;
    }

    public void setIdNivelEnsino(int idNivelEnsino) {
        this.idNivelEnsino = idNivelEnsino;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setIdColigada(int idColigada) {
        this.idColigada = idColigada;
    }
}