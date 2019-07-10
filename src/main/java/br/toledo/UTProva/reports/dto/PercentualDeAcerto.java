package br.toledo.UTProva.reports.dto;

public class PercentualDeAcerto{

    private String idAluno;
    private String nomeAluno;
    private double taxaAcerto;

    public String getIdAluno() {
        return idAluno;
    }

    public double getTaxaAcerto() {
        return taxaAcerto;
    }

    public void setTaxaAcerto(double taxaAcerto) {
        this.taxaAcerto = taxaAcerto;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }

}