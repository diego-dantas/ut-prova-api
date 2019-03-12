package br.toledo.UTProva.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrupoDTO {

    private String tipo;
    private List<ContextoDTO> contextos;

    public String getTipo() {
        return tipo;
    }

    public List<ContextoDTO> getContextos() {
        return contextos;
    }

    public void setContextos(List<ContextoDTO> contextos) {
        this.contextos = contextos;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}