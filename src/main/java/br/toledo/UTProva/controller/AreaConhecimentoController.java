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

import br.toledo.UTProva.model.dao.entity.AreaConhecimentoEntity;
import br.toledo.UTProva.model.dao.repository.AreaConhecimentoRepository;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;

@RestController
@RequestMapping(value  = "/api")
public class AreaConhecimentoController {

    @Autowired
    private AreaConhecimentoRepository areaConhecimentoRepository;

    @PostMapping(value = "/createUpdateAreaConhecimento")
    public ResponseEntity<AreaConhecimentoDTO> createUpdateAreaConhecimento(@RequestBody AreaConhecimentoDTO areaConhecimentoDTO){

        try {
            AreaConhecimentoEntity areaConhecimentoEntity = new AreaConhecimentoEntity();
            
            areaConhecimentoEntity.setId(areaConhecimentoDTO.getId());
            areaConhecimentoEntity.setDescription(areaConhecimentoDTO.getDescription());
            areaConhecimentoEntity.setStatus(areaConhecimentoDTO.isStatus());

            areaConhecimentoEntity = areaConhecimentoRepository.save(areaConhecimentoEntity);
            
            areaConhecimentoDTO.setId(areaConhecimentoEntity.getId());
            areaConhecimentoDTO.setDescription(areaConhecimentoEntity.getDescription());
            areaConhecimentoDTO.setStatus(areaConhecimentoEntity.isStatus());


            return ResponseEntity.ok(areaConhecimentoDTO);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getAreaConhecimento")
    public ResponseEntity<List<AreaConhecimentoDTO>> getAreaConhecimento(){

        try {
            List<AreaConhecimentoDTO> areaConhecimentoDTOs = new ArrayList<>();
            List<AreaConhecimentoEntity> entities = areaConhecimentoRepository.findAll();
            
            for(AreaConhecimentoEntity areaConhecimentoEntity : entities){
                AreaConhecimentoDTO areaConhecimentoDTO = new AreaConhecimentoDTO();
                areaConhecimentoDTO.setId(areaConhecimentoEntity.getId());
                areaConhecimentoDTO.setDescription(areaConhecimentoEntity.getDescription());
                areaConhecimentoDTO.setStatus(areaConhecimentoEntity.isStatus());
                areaConhecimentoDTOs.add(areaConhecimentoDTO);
            }
            return ResponseEntity.ok(areaConhecimentoDTOs);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/deleteAreaConhecimento")
    public ResponseEntity<AreaConhecimentoDTO> deleteHabilidade(@RequestBody AreaConhecimentoDTO areaConhecimentoDTO){
        try {
            areaConhecimentoRepository.deleteById(areaConhecimentoDTO.getId());;
            return ResponseEntity.ok(areaConhecimentoDTO);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}