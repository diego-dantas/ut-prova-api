package br.toledo.UTProva.model.dto;

import java.util.Date;
import java.util.List;

public class SimuladoRetornoDTO {

    private Long id;
    private String nome;
    private Date dataHoraInicial;
    private Date dataHoraFinal;
    private boolean rascunho;
    private String status;
    private List<QuestaoDTO> questoes;

    public String getNome() {
        return nome;
    }


    public List<QuestaoDTO> getQuestoes() {
        return questoes;
    }


    public void setQuestoes(List<QuestaoDTO> questoes) {
        this.questoes = questoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

}