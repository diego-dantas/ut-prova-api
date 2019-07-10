package br.toledo.UTProva.reports.dto;

import java.util.List;

public class PercentualDeAcertoCSV{

    private double mediaSimulado;
    private List<PercentualDeAcerto> alunos;

    public double getMediaSimulado() {
        return mediaSimulado;
    }

    public List<PercentualDeAcerto> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<PercentualDeAcerto> alunos) {
        this.alunos = alunos;
    }

    public void setMediaSimulado(double mediaSimulado) {
        this.mediaSimulado = mediaSimulado;
    }

   


}