package br.toledo.UTProva.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Map> createUpdateAreaConhecimento(@RequestBody AreaConhecimentoDTO areaConhecimentoDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            if(areaConhecimentoDTO.getId() != null && areaConhecimentoDTO.isStatus() == false){
                int qtd = areaConhecimentoRepository.countQuestao(areaConhecimentoDTO.getId());
                if(qtd > 0){
                    map.put("success", false);
                    map.put("message", "Não foi possível efetuar a inativação. Motivo: A Area de conhecimento possui vínculo com Questão.");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            
            AreaConhecimentoEntity areaConhecimentoEntity = new AreaConhecimentoEntity();
            areaConhecimentoEntity.setId(areaConhecimentoDTO.getId());
            areaConhecimentoEntity.setDescription(areaConhecimentoDTO.getDescription());
            areaConhecimentoEntity.setStatus(areaConhecimentoDTO.isStatus());

            areaConhecimentoEntity = areaConhecimentoRepository.save(areaConhecimentoEntity);
            
            map.put("success", true);
            map.put("message", "Area de conhecimento salvo com sucesso");


            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  
            map.put("success", false);
            map.put("message", "Erro ao salvar Area de conhecimento");         
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
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

    @GetMapping(value = "/getAreaConhecimento/ativo")
    public ResponseEntity<List<AreaConhecimentoDTO>> getAreaConhecimentoAtivo(){

        try {
            List<AreaConhecimentoDTO> areaConhecimentoDTOs = new ArrayList<>();
            List<AreaConhecimentoEntity> entities = areaConhecimentoRepository.findAreaAtivas();
            
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
    public ResponseEntity<Map> deleteHabilidade(@RequestBody AreaConhecimentoDTO areaConhecimentoDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if(areaConhecimentoRepository.countQuestao(areaConhecimentoDTO.getId()) > 0){
                map.put("success", false);
                map.put("message", "Area de conhecimento não pode ser excluída.");
            }else{
                areaConhecimentoRepository.deleteById(areaConhecimentoDTO.getId());;
                map.put("success", true);
                map.put("message", "Area de conhecimento excluida.");
            }
            
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();        
            map.put("success", false);
            map.put("message", "Erro ao excluir a Area de conhecimento.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}