package br.toledo.UTProva.model.dto;

public class ConteudoDTO{

    private Long id;
    private String description;
    private boolean status;
    private AreaConhecimentoDTO areaConhecimento;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }
   
    public void setStatus(boolean status) {
        this.status = status;
    }

    public AreaConhecimentoDTO getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimentoDTO areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }
}