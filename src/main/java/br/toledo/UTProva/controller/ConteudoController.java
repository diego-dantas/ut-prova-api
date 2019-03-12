package br.toledo.UTProva.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map> createUpdateConteudo(@RequestBody ConteudoDTO conteudoDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            if(conteudoDTO.getId() != null && conteudoDTO.isStatus() == false){
                int qtd = conteudoRepository.countQuestao(conteudoDTO.getId());
                if(qtd > 0){
                    map.put("success", false);
                    map.put("message", "Não foi possível efetuar a inativação. Motivo: O Conteudo possui vínculo com Questão.");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            
            ConteudoEntity conteudoEntity = new ConteudoEntity();
            conteudoEntity.setId(conteudoDTO.getId());
            conteudoEntity.setDescription(conteudoDTO.getDescription());
            conteudoEntity.setStatus(conteudoDTO.isStatus());

            conteudoEntity = conteudoRepository.save(conteudoEntity);
            
            map.put("success", true);
            map.put("message", "Conteudo salvo com sucesso");

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();       
            map.put("success", false);
            map.put("message", "Erro ao salvar o Conteudo");         
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            
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

    @GetMapping(value = "/getConteudos/ativo")
    public ResponseEntity<List<ConteudoDTO>> getConteudosAtivo(){

        try {
            List<ConteudoDTO> conteudos = new ArrayList<>();
            List<ConteudoEntity> entities = conteudoRepository.findAtivas();
            
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
    public ResponseEntity<Map> deleteConteudo(@RequestBody ConteudoDTO conteudoDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            if(conteudoRepository.countQuestao(conteudoDTO.getId()) > 0){
                map.put("success", false);
                map.put("message", "Conteudo não pode ser excluída.");
            }else{
                conteudoRepository.deleteById(conteudoDTO.getId());;
                map.put("success", true);
                map.put("message", "Conteudo excluido.");
            }
            
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();        
            map.put("success", false);
            map.put("message", "Erro ao excluir o Conteudo.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

}