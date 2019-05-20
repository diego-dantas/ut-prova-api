package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name = "simulado_status_aluno")
public class SimuladoStatusAlunoEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String idAluno;
    private String nomeAluno;
    private SimuladoEntity simulado;
    private SimuladoStatusEntity simuladoStatus;
    private Date dataInicio;
    private Date dataFinal;

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

    @Column(name = "nome_aluno", nullable = false, length = 300)
    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
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

    @Column(name = "dataInicio", nullable = false)
    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    @Column(name = "dataFinal", nullable = true)
    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

}