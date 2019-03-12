package br.toledo.UTProva.model.dto;



public class SimuladoResolucaoDTO {

    private Long id;
    private Long idSimulado;
    private Long idQuestao;
    private Long idAlternativa;
    private String idAluno;
    private String idUltilizador;

    public Long getId() {
        return id;
    }

    public String getIdUltilizador() {
        return idUltilizador;
    }

    public void setIdUltilizador(String idUltilizador) {
        this.idUltilizador = idUltilizador;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSimulado() {
        return idSimulado;
    }

    public void setIdSimulado(Long idSimulado) {
        this.idSimulado = idSimulado;
    }

    public Long getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(Long idQuestao) {
        this.idQuestao = idQuestao;
    }
    
    public Long getIdAlternativa() {
        return idAlternativa;
    }
    
    public void setIdAlternativa(Long idAlternativa) {
        this.idAlternativa = idAlternativa;
    }
    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }
}