package br.toledo.UTProva.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dto.ContextoDTO;
import br.toledo.UTProva.service.Gateway;
import br.toledo.UTProva.service.ServicePost;
import br.toledo.UTProva.service.academico.CoordenadoresService;

@RestController
@RequestMapping(value = "/api")
public class CoordenadorController {


    @PostMapping(value = "/getPeriodo")
    public ResponseEntity<Map> getPeriodo(@RequestHeader(value = "Authorization") String acessToken, 
                            @RequestBody ContextoDTO contextoDTO){
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            if(contextoDTO.getTipo().equals("COORDENADOR")){
                Gateway gateway =  ServicePost.revalidToken(acessToken, contextoDTO);
                CoordenadoresService coordenadoresService = new CoordenadoresService();
                
                map.put("periodos",coordenadoresService.getPeriodosLetivos(gateway.getToken()));
               
            }
            
            return new ResponseEntity<>(map, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    
}