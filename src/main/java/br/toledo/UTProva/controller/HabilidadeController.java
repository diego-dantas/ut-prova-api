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
import br.toledo.UTProva.model.dao.entity.HabilidadeEntity;
import br.toledo.UTProva.model.dao.repository.AreaConhecimentoRepository;
import br.toledo.UTProva.model.dao.repository.HabilidadeRepository;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;
import br.toledo.UTProva.model.dto.HabilidadeDTO;

@RestController
@RequestMapping(value  = "/api")
public class HabilidadeController {

    @Autowired
    private HabilidadeRepository habilidadeRepository;
    @Autowired
    private AreaConhecimentoRepository areaConhecimentoRepository;


    @PostMapping(value = "/createUpdateHabilidade")
    public ResponseEntity<Map> createUpdateHabilidade(@RequestBody HabilidadeDTO habilidadeDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            if(habilidadeDTO.getId() != null && habilidadeDTO.isStatus() == false){
                int qtd = habilidadeRepository.countQuestaoByHabilidade(habilidadeDTO.getId());
                if(qtd > 0){
                    map.put("success", false);
                    map.put("message", "Não foi possível efetuar a inativação. Motivo: A Habilidade possui vínculo com Questão.");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }
            AreaConhecimentoEntity areaConhecimento = new AreaConhecimentoEntity();
            areaConhecimento.setId(habilidadeDTO.getAreaConhecimento().getId());

            HabilidadeEntity habilidadeEntity = new HabilidadeEntity();
            habilidadeEntity.setId(habilidadeDTO.getId());
            habilidadeEntity.setDescription(habilidadeDTO.getDescription());
            habilidadeEntity.setStatus(habilidadeDTO.isStatus());
            habilidadeEntity.setAreaConhecimento(areaConhecimento);

            habilidadeEntity = habilidadeRepository.save(habilidadeEntity);

            map.put("success", true);
            map.put("message", "Habilidade salva com sucesso");

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();   
            map.put("success", false);
            map.put("message", "Erro ao salvar Habilidade");     
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Metodo para buscar todas as habilidades no banco
     */
    @GetMapping(value = "/getHabilidades")
    public ResponseEntity<List<HabilidadeDTO>> getHabilidades(){

        try {
            List<HabilidadeEntity> entities = habilidadeRepository.findAll();
            return getHabilidadesAll(entities);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Metodo para buscar as habilidades ativas no banco
     */
    @GetMapping(value = "/getHabilidades/ativo")
    public ResponseEntity<List<HabilidadeDTO>> getHabilidadesAtivo(){

        try {
            List<HabilidadeEntity> entities = habilidadeRepository.findAtivas();
            return getHabilidadesAll(entities);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Metodo para buscar todas as habilidades no banco
     */
    @PostMapping(value = "/getHabilidades/areas")
    public ResponseEntity<List<HabilidadeDTO>> getHabilidades(@RequestBody List<AreaConhecimentoDTO> areaConhecimentoDTOs){

        try {
            List<Long> idAreas = new ArrayList<>();
            areaConhecimentoDTOs.forEach(n -> idAreas.add(n.getId()));
            List<HabilidadeEntity> entities = habilidadeRepository.findAtivasByAreaCenhecimento(idAreas);
            return getHabilidadesAll(entities);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Metodo para buscar todas as habilidades no banco
     */
    @GetMapping(value = "/getHabilidades/{idArea}")
    public ResponseEntity<List<HabilidadeDTO>> getHabilidades(@PathVariable("idArea") Long idArea){

        try {
            List<Long> idAreas = new ArrayList<>();
            idAreas.add(idArea);
            List<HabilidadeEntity> entities = habilidadeRepository.findAtivasByAreaCenhecimento(idAreas);
            return getHabilidadesAll(entities);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping(value = "/deleteHabilidade")
    public ResponseEntity<Map> deleteHabilidade(@RequestBody HabilidadeDTO habilidadeDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if(habilidadeRepository.countQuestaoByHabilidade(habilidadeDTO.getId()) > 0){
                map.put("success", false);
                map.put("message", "Habilidade não pode ser excluída.");
            }else{
                habilidadeRepository.deleteById(habilidadeDTO.getId());;
                map.put("success", true);
                map.put("message", "Habilidade excluida.");
            }
            
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();        
            map.put("success", false);
            map.put("message", "Erro ao excluir a Habilidade.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    
    public ResponseEntity<List<HabilidadeDTO>> getHabilidadesAll(List<HabilidadeEntity> entities){

        try {
            List<HabilidadeDTO> habilidadeDTOs = new ArrayList<>();
            for(HabilidadeEntity habilidadeEntity : entities){
                HabilidadeDTO habilidadeDTO = new HabilidadeDTO();

                AreaConhecimentoDTO areaConhecimentoDTO = new AreaConhecimentoDTO();
                AreaConhecimentoEntity areaConhecimentoEntity = areaConhecimentoRepository.getOne(habilidadeEntity.getAreaConhecimento().getId());
                areaConhecimentoDTO.setId(areaConhecimentoEntity.getId());
                areaConhecimentoDTO.setDescription(areaConhecimentoEntity.getDescription());
                areaConhecimentoDTO.setStatus(areaConhecimentoEntity.isStatus());

                habilidadeDTO.setAreaConhecimento(areaConhecimentoDTO);
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

}