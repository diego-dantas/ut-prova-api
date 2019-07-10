package br.toledo.UTProva.model.dto;


public class UserDTO{

    private String username;
    private String[] privilegios;

    public String getUsername() {
        return username;
    }

    public String[] getPrivilegios() {
        return privilegios;
    }

    public void setPrivilegios(String[] privilegios) {
        this.privilegios = privilegios;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}