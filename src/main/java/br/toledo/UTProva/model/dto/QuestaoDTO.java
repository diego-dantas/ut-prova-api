package br.toledo.UTProva.model.dto;

import java.util.List;

public class QuestaoDTO {
    
    private Long id;
    private String descricao;
    private boolean status;
    private boolean enade;
    private boolean discursiva;
    private String fonte;
    private String ano;
    private char alterCorreta;
    private String imagem;
    private HabilidadeDTO habilidade;
    private ConteudoDTO conteudo;
    private AreaConhecimentoDTO areaConhecimento;
    private List<AlternativaDTO> alternativas;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean isEnade() {
        return enade;
    }

    public void setEnade(boolean enade) {
        this.enade = enade;
    }
    public boolean isDiscursiva() {
        return discursiva;
    }

    public void setDiscursiva(boolean discursiva) {
        this.discursiva = discursiva;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public char getAlterCorreta() {
        return alterCorreta;
    }

    public void setAlterCorreta(char alterCorreta) {
        this.alterCorreta = alterCorreta;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public HabilidadeDTO getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(HabilidadeDTO habilidade) {
        this.habilidade = habilidade;
    }

    public ConteudoDTO getConteudo() {
        return conteudo;
    }

    public void setConteudo(ConteudoDTO conteudo) {
        this.conteudo = conteudo;
    }

    public AreaConhecimentoDTO getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimentoDTO areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public List<AlternativaDTO> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<AlternativaDTO> alternativas) {
        this.alternativas = alternativas;
    }
}