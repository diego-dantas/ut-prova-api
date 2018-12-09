package br.toledo.UTProva.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.AlternativaEntity;
import br.toledo.UTProva.model.dao.entity.AreaConhecimentoEntity;
import br.toledo.UTProva.model.dao.entity.ConteudoEntity;
import br.toledo.UTProva.model.dao.entity.HabilidadeEntity;
import br.toledo.UTProva.model.dao.entity.QuestaoEntity;
import br.toledo.UTProva.model.dao.repository.AlternativaRepository;
import br.toledo.UTProva.model.dao.repository.AreaConhecimentoRepository;
import br.toledo.UTProva.model.dao.repository.ConteudoRepository;
import br.toledo.UTProva.model.dao.repository.HabilidadeRepository;
import br.toledo.UTProva.model.dao.repository.QuestaoRepository;
import br.toledo.UTProva.model.dto.AlternativaDTO;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;
import br.toledo.UTProva.model.dto.ConteudoDTO;
import br.toledo.UTProva.model.dto.HabilidadeDTO;
import br.toledo.UTProva.model.dto.QuestaoDTO;

@RestController
@RequestMapping(value = "/api")
public class QuestaoController{

    @Autowired
    private QuestaoRepository questaoRepository;
    @Autowired
    private HabilidadeRepository habilidadeRepository;
    @Autowired
    private ConteudoRepository conteudoRepository;
    @Autowired
    private AreaConhecimentoRepository areaConhecimentoRepository;
    @Autowired
    private AlternativaRepository alternativaRepository;


    @PostMapping(value = "/createUpdateQuestao")
    public ResponseEntity<QuestaoDTO> createUpdateQuestao(@RequestBody QuestaoDTO questaoDTO){

        try {

            QuestaoEntity          questaoEntity    = new QuestaoEntity();
            ConteudoEntity         conteudo         = new ConteudoEntity();
            HabilidadeEntity       habilidade       = new HabilidadeEntity();
            AreaConhecimentoEntity areaConhecimento = new AreaConhecimentoEntity();

            
            conteudo.setId(questaoDTO.getConteudo().getId());
            habilidade.setId(questaoDTO.getHabilidade().getId());
            areaConhecimento.setId(questaoDTO.getAreaConhecimento().getId());

            questaoEntity.setConteudo(conteudo);
            questaoEntity.setHabilidade(habilidade);
            questaoEntity.setAreaConhecimento(areaConhecimento);
            
            questaoEntity.setId(questaoDTO.getId());
            questaoEntity.setDescricao(questaoDTO.getDescricao());
            questaoEntity.setEnade(questaoDTO.isEnade());
            questaoEntity.setStatus(questaoDTO.isStatus());
            questaoEntity.setDiscursiva(questaoDTO.isDiscursiva());
            questaoEntity.setFonte(questaoDTO.getFonte());
            questaoEntity.setAno(questaoDTO.getAno());
            questaoEntity.setAlterCorreta(questaoDTO.getAlterCorreta());
            questaoEntity.setImagem(questaoDTO.getImagem());

            questaoEntity = questaoRepository.save(questaoEntity);
            
            if(questaoEntity.getId() != null && questaoEntity.isDiscursiva() == false){
                for(AlternativaDTO alternativaDTO : questaoDTO.getAlternativas()){
                    AlternativaEntity alternativaEntity = new AlternativaEntity();
    
                    alternativaEntity.setId(alternativaDTO.getId());
                    alternativaEntity.setCorreta(alternativaDTO.isCorreta());
                    alternativaEntity.setDescricao(alternativaDTO.getDescricao());
                    alternativaEntity.setQuestao(questaoEntity);
                    
                    System.out.println(
                        "LOG - Alternativas\n" +
                        "ID " + alternativaEntity.getId() + "\n" + 
                        "Desc " + alternativaEntity.getDescricao() + "\n" +
                        "Correta " + alternativaEntity.isCorreta() + "\n" +
                        "ID Questão " + alternativaEntity.getQuestao().getId() + "\n"   
                    );
                    alternativaEntity = alternativaRepository.save(alternativaEntity);
                }
            }
            

            questaoDTO.setId(questaoEntity.getId());
            return ResponseEntity.ok(questaoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao criar ou alterar a questão");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Metodo para retornar todas as questões que estiverem no banco.
     */
    @GetMapping(value = "/getQuestoes") 
    public ResponseEntity<List<QuestaoDTO>> getQuestoes(){

        try {
            List<QuestaoDTO> questaoDTOs = new ArrayList<>();
            List<QuestaoEntity> questaoEntities = new ArrayList<>();


            questaoEntities = questaoRepository.findAll();
            for(QuestaoEntity qEntity : questaoEntities){
                QuestaoDTO          questaoDTO       = new QuestaoDTO();
                ConteudoDTO         conteudo         = new ConteudoDTO();
                HabilidadeDTO       habilidade       = new HabilidadeDTO();                
                AreaConhecimentoDTO areaConhecimento = new AreaConhecimentoDTO();
                List<AlternativaDTO> alternativas    = new ArrayList<>();


                HabilidadeEntity habilidadeEntity = habilidadeRepository.getOne(qEntity.getHabilidade().getId());
                habilidade.setId(habilidadeEntity.getId());
                habilidade.setDescription(habilidadeEntity.getDescription());
                habilidade.setStatus(habilidadeEntity.isStatus());

                ConteudoEntity conteudoEntity = conteudoRepository.getOne(qEntity.getConteudo().getId());
                conteudo.setId(conteudoEntity.getId());
                conteudo.setDescription(conteudoEntity.getDescription());
                conteudo.setStatus(conteudoEntity.isStatus());
                
                AreaConhecimentoEntity areaConhecimentoEntity = areaConhecimentoRepository.getOne(qEntity.getAreaConhecimento().getId());
                areaConhecimento.setId(areaConhecimentoEntity.getId());
                areaConhecimento.setDescription(areaConhecimentoEntity.getDescription());
                areaConhecimento.setStatus(areaConhecimentoEntity.isStatus());

                if(qEntity.isDiscursiva() == false){

                    List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(qEntity);

                    for(AlternativaEntity alter : alternativaEntities){
                        AlternativaDTO alternativaDTO = new AlternativaDTO();
    
                        alternativaDTO.setId(alter.getId());
                        alternativaDTO.setCorreta(alter.isCorreta());
                        alternativaDTO.setDescricao(alter.getDescricao());

                        alternativas.add(alternativaDTO);
                    }
                }


                questaoDTO.setId(qEntity.getId());
                questaoDTO.setDescricao(qEntity.getDescricao());
                questaoDTO.setEnade(qEntity.isEnade());
                questaoDTO.setStatus(qEntity.isStatus());
                questaoDTO.setDiscursiva(qEntity.isDiscursiva());
                questaoDTO.setFonte(qEntity.getFonte());
                questaoDTO.setAno(qEntity.getAno());
                questaoDTO.setAlterCorreta(qEntity.getAlterCorreta());
                questaoDTO.setImagem(qEntity.getImagem());

                questaoDTO.setHabilidade(habilidade);
                questaoDTO.setConteudo(conteudo);
                questaoDTO.setAreaConhecimento(areaConhecimento);
                questaoDTO.setAlternativas(alternativas);

                questaoDTOs.add(questaoDTO);
            }

            return ResponseEntity.ok(questaoDTOs);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar as questões");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /** 
     * Meto para retorna questão por ID 
    */
    @GetMapping(value = "/getQuestoes/id") 
    public ResponseEntity<QuestaoDTO> getQuestoesID(@RequestBody QuestaoDTO  questaoDTO){

        try {
            ConteudoDTO         conteudo         = new ConteudoDTO();
            HabilidadeDTO       habilidade       = new HabilidadeDTO();                
            AreaConhecimentoDTO areaConhecimento = new AreaConhecimentoDTO();
            List<AlternativaDTO> alternativas    = new ArrayList<>();

            QuestaoEntity questao = questaoRepository.getOne(questaoDTO.getId());

            HabilidadeEntity habilidadeEntity = habilidadeRepository.getOne(questao.getHabilidade().getId());
            habilidade.setId(habilidadeEntity.getId());
            habilidade.setDescription(habilidadeEntity.getDescription());
            habilidade.setStatus(habilidadeEntity.isStatus());

            ConteudoEntity conteudoEntity = conteudoRepository.getOne(questao.getConteudo().getId());
            conteudo.setId(conteudoEntity.getId());
            conteudo.setDescription(conteudoEntity.getDescription());
            conteudo.setStatus(conteudoEntity.isStatus());
                
            AreaConhecimentoEntity areaConhecimentoEntity = areaConhecimentoRepository.getOne(questao.getAreaConhecimento().getId());
            areaConhecimento.setId(areaConhecimentoEntity.getId());
            areaConhecimento.setDescription(areaConhecimentoEntity.getDescription());
            areaConhecimento.setStatus(areaConhecimentoEntity.isStatus());

            if(questao.isDiscursiva() == false){

                List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(questao);
                for(AlternativaEntity alter : alternativaEntities){
                    AlternativaDTO alternativaDTO = new AlternativaDTO();

                    alternativaDTO.setId(alter.getId());
                    alternativaDTO.setCorreta(alter.isCorreta());
                    alternativaDTO.setDescricao(alter.getDescricao());
                    alternativas.add(alternativaDTO);
                }
            }
            questaoDTO.setId(questao.getId());
            questaoDTO.setDescricao(questao.getDescricao());
            questaoDTO.setEnade(questao.isEnade());
            questaoDTO.setStatus(questao.isStatus());
            questaoDTO.setDiscursiva(questao.isDiscursiva());
            questaoDTO.setFonte(questao.getFonte());
            questaoDTO.setAno(questao.getAno());
            questaoDTO.setAlterCorreta(questao.getAlterCorreta());
            questaoDTO.setImagem(questao.getImagem());

            questaoDTO.setHabilidade(habilidade);
            questaoDTO.setConteudo(conteudo);
            questaoDTO.setAreaConhecimento(areaConhecimento);
            questaoDTO.setAlternativas(alternativas);
            
            return ResponseEntity.ok(questaoDTO);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar as questões");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}