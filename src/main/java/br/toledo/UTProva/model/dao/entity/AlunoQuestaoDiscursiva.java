package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.Gson;



@Entity
@Table(name = "aluno_questao_discursiva")
public class AlunoQuestaoDiscursiva implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long id;
    private String idAluno; 
    private String nomeAluno;
    private SimuladoEntity simulado;
    private String notaFormacaoGeral;
    private String notaConhecimentoEspecifico;

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

    @Column(name = "nota_formacao_geral", nullable = true, length = 10)
    public String getNotaFormacaoGeral() {
        return notaFormacaoGeral;
    }

    public void setNotaFormacaoGeral(String notaFormacaoGeral) {
        this.notaFormacaoGeral = notaFormacaoGeral;
    }

    @Column(name = "nota_conhecimento_especifico", nullable = true, length = 10)
    public String getNotaConhecimentoEspecifico() {
        return notaConhecimentoEspecifico;
    }

    public void setNotaConhecimentoEspecifico(String notaConhecimentoEspecifico) {
        this.notaConhecimentoEspecifico = notaConhecimentoEspecifico;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }

}