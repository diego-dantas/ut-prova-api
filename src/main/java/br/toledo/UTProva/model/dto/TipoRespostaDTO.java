package br.toledo.UTProva.model.dto;

import java.util.List;

public class TipoRespostaDTO{

    private Long id;
    private String descricao;


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

}