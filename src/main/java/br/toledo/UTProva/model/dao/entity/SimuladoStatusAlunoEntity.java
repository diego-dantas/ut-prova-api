package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "simulado_status_aluno")
public class SimuladoStatusAlunoEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String idAluno;
    private SimuladoEntity simulado;
    private SimuladoStatusEntity simuladoStatus;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "id_aluno", nullable = false, length = 10)
    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public SimuladoEntity getSimulado() {
        return simulado;
    }

    public void setSimulado(SimuladoEntity simulado) {
        this.simulado = simulado;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public SimuladoStatusEntity getSimuladoStatus() {
        return simuladoStatus;
    }

    public void setSimuladoStatus(SimuladoStatusEntity simuladoStatus) {
        this.simuladoStatus = simuladoStatus;
    }

}