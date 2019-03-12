package br.toledo.UTProva.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GruposDTO {

    
    private List<GrupoDTO> grupos;
    public List<GrupoDTO> getGrupos() {
        return grupos;
    }


    public void setGrupos(List<GrupoDTO> grupos) {
        this.grupos = grupos;
    }

  
}