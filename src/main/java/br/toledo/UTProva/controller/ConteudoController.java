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
import br.toledo.UTProva.model.dao.entity.ConteudoEntity;
import br.toledo.UTProva.model.dao.repository.AreaConhecimentoRepository;
import br.toledo.UTProva.model.dao.repository.ConteudoRepository;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;
import br.toledo.UTProva.model.dto.ConteudoDTO;
import br.toledo.UTProva.model.dto.ListIdsDTO;
import br.toledo.UTProva.model.dto.QuestoesFilterDTO;

@RestController
@RequestMapping(value  = "/api")
public class ConteudoController {

    @Autowired
    private ConteudoRepository conteudoRepository;
    @Autowired
    private AreaConhecimentoRepository areaConhecimentoRepository;

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
            
            AreaConhecimentoEntity areaConhecimento = new AreaConhecimentoEntity();
            areaConhecimento.setId(conteudoDTO.getAreaConhecimento().getId());

            ConteudoEntity conteudoEntity = new ConteudoEntity();
            conteudoEntity.setId(conteudoDTO.getId());
            conteudoEntity.setDescription(conteudoDTO.getDescription());
            conteudoEntity.setStatus(conteudoDTO.isStatus());
            conteudoEntity.setAreaConhecimento(areaConhecimento);

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

    // retorna todos os conteudos ativos e inativos do banco
    @GetMapping(value = "/getConteudos")
    public ResponseEntity<List<ConteudoDTO>> getConteudos(){
        try {
            List<ConteudoEntity> entities = conteudoRepository.findAll();
            return getConteudosAll(entities);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // retorna todos os conteudos ativos do banco
    @GetMapping(value = "/getConteudos/ativo")
    public ResponseEntity<List<ConteudoDTO>> getConteudosAtivo(){

        try {
            List<ConteudoDTO> conteudos = new ArrayList<>();
            List<ConteudoEntity> entities = conteudoRepository.findAtivas();
            return getConteudosAll(entities);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Retorna os conteudos ativos por area de conhecimento 
    @PostMapping(value = "/getConteudos/areas")
    public ResponseEntity<List<ConteudoDTO>> getConteudos(@RequestBody List<AreaConhecimentoDTO> areaConhecimentoDTOs){
        try {
            
            List<Long> idAreas = new ArrayList<>();
            areaConhecimentoDTOs.forEach(n -> idAreas.add(n.getId()));
            List<ConteudoEntity> entities = conteudoRepository.findConteudosAtivosByAreaConhecimento(idAreas);
            return getConteudosAll(entities);
        } catch (Exception e) {
            e.printStackTrace();        
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Retorna os conteudos ativos por area de conhecimento 
    @GetMapping(value = "/getConteudos/{idArea}")
    public ResponseEntity<List<ConteudoDTO>> getConteudos(@PathVariable("idArea") Long idArea){
        try {
            
            List<Long> idAreas = new ArrayList<>();
            idAreas.add(idArea);
            List<ConteudoEntity> entities = conteudoRepository.findConteudosAtivosByAreaConhecimento(idAreas);
            return getConteudosAll(entities);
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

    private ResponseEntity<List<ConteudoDTO>> getConteudosAll(List<ConteudoEntity> entities){
        try {
            List<ConteudoDTO> conteudos = new ArrayList<>();

            for(ConteudoEntity conteudoEntity : entities){
                
                ConteudoDTO conteudoDTO = new ConteudoDTO();
                AreaConhecimentoDTO areaConhecimentoDTO = new AreaConhecimentoDTO();
                AreaConhecimentoEntity areaConhecimentoEntity = areaConhecimentoRepository.getOne(conteudoEntity.getAreaConhecimento().getId());
                
                areaConhecimentoDTO.setId(areaConhecimentoEntity.getId());
                areaConhecimentoDTO.setDescription(areaConhecimentoEntity.getDescription());
                areaConhecimentoDTO.setStatus(areaConhecimentoEntity.isStatus());
                
                conteudoDTO.setAreaConhecimento(areaConhecimentoDTO);
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

}