package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "simulado_questoes")
public class SimuladoQuestoesEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private QuestaoEntity questao;
    private SimuladoEntity simulado;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public QuestaoEntity getQuestao() {
        return questao;
    }

    public void setQuestao(QuestaoEntity questao) {
        this.questao = questao;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public SimuladoEntity getSimulado() {
        return simulado;
    }

 
    public void setSimulado(SimuladoEntity simulado) {
        this.simulado = simulado;
    }

}