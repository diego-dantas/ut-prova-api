package br.toledo.UTProva.model.dto;

import java.util.Date;
import java.util.List;

public class SimuladoDTO {

    private Long id;
    private String nome;
    private Date dataHoraInicial;
    private Date dataHoraFinal;
    private boolean rascunho;
    private boolean enade;
    private String status;
    private List<QuestaoDTO> questoes;
    private List<CursosDTO> cursos;
    private List<TurmasDTO> turmas;
    private List<DisciplinasDTO> disciplinas;

    public String getNome() {
        return nome;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   

    public List<DisciplinasDTO> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<DisciplinasDTO> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public List<TurmasDTO> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<TurmasDTO> turmas) {
        this.turmas = turmas;
    }

    public List<CursosDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursosDTO> cursos) {
        this.cursos = cursos;
    }

    public List<QuestaoDTO> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<QuestaoDTO> questoes) {
        this.questoes = questoes;
    }

    public boolean isRascunho() {
        return rascunho;
    }


    public void setRascunho(boolean rascunho) {
        this.rascunho = rascunho;
    }

   

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataHoraInicial() {
        return dataHoraInicial;
    }

    public void setDataHoraInicial(Date dataHoraInicial) {
        this.dataHoraInicial = dataHoraInicial;
    }
    
    public Date getDataHoraFinal() {
        return dataHoraFinal;
    }

    public void setDataHoraFinal(Date dataHoraFinal) {
        this.dataHoraFinal = dataHoraFinal;
    }

    public boolean isEnade() {
        return enade;
    }

    public void setEnade(boolean enade) {
        this.enade = enade;
    }
   
}