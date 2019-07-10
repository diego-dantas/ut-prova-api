package br.toledo.UTProva.model.dto;

import java.util.List;

public class RetornoCustom{
    
    private List<String> privilegios;
    private UserInfo userInfo;
    private GruposDTO gruposDTO;
    

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public List<String> getPrivilegios() {
        return privilegios;
    }

    public void setPrivilegios(List<String> privilegios) {
        this.privilegios = privilegios;
    }

    public GruposDTO getGruposDTO() {
        return gruposDTO;
    }

    public void setGruposDTO(GruposDTO gruposDTO) {
        this.gruposDTO = gruposDTO;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}