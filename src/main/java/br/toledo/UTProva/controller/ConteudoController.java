package br.toledo.UTProva.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.ConteudoEntity;
import br.toledo.UTProva.model.dao.repository.ConteudoRepository;
import br.toledo.UTProva.model.dto.ConteudoDTO;

@RestController
@RequestMapping(value  = "/api")
public class ConteudoController {

    @Autowired
    private ConteudoRepository conteudoRepository;

    @PostMapping(value = "/createUpdateConteudo")
    public ResponseEntity<ConteudoDTO> createUpdateConteudo(@RequestBody ConteudoDTO conteudoDTO){

        try {
            ConteudoEntity conteudoEntity = new ConteudoEntity();
            
            conteudoEntity.setId(conteudoDTO.getId());
            conteudoEntity.setDescription(conteudoDTO.getDescription());
            conteudoEntity.setStatus(conteudoDTO.isStatus());

            conteudoEntity = conteudoRepository.save(conteudoEntity);
            
            conteudoDTO.setId(conteudoEntity.getId());
            conteudoDTO.setDescription(conteudoEntity.getDescription());
            conteudoDTO.setStatus(conteudoEntity.isStatus());

            return ResponseEntity.ok(conteudoDTO);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getConteudos")
    public ResponseEntity<List<ConteudoDTO>> getConteudos(){

        try {
            List<ConteudoDTO> conteudos = new ArrayList<>();
            List<ConteudoEntity> entities = conteudoRepository.findAll();
            
            for(ConteudoEntity conteudoEntity : entities){
                ConteudoDTO conteudoDTO = new ConteudoDTO();
                conteudoDTO.setId(conteudoEntity.getId());
                conteudoDTO.setDescription(conteudoEntity.getDescription());
                conteudoDTO.setStatus(conteudoEntity.isStatus());
                conteudos.add(conteudoDTO);
            }
            return ResponseEntity.ok(conteudos);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping(value = "/deleteConteudo")
    public ResponseEntity<ConteudoDTO> deleteConteudo(@RequestBody ConteudoDTO conteudoDTO){
        try {
            conteudoRepository.deleteById(conteudoDTO.getId());;
            return ResponseEntity.ok(conteudoDTO);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}