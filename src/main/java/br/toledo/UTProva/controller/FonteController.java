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
import br.toledo.UTProva.model.dao.entity.FonteEntity;
import br.toledo.UTProva.model.dao.repository.AreaConhecimentoRepository;
import br.toledo.UTProva.model.dao.repository.FonteRepository;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;
import br.toledo.UTProva.model.dto.FonteDTO;

@RestController
@RequestMapping(value  = "/api")
public class FonteController {

    @Autowired
    private FonteRepository fonteRepository;

    @PostMapping(value = "/createUpdateFonte")
    public ResponseEntity<Map> createUpdateFonte(@RequestBody FonteDTO fonteDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            if(fonteDTO.getId() != null && fonteDTO.isStatus() == false){
                int qtd = fonteRepository.countQuestao(fonteDTO.getId());
                if(qtd > 0){
                    map.put("success", false);
                    map.put("message", "Não foi possível efetuar a inativação. Motivo: A Fonte possui vínculo com Questão.");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            
            FonteEntity fonteEntity = new FonteEntity();
            fonteEntity.setId(fonteDTO.getId());
            fonteEntity.setDescription(fonteDTO.getDescription());
            fonteEntity.setStatus(fonteDTO.isStatus());

            fonteEntity = fonteRepository.save(fonteEntity);
            
            map.put("success", true);
            map.put("message", "Fonte salva com sucesso");


            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  
            map.put("success", false);
            map.put("message", "Erro ao salvar Fonte");         
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getFontes")
    public ResponseEntity<List<FonteDTO>> getFontes(){

        try {
            List<FonteDTO> fonteDTOs = new ArrayList<>();
            List<FonteEntity> entities = fonteRepository.findAll();
            
            for(FonteEntity fontes : entities){
                FonteDTO fonteDTO = new FonteDTO();
                fonteDTO.setId(fontes.getId());
                fonteDTO.setDescription(fontes.getDescription());
                fonteDTO.setStatus(fontes.isStatus());
                fonteDTOs.add(fonteDTO);
            }
            return ResponseEntity.ok(fonteDTOs);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/getFontes/ativo")
    public ResponseEntity<List<FonteDTO>> getFontesAtivs(){

        try {
            List<FonteDTO> fonteDTOs = new ArrayList<>();
            List<FonteEntity> entities = fonteRepository.findFontesAtivas();
            
            for(FonteEntity fontes : entities){
                FonteDTO fonteDTO = new FonteDTO();
                fonteDTO.setId(fontes.getId());
                fonteDTO.setDescription(fontes.getDescription());
                fonteDTO.setStatus(fontes.isStatus());
                fonteDTOs.add(fonteDTO);
            }
            return ResponseEntity.ok(fonteDTOs);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/deleteFonte")
    public ResponseEntity<Map> deleteHabilidade(@RequestBody FonteDTO fonteDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if(fonteRepository.countQuestao(fonteDTO.getId()) > 0){
                map.put("success", false);
                map.put("message", "A Fonte não pode ser excluída.");
            }else{
                fonteRepository.deleteById(fonteDTO.getId());;
                map.put("success", true);
                map.put("message", "Fonte excluida.");
            }
            
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();        
            map.put("success", false);
            map.put("message", "Erro ao excluir a Fonte.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
}