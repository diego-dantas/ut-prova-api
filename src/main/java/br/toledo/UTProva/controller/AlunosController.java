package br.toledo.UTProva.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.AlunoQuestaoDiscursiva;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.TipoQuestaoEntity;
import br.toledo.UTProva.model.dao.repository.AlunoQuestaoDiscursivaRepository;
import br.toledo.UTProva.model.dao.repository.TipoQuestaoRepository;
import br.toledo.UTProva.model.dto.AlunoQuestaoDiscursivaDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;
import br.toledo.UTProva.model.dto.TipoQuestaoDTO;

@RestController
@RequestMapping(value = "/api")
public class AlunosController{

    @Autowired
    private AlunoQuestaoDiscursivaRepository alunoQuestaoDiscursivaRepository;
    @Autowired
    private TipoQuestaoRepository tipoQuestaoRepository;

    @GetMapping(value = "/getAlunoDiscursiva/{idSimulado}")
    public ResponseEntity<List<AlunoQuestaoDiscursivaDTO>> getAlunoDiscursiva(@PathVariable("idSimulado") Long idSimulado){

        try {
            List<AlunoQuestaoDiscursiva> alunoQuestaoDiscursivas = alunoQuestaoDiscursivaRepository.findByIdSimulado(idSimulado);
            List<AlunoQuestaoDiscursivaDTO> alunosDTOs = new ArrayList<>();
            alunoQuestaoDiscursivas.forEach(alunos -> {
               

                // TipoQuestaoEntity tipoQuestaoEntity = tipoQuestaoRepository.getOne(alunos.getTipoQuestao().getId());
                // TipoQuestaoDTO tipo = new TipoQuestaoDTO();
                // tipo.setId(tipoQuestaoEntity.getId());
                // tipo.setDescricao(tipoQuestaoEntity.getDescricao());
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> simulado = new HashMap<>();
                simulado.put("id", alunos.getSimulado().getId());

                
                map.put("notaFormacaoGeral", alunos.getNotaFormacaoGeral());  
                map.put("formacaoGeral", alunos.getNotaFormacaoGeral() == null ? false : true);  
                map.put("notaConhecimentoEspecifico", alunos.getNotaConhecimentoEspecifico());  
                map.put("conhecimentoEspecifico", alunos.getNotaConhecimentoEspecifico() == null ? false : true);   
                

                AlunoQuestaoDiscursivaDTO aluno = new AlunoQuestaoDiscursivaDTO();
                aluno.setSimulado(simulado);
                aluno.setId(alunos.getId());
                aluno.setNomeAluno(alunos.getNomeAluno());
                aluno.setIdAluno(alunos.getIdAluno());
                aluno.setNotas(map);
                
                

                alunosDTOs.add(aluno);
            });


            return new ResponseEntity<>(alunosDTOs, HttpStatus.OK);
        } catch (Exception e) {
           System.out.println("Deu merda mano na busca dos alunos ");
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping(value = "/createUpdateNotaDiscursiva")
    public ResponseEntity<Map<String, Object>> getAlunoDiscursiva(@RequestBody List<AlunoQuestaoDiscursivaDTO> alunos){

        Map<String, Object> map = new HashMap<>();
        try {
            
            List<AlunoQuestaoDiscursiva> alunosDiscursivas = new ArrayList<>();
            Gson gson = new Gson();
            alunos.forEach(aluno -> {
                // System.out.println("vamos la mano " + aluno.getNotas().get("notaFormacaoGeral").toString());
                // String al = gson.toJson(aluno);
                // System.out.println("vamos la mano " + al);
                Long idSimulado = Long.valueOf(aluno.getSimulado().get("id").toString());
                SimuladoEntity simulado = new SimuladoEntity();
                
                simulado.setId(idSimulado);
                System.out.println("notaConhecimentoEspecifico "+ aluno.getNotas().get("notaConhecimentoEspecifico"));
                System.out.println("notaFormacaoGeral "+ aluno.getNotas().get("notaFormacaoGeral"));
                AlunoQuestaoDiscursiva alunoDiscursiva = new AlunoQuestaoDiscursiva();
                String notaConhecimentoEspecifico = aluno.getNotas().get("notaConhecimentoEspecifico") != null ? 
                                                        aluno.getNotas().get("notaConhecimentoEspecifico").toString() : 
                                                        null;
                String notaFormacaoGeral = aluno.getNotas().get("notaFormacaoGeral") != null ? 
                                                        aluno.getNotas().get("notaFormacaoGeral").toString() : 
                                                        null;
                
                alunoDiscursiva.setSimulado(simulado);
                alunoDiscursiva.setId(aluno.getId());
                alunoDiscursiva.setNomeAluno(aluno.getNomeAluno());
                alunoDiscursiva.setIdAluno(aluno.getIdAluno());
                alunoDiscursiva.setNotaConhecimentoEspecifico(notaConhecimentoEspecifico);
                alunoDiscursiva.setNotaFormacaoGeral(notaFormacaoGeral);
                alunosDiscursivas.add(alunoDiscursiva);
            });

            alunoQuestaoDiscursivaRepository.saveAll(alunosDiscursivas);

            map.put("Status", "Success");
            map.put("Message", "Notas gravadas com Sucesso !");


            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
           System.out.println("Deu merda para salvar as questoes discursivas ");
           map.put("Status", "Error");
           map.put("Message", "Erro ao gravar a nota do aluno");
           return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
    
}