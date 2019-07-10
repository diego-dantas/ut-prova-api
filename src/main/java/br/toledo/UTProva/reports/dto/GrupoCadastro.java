package br.toledo.UTProva.reports.dto;

import java.util.List;

public class GrupoCadastro {

    private List<AreaConhecimentoReports> areaConhecimento;
    private List<ConteudoReports> conteudos;
    private List<HabilidadesReports> habilidades;

    public List<AreaConhecimentoReports> getAreaConhecimento() {
        return areaConhecimento;
    }

    public List<HabilidadesReports> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<HabilidadesReports> habilidades) {
        this.habilidades = habilidades;
    }

    public List<ConteudoReports> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<ConteudoReports> conteudos) {
        this.conteudos = conteudos;
    }

    public void setAreaConhecimento(List<AreaConhecimentoReports> areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }
}