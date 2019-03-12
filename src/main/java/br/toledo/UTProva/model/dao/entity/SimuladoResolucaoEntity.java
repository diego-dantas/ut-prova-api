package br.toledo.UTProva.model.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "simulado_resolucao")
public class SimuladoResolucaoEntity {

    private Long id;
    private Long idSimulado;
    private Long idQuestao;
    private Long idAlternativa;
    private String idAluno;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "id_simulado", nullable = false)
    public Long getIdSimulado() {
        return idSimulado;
    }

    public void setIdSimulado(Long idSimulado) {
        this.idSimulado = idSimulado;
    }

    @Column(name = "id_questao", nullable = false)
    public Long getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(Long idQuestao) {
        this.idQuestao = idQuestao;
    }

    @Column(name = "id_alternativa", nullable = false)
    public Long getIdAlternativa() {
        return idAlternativa;
    }
    
    public void setIdAlternativa(Long idAlternativa) {
        this.idAlternativa = idAlternativa;
    }

    @Column(name = "id_aluno", nullable = false)
    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }
}