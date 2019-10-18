package br.toledo.UTProva.reports.dto;

public class AcertosDetalhado {

    private Long idAluno;
    private String nomeAluno;
    private String conteudo;
    private Long idQuestao;
    private String correta;

    public Long getIdAluno() {
        return idAluno;
    }

    public String getCorreta() {
        return correta;
    }

    public void setCorreta(String correta) {
        this.correta = correta;
    }

    public Long getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(Long idQuestao) {
        this.idQuestao = idQuestao;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public void setIdAluno(Long idAluno) {
        this.idAluno = idAluno;
    }

    
}