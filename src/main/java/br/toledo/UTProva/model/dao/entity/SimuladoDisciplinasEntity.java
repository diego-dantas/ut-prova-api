package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "simulado_disciplinas")
public class SimuladoDisciplinasEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String nome;
    private String idDisciplina;
    private String idTurma;
    private int idPeriodoLetivo;
    private SimuladoEntity simulado;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Column(name = "idDisciplina", nullable = false)
    public String getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(String idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "nome", nullable = false, length = 1000)
    public String getNome() {
        return nome;
    } 
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "periodo_letivo", nullable = false)
    public int getIdPeriodoLetivo() {
        return idPeriodoLetivo;
    }

    public void setIdPeriodoLetivo(int idPeriodoLetivo) {
        this.idPeriodoLetivo = idPeriodoLetivo;
    }

    @Column(name = "idTurma", nullable = false)
    public String getIdTurma() {
        return idTurma;
    }


    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public SimuladoEntity getSimulado() {
        return simulado;
    }
 
    public void setSimulado(SimuladoEntity simulado) {
        this.simulado = simulado;
    }

}