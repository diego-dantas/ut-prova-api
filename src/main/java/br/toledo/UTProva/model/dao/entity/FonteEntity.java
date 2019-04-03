package br.toledo.UTProva.model.dao.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.toledo.UTProva.useful.audit.DateAudit;

@Entity
@Table(name = "fonte")
public class FonteEntity extends DateAudit{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String description;
    private boolean status;
    private List<QuestaoEntity> questao;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    @NotBlank
    @Size(max = 250)
    public String getDescription() {
        return description;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "fonte", fetch = FetchType.LAZY)
    public List<QuestaoEntity> getQuestao() {
        return questao;
    }

    public void setQuestao(List<QuestaoEntity> questao) {
        this.questao = questao;
    }
}