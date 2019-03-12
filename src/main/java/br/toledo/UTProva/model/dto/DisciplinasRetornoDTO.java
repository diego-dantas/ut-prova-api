package br.toledo.UTProva.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DisciplinasRetornoDTO {
    
    
    private String idCurso;
    private int idPeriodoLetivo;
    private String idTurma;
    private String id;
    private String nome;
    private List<SimuladoRetornoDTO> simulados;

    public String getIdCurso() {
        return idCurso;
    }

    public List<SimuladoRetornoDTO> getSimulados() {
        return simulados;
    }

    public void setSimulados(List<SimuladoRetornoDTO> simulados) {
        this.simulados = simulados;
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

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }




}