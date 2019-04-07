package br.toledo.UTProva.model.dto;

public class SimuladoDashAluno {

    private Long idSimulado;
    private int simuladosFinalizados;
    private int questoesRespondidas;
    private int questooesCertas;

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

    public int getQuestooesCertas() {
        return questooesCertas;
    }
    
    public void setQuestooesCertas(int questooesCertas) {
        this.questooesCertas = questooesCertas;
    }

    public int getQuestoesRespondidas() {
        return questoesRespondidas;
    }


    public void setQuestoesRespondidas(int questoesRespondidas) {
        this.questoesRespondidas = questoesRespondidas;
    }
}