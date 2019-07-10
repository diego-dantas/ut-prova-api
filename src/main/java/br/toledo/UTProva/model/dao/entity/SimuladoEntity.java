package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "simulados")
public class SimuladoEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Long id;
    private String nome;
    private Date dataHoraInicial;
    private Date dataHoraFinal;
    private boolean rascunho;
    private boolean enade;
    private String status;
    private List<SimuladoQuestoesEntity> simuladoQuestao;
    private List<SimuladoCursosEntity> simuladoCursos;
    private List<SimuladoTurmasEntity> simuladoTurmas;
    private List<SimuladoDisciplinasEntity> simuladoDisciplinas;
    private List<SimuladoStatusAlunoEntity> simuladoStatusAluno;
    private List<AlunoQuestaoDiscursiva> alunoQuestaoDiscursiva;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public boolean isEnade() {
        return enade;
    }

    @Column(name = "nome", nullable = false, length = 1000)
    public String getNome() {
        return nome;
    } 
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "rascunho", nullable = false)
    public boolean isRascunho() {
        return rascunho;
    }

    public void setRascunho(boolean rascunho) {
        this.rascunho = rascunho;
    }

    @Column(name = "enade", nullable = false)
    public void setEnade(boolean enade) {
        this.enade = enade;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "dataHoraInicial", nullable = false)
    public Date getDataHoraInicial() {
        return dataHoraInicial;
    }

    public void setDataHoraInicial(Date dataHoraInicial) {
        this.dataHoraInicial = dataHoraInicial;
    }

    @Column(name = "dataHoraFinal", nullable = false)
    public Date getDataHoraFinal() {
        return dataHoraFinal;
    }

    public void setDataHoraFinal(Date dataHoraFinal) {
        this.dataHoraFinal = dataHoraFinal;
    }

    @OneToMany(mappedBy = "simulado", fetch = FetchType.LAZY)
    public List<SimuladoQuestoesEntity> getSimuladoQuestao() {
        return simuladoQuestao;
    }

    public void setSimuladoQuestao(List<SimuladoQuestoesEntity> simuladoQuestao) {
        this.simuladoQuestao = simuladoQuestao;
    }

    @OneToMany(mappedBy = "simulado", fetch = FetchType.LAZY)
    public List<SimuladoCursosEntity> getSimuladoCursos() {
        return simuladoCursos;
    }

    public void setSimuladoCursos(List<SimuladoCursosEntity> simuladoCursos) {
        this.simuladoCursos = simuladoCursos;
    }
    
    @OneToMany(mappedBy = "simulado", fetch = FetchType.LAZY)
    public List<SimuladoTurmasEntity> getSimuladoTurmas() {
        return simuladoTurmas;
    }

    public void setSimuladoTurmas(List<SimuladoTurmasEntity> simuladoTurmas) {
        this.simuladoTurmas = simuladoTurmas;
    }
    
    @OneToMany(mappedBy = "simulado", fetch = FetchType.LAZY)
    public List<SimuladoDisciplinasEntity> getSimuladoDisciplinas() {
        return simuladoDisciplinas;
    }

    public void setSimuladoDisciplinas(List<SimuladoDisciplinasEntity> simuladoDisciplinas) {
        this.simuladoDisciplinas = simuladoDisciplinas;
    }
    
    @Column(name = "status")
    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "simulado", fetch = FetchType.LAZY)
    public List<SimuladoStatusAlunoEntity> getSimuladoStatusAluno() {
        return simuladoStatusAluno;
    }

    public void setSimuladoStatusAluno(List<SimuladoStatusAlunoEntity> simuladoStatusAluno) {
        this.simuladoStatusAluno = simuladoStatusAluno;
    }

    @OneToMany(mappedBy = "simulado", fetch = FetchType.LAZY)
    public List<AlunoQuestaoDiscursiva> getAlunoQuestaoDiscursiva() {
        return alunoQuestaoDiscursiva;
    }

    public void setAlunoQuestaoDiscursiva(List<AlunoQuestaoDiscursiva> alunoQuestaoDiscursiva) {
        this.alunoQuestaoDiscursiva = alunoQuestaoDiscursiva;
    }

}