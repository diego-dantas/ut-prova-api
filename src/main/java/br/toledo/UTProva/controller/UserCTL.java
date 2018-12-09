package br.toledo.UTProva.controller;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.UsuarioEntity;
import br.toledo.UTProva.model.dao.repository.UsuarioRepository;
import br.toledo.UTProva.model.dto.UsuarioDTO;

@RestController
@RequestMapping(value = "/api")
public class UserCTL {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping(value = "/login/user")
    public ResponseEntity<UsuarioDTO> loginUser(@RequestBody UsuarioDTO usuarioDTO){

        String retorno = "";
        String loginStatus = "";
        Gson gson = new Gson();
        try {

            UsuarioEntity usuarioEntity = usuarioRepository.findByUsuario(usuarioDTO.getUsuario());

            // if(usuarioDTO.getUsuario().equals(usuarioEntity.getUser()) && 
            //    usuarioDTO.getSenha().equals(usuarioEntity.getPassword())){
                
            //     usuarioDTO.setId(usuarioEntity.getId());
            //     usuarioDTO.setStatus(usuarioEntity.isStatus());
            //     usuarioDTO.setCodNativo(usuarioEntity.getCodNativo());

            // }else{
            //     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            // }  
            

            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}