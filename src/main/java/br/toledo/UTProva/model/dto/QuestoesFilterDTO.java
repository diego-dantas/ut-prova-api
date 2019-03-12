package br.toledo.UTProva.model.dto;

import java.util.List;
import br.toledo.UTProva.model.dto.HabilidadeDTO;
import br.toledo.UTProva.model.dto.ConteudoDTO;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;


public class QuestoesFilterDTO {

    private List<Long> codigos;
    private List<HabilidadeDTO> habilidades;
    private List<ConteudoDTO> conteudos;
    private List<AreaConhecimentoDTO> areaConhecimentos;
    private List<AnosDTO> anos;
    private TipoQuestaoDTO tipo;
    private String enade;
    private String discursiva;
    private String fonte;

    public List<HabilidadeDTO> getHabilidades() {
        return habilidades;
    }

    public TipoQuestaoDTO getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestaoDTO tipo) {
        this.tipo = tipo;
    }

    public List<Long> getCodigos() {
        return codigos;
    }

    public void setCodigos(List<Long> codigos) {
        this.codigos = codigos;
    }

    public List<AnosDTO> getAnos() {
        return anos;
    }


    public void setAnos(List<AnosDTO> anos) {
        this.anos = anos;
    }

    public void setHabilidades(List<HabilidadeDTO> habilidades) {
        this.habilidades = habilidades;
    }

    public String getEnade() {
        return enade;
    }


    public void setEnade(String enade) {
        this.enade = enade;
    }
   
    public List<ConteudoDTO> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<ConteudoDTO> conteudos) {
        this.conteudos = conteudos;
    }
   
    public List<AreaConhecimentoDTO> getAreaConhecimentos() {
        return areaConhecimentos;
    }

    public void setAreaConhecimentos(List<AreaConhecimentoDTO> areaConhecimentos) {
        this.areaConhecimentos = areaConhecimentos;
    }

    public String getDiscursiva() {
        return discursiva;
    }


    public void setDiscursiva(String discursiva) {
        this.discursiva = discursiva;
    }

    public String getFonte() {
        return fonte;
    }
  
    public void setFonte(String fonte) {
        this.fonte = fonte;
    }
}