package br.toledo.UTProva.model.dto;

import java.util.Date;

public class SimuladoDashAluno {

    private Long idSimulado;
    private int simuladosFinalizados;
    private int questoesRespondidas;
    private int questoesCertas;
    private double percentual;
    private Date dataInicio;
    private Date dataFinal;

    public Long getIdSimulado() {
        return idSimulado;
    }

    public void setIdSimulado(Long idSimulado) {
        this.idSimulado = idSimulado;
    }

    public int getSimuladosFinalizados() {
        return simuladosFinalizados;
    }

    public void setSimuladosFinalizados(int simuladosFinalizados) {
        this.simuladosFinalizados = simuladosFinalizados;
    }

    public int getQuestoesCertas() {
        return questoesCertas;
    }
    
    public void setQuestoesCertas(int questoesCertas) {
        this.questoesCertas = questoesCertas;
    }

    public int getQuestoesRespondidas() {
        return questoesRespondidas;
    }


    public void setQuestoesRespondidas(int questoesRespondidas) {
        this.questoesRespondidas = questoesRespondidas;
    }

    public double getPercentual() {
        return percentual;
    }

    public void setPercentual(double percentual) {
        this.percentual = percentual;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
}