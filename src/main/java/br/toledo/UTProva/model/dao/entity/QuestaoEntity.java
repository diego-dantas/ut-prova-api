package br.toledo.UTProva.model.dao.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "questoes")
public class QuestaoEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Long id;
    private String descricao;
    private boolean status;
    private boolean enade;
    private boolean discursiva;
    private String dificuldade;
    private String ano;
    private char alterCorreta;
    private String imagem;
    private HabilidadeEntity habilidade;
    private ConteudoEntity conteudo;
    private TipoQuestaoEntity tipo;
    private TipoRespostaEntity tipoResposta;
    private AreaConhecimentoEntity areaConhecimento;
    private FonteEntity fonte;
    private List<AlternativaEntity> alternativa;
    private List<SimuladoQuestoesEntity> simuladoQuestao;

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

    @Column(name = "status", nullable = false)
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Column(name = "enade", nullable = true)
    public boolean isEnade() {
        return enade;
    }

    public void setEnade(boolean enade) {
        this.enade = enade;
    }

    @Column(name = "discursiva", nullable = true)
    public boolean isDiscursiva() {
        return discursiva;
    }

    public void setDiscursiva(boolean discursiva) {
        this.discursiva = discursiva;
    }


    @Column(name = "dificuldade", nullable = true, length = 50)
    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }


    @Column(name = "ano", nullable = true, length = 4)
    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    @Column(name = "alternativa_correta", nullable = true)
    public char getAlterCorreta() {
        return alterCorreta;
    }

    public void setAlterCorreta(char alterCorreta) {
        this.alterCorreta = alterCorreta;
    }

    @Lob
    @Column(name = "imagem", nullable = true, columnDefinition="LONGTEXT")
    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public HabilidadeEntity getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(HabilidadeEntity habilidade){
        this.habilidade = habilidade;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public ConteudoEntity getConteudo() {
        return conteudo;
    }

    public void setConteudo(ConteudoEntity conteudo) {
        this.conteudo = conteudo;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    public TipoQuestaoEntity getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuestaoEntity tipo) {
        this.tipo = tipo;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public AreaConhecimentoEntity getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimentoEntity areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public FonteEntity getFonte() {
        return fonte;
    }

    public void setFonte(FonteEntity fonte) {
        this.fonte = fonte;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public TipoRespostaEntity getTipoResposta() {
        return tipoResposta;
    }

    public void setTipoResposta(TipoRespostaEntity tipoResposta) {
        this.tipoResposta = tipoResposta;
    }


    @OneToMany(mappedBy = "questao", fetch = FetchType.LAZY)
    public List<AlternativaEntity> getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(List<AlternativaEntity> alternativa) {
        this.alternativa = alternativa;
    }

    @OneToMany(mappedBy = "questao", fetch = FetchType.LAZY)
    public List<SimuladoQuestoesEntity> getSimuladoQuestao() {
        return simuladoQuestao;
    }

    public void setSimuladoQuestao(List<SimuladoQuestoesEntity> simuladoQuestao) {
        this.simuladoQuestao = simuladoQuestao;
    }
}