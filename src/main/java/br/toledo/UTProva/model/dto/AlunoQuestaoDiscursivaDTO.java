package br.toledo.UTProva.model.dto;

import java.util.List;
import java.util.Map;

public class AlunoQuestaoDiscursivaDTO{

    private Long id;
    private String idAluno;
    private String nomeAluno;
    private Map<String, Object> simulado;
    private Map<String, Object> notas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public Map<String, Object> getSimulado() {
        return simulado;
    }

    public void setSimulado(Map<String, Object> simulado) {
        this.simulado = simulado;
    }

    public Map<String, Object> getNotas() {
        return notas;
    }

    public void setNotas(Map<String, Object> notas) {
        this.notas = notas;
    }

}