package br.toledo.UTProva.model.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.toledo.UTProva.useful.audit.DateAudit;

@Entity
@Table(name = "habilidades")
public class HabilidadeEntity extends DateAudit{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @NotBlank
    @Size(max = 250)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }
}