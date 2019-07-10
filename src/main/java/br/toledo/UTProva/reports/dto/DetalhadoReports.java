package br.toledo.UTProva.reports.dto;

public class DetalhadoReports {

    private String matricula;
    private String aluno;
    private int qtdAcertoFG;
    private double notaObjetivasFG;
    private int qtdAcertoCE;
    private double notaObjetivasCE;
    private int totalAcertoObjetivas;
    private double notaObjetivasTotal;
    private double notaDiscursivaFG;
    private double notaDiscursivaCE;
    private double notaFinalFG;
    private double notaFinalCE;
    private double notaTotal;

    public String getMatricula() {
        return matricula;
    }

    public double getNotaTotal() {
        return notaTotal;
    }

    public void setNotaTotal(double notaTotal) {
        this.notaTotal = notaTotal;
    }

    public double getNotaFinalCE() {
        return notaFinalCE;
    }

    public void setNotaFinalCE(double notaFinalCE) {
        this.notaFinalCE = notaFinalCE;
    }

    public double getNotaFinalFG() {
        return notaFinalFG;
    }

    public void setNotaFinalFG(double notaFinalFG) {
        this.notaFinalFG = notaFinalFG;
    }

    public double getNotaDiscursivaCE() {
        return notaDiscursivaCE;
    }

    public void setNotaDiscursivaCE(double notaDiscursivaCE) {
        this.notaDiscursivaCE = notaDiscursivaCE;
    }

    public double getNotaDiscursivaFG() {
        return notaDiscursivaFG;
    }

    public void setNotaDiscursivaFG(double notaDiscursivaFG) {
        this.notaDiscursivaFG = notaDiscursivaFG;
    }

    public double getNotaObjetivasTotal() {
        return notaObjetivasTotal;
    }

    public void setNotaObjetivasTotal(double notaObjetivasTotal) {
        this.notaObjetivasTotal = notaObjetivasTotal;
    }

    public int getTotalAcertoObjetivas() {
        return totalAcertoObjetivas;
    }

    public void setTotalAcertoObjetivas(int totalAcertoObjetivas) {
        this.totalAcertoObjetivas = totalAcertoObjetivas;
    }

    public double getNotaObjetivasCE() {
        return notaObjetivasCE;
    }

    public void setNotaObjetivasCE(double notaObjetivasCE) {
        this.notaObjetivasCE = notaObjetivasCE;
    }

    public int getQtdAcertoCE() {
        return qtdAcertoCE;
    }

    public void setQtdAcertoCE(int qtdAcertoCE) {
        this.qtdAcertoCE = qtdAcertoCE;
    }

    public double getNotaObjetivasFG() {
        return notaObjetivasFG;
    }

    public void setNotaObjetivasFG(double notaObjetivasFG) {
        this.notaObjetivasFG = notaObjetivasFG;
    }

    public int getQtdAcertoFG() {
        return qtdAcertoFG;
    }

    public void setQtdAcertoFG(int qtdAcertoFG) {
        this.qtdAcertoFG = qtdAcertoFG;
    }

    public String getAluno() {
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}