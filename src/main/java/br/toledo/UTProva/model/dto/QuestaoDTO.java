package br.toledo.UTProva.model.dto;

import java.util.List;

import br.toledo.UTProva.model.dto.TipoQuestaoDTO;;

public class QuestaoDTO {
    
    private Long id;
    private String descricao;
    private boolean status;
    private String dificuldade;
    private String ano;
    private char alterCorreta;
    private String imagem;
    private HabilidadeDTO habilidade;
    private ConteudoDTO conteudo;
    private TipoQuestaoDTO tipo;
    private TipoRespostaDTO tipoResposta;
    private AreaConhecimentoDTO areaConhecimento;
    private FonteDTO fonte;
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
   
    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
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

    public TipoQuestaoDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestaoDTO tipo) {
        this.tipo = tipo;
    }
    
    public AreaConhecimentoDTO getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimentoDTO areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }
    
    public FonteDTO getFonte() {
        return fonte;
    }

    public void setFonte(FonteDTO fonte) {
        this.fonte = fonte;
    }


    public TipoRespostaDTO getTipoResposta() {
        return tipoResposta;
    }

    public void setTipoResposta(TipoRespostaDTO tipoResposta) {
        this.tipoResposta = tipoResposta;
    }

    public List<AlternativaDTO> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<AlternativaDTO> alternativas) {
        this.alternativas = alternativas;
    }
}