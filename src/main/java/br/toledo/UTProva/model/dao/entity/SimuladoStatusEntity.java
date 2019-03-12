package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "simulado_status")
public class SimuladoStatusEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Long id;
    private String descricao;
    private List<SimuladoStatusAlunoEntity> simuladoStatusAluno;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "descricao", nullable = false, length = 3000)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @OneToMany(mappedBy = "simuladoStatus", fetch = FetchType.LAZY)
    public List<SimuladoStatusAlunoEntity> getSimuladoStatusAluno() {
        return simuladoStatusAluno;
    }

    public void setSimuladoStatusAluno(List<SimuladoStatusAlunoEntity> simuladoStatusAluno) {
        this.simuladoStatusAluno = simuladoStatusAluno;
    }
}