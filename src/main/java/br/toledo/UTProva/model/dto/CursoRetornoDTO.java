package br.toledo.UTProva.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CursoRetornoDTO {
    
    
    private String id;
    private String nome;
    private int idPeriodoLetivo;
    private List<SimuladoRetornoDTO> simulados;
    private List<TurmaRetornoDTO> turmas;

    public String getId() {
        return id;
    }
    
    public List<TurmaRetornoDTO> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<TurmaRetornoDTO> turmas) {
        this.turmas = turmas;
    }

    public List<SimuladoRetornoDTO> getSimulados() {
        return simulados;
    }


    public void setSimulados(List<SimuladoRetornoDTO> simulados) {
        this.simulados = simulados;
    }

    public int getIdPeriodoLetivo() {
        return idPeriodoLetivo;
    }

    public void setIdPeriodoLetivo(int idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }

    public String getNome() {
        return nome;
    }

   
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    
}