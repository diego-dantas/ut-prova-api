package br.toledo.UTProva.model.dto;

public class ContextoDTO {

    private String tipo;
    private String descricao;
    private int idColigada;
    private int idFilial;
    private String idUtilizador;
    private int idPeriodoLetivo;
    private int idMatrizAplicada;
    private String inicioPeriodoLetivo;
    private String terminoPeriodoLetivo;
    private int idNivelEnsino;

    public String getTipo() {
        return tipo;
    }
    
    public int getIdMatrizAplicada() {
        return idMatrizAplicada;
    }


    public void setIdMatrizAplicada(int idMatrizAplicada) {
        this.idMatrizAplicada = idMatrizAplicada;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdColigada() {
        return idColigada;
    }

    public void setIdColigada(int idColigada) {
        this.idColigada = idColigada;
    }
   
    public int getIdNivelEnsino() {
        return idNivelEnsino;
    }

    public void setIdNivelEnsino(int idNivelEnsino) {
        this.idNivelEnsino = idNivelEnsino;
    }

    public String getTerminoPeriodoLetivo() {
        return terminoPeriodoLetivo;
    }

    public void setTerminoPeriodoLetivo(String terminoPeriodoLetivo) {
        this.terminoPeriodoLetivo = terminoPeriodoLetivo;
    }

    public String getInicioPeriodoLetivo() {
        return inicioPeriodoLetivo;
    }

    public void setInicioPeriodoLetivo(String inicioPeriodoLetivo) {
        this.inicioPeriodoLetivo = inicioPeriodoLetivo;
    }

    public int getIdPeriodoLetivo() {
        return idPeriodoLetivo;
    }

    public void setIdPeriodoLetivo(int idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }

    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }
}