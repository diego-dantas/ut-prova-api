package br.toledo.UTProva.controller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.toledo.UTProva.model.dao.entity.AlternativaEntity;
import br.toledo.UTProva.model.dao.repository.AlternativaRepository;
import br.toledo.UTProva.model.dto.AlternativaDTO;

@Controller
@RequestMapping(value = "/api")
public class AlternativasController {

    @Autowired
    private AlternativaRepository alternativaRepository;

    /**
     * 
     * @param idQuestao
     * @return Lista das alternativas por ID da qestão
     */
    @RequestMapping(value = "/getAlternativas/{idQuestao}")
    public ResponseEntity<List<AlternativaDTO>> getAlternativas(@PathVariable("idQuestao") Long idQuestao){
        try {
            List<AlternativaDTO> alternativas = new ArrayList<>();
            List<AlternativaEntity> alternativaEntity = alternativaRepository.findAlternativasByQuestao(idQuestao);
            for(AlternativaEntity alter : alternativaEntity){
                AlternativaDTO alternativaDTO = new AlternativaDTO();
                alternativaDTO.setId(alter.getId());
                alternativaDTO.setCorreta(alter.isCorreta());
                alternativaDTO.setDescricao(alter.getDescricao());
                alternativas.add(alternativaDTO);
            }
            return ResponseEntity.ok(alternativas);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar as alternativas " + e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}