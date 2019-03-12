package br.toledo.UTProva.model.dto;

import java.util.List;

import br.toledo.UTProva.model.dto.TipoQuestaoDTO;;

public class QuestaoRetornoDTO {
    
    private Long id;
    private String descricao;
    private String imagem;
    private Long respondida;
    private List<AlternativaRetornoDTO> alternativas;

    public Long getId() {
        return id;
    }

    
    public String getImagem() {
        return imagem;
    }
    public void setImagem(String imagem) {
        this.imagem = imagem;
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

   
    public List<AlternativaRetornoDTO> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<AlternativaRetornoDTO> alternativas) {
        this.alternativas = alternativas;
    }

    public Long getRespondida() {
        return respondida;
    }

    public void setRespondida(Long respondida) {
        this.respondida = respondida;
    }
}