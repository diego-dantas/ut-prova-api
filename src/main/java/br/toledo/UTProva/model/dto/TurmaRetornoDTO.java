package br.toledo.UTProva.model.dto;

import java.util.List;

public class TurmaRetornoDTO {
    
    private int idPeriodoLetivo;
    private String id;
    private String nome;
    private String idCurso;
    private List<DisciplinasRetornoDTO> disciplinas;
    private List<SimuladoRetornoDTO> simulados;

    public int getIdPeriodoLetivo() {
        return idPeriodoLetivo;
    }

   
    public List<SimuladoRetornoDTO> getSimulados() {
        return simulados;
    }


    public void setSimulados(List<SimuladoRetornoDTO> simulados) {
        this.simulados = simulados;
    }

    public List<DisciplinasRetornoDTO> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<DisciplinasRetornoDTO> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getIdCurso() {
        return idCurso;
    }

 
    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }

    public String getNome() {
        return nome;
    }

   
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdPeriodoLetivo(int idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }


   

}