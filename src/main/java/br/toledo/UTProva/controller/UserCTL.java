package br.toledo.UTProva.controller;

import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dto.UserDTO;

@RestController
@RequestMapping(value = "/api")
public class UserCTL {


    @PostMapping(value = "/login/user")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO){

        String retorno = "";
        String loginStatus = "";
        Gson gson = new Gson();
        try {
            if(userDTO.getUsuario().equals("admin") && userDTO.getSenha().equals("admin")){
                loginStatus = "{Status: success}";
            }else{
                loginStatus = "{Status: failed}";
            }  
            retorno = gson.toJson(loginStatus);

            return ResponseEntity.ok(retorno);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}