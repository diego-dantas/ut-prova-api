package br.toledo.UTProva.model.dto;

public class UsuarioDTO {

    private Long id;
    private String usuario;
    private String senha;
    private String codNativo;
    private boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCodNativo() {
        return codNativo;
    }

    public void setCodNativo(String codNativo) {
        this.codNativo = codNativo;
    }

    public boolean isStatus() {
        return status;
    }


    public void setStatus(boolean status) {
        this.status = status;
    }
}