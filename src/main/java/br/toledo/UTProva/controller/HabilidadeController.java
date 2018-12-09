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

import br.toledo.UTProva.model.dao.entity.HabilidadeEntity;
import br.toledo.UTProva.model.dao.repository.HabilidadeRepository;
import br.toledo.UTProva.model.dto.HabilidadeDTO;

@RestController
@RequestMapping(value  = "/api")
public class HabilidadeController {

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    @PostMapping(value = "/createUpdateHabilidade")
    public ResponseEntity<HabilidadeDTO> createUpdateHabilidade(@RequestBody HabilidadeDTO habilidadeDTO){

        try {
            HabilidadeEntity habilidadeEntity = new HabilidadeEntity();
            habilidadeEntity.setId(habilidadeDTO.getId());
            habilidadeEntity.setDescription(habilidadeDTO.getDescription());
            habilidadeEntity.setStatus(habilidadeDTO.isStatus());

            habilidadeEntity = habilidadeRepository.save(habilidadeEntity);
            
            habilidadeDTO.setId(habilidadeEntity.getId());
            habilidadeDTO.setDescription(habilidadeEntity.getDescription());
            habilidadeDTO.setStatus(habilidadeEntity.isStatus());


            return ResponseEntity.ok(habilidadeDTO);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getHabilidades")
    public ResponseEntity<List<HabilidadeDTO>> getHabilidades(){

        try {
            List<HabilidadeDTO> habilidadeDTOs = new ArrayList<>();
            List<HabilidadeEntity> entities = habilidadeRepository.findAll();
            
            for(HabilidadeEntity habilidadeEntity : entities){
                HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
                habilidadeDTO.setId(habilidadeEntity.getId());
                habilidadeDTO.setDescription(habilidadeEntity.getDescription());
                habilidadeDTO.setStatus(habilidadeEntity.isStatus());
                habilidadeDTOs.add(habilidadeDTO);
            }
            return ResponseEntity.ok(habilidadeDTOs);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping(value = "/deleteHabilidade")
    public ResponseEntity<HabilidadeDTO> deleteHabilidade(@RequestBody HabilidadeDTO habilidadeDTO){
        try {
            habilidadeRepository.deleteById(habilidadeDTO.getId());;
            return ResponseEntity.ok(habilidadeDTO);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}