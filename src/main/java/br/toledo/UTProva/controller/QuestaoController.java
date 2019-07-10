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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.AlternativaEntity;
import br.toledo.UTProva.model.dao.entity.AreaConhecimentoEntity;
import br.toledo.UTProva.model.dao.entity.ConteudoEntity;
import br.toledo.UTProva.model.dao.entity.FonteEntity;
import br.toledo.UTProva.model.dao.entity.HabilidadeEntity;
import br.toledo.UTProva.model.dao.entity.QuestaoEntity;
import br.toledo.UTProva.model.dao.entity.TipoQuestaoEntity;
import br.toledo.UTProva.model.dao.entity.TipoRespostaEntity;
import br.toledo.UTProva.model.dto.TipoQuestaoDTO;
import br.toledo.UTProva.model.dto.TipoRespostaDTO;
import br.toledo.UTProva.model.dao.repository.AlternativaRepository;
import br.toledo.UTProva.model.dao.repository.AreaConhecimentoRepository;
import br.toledo.UTProva.model.dao.repository.ConteudoRepository;
import br.toledo.UTProva.model.dao.repository.FonteRepository;
import br.toledo.UTProva.model.dao.repository.HabilidadeRepository;
import br.toledo.UTProva.model.dao.repository.QuestaoFilterRepository;
import br.toledo.UTProva.model.dao.repository.QuestaoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoQuestoesRepository;
import br.toledo.UTProva.model.dao.repository.TipoQuestaoRepository;
import br.toledo.UTProva.model.dao.serviceJDBC.AlternativaJDBC;
import br.toledo.UTProva.model.dto.AlternativaDTO;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;
import br.toledo.UTProva.model.dto.ConteudoDTO;
import br.toledo.UTProva.model.dto.FonteDTO;
import br.toledo.UTProva.model.dto.HabilidadeDTO;
import br.toledo.UTProva.model.dto.QuestaoDTO;
import br.toledo.UTProva.model.dto.QuestoesFilterDTO;

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
    private FonteRepository fonteRepository;
    @Autowired
    private AreaConhecimentoRepository areaConhecimentoRepository;
    @Autowired
    private TipoQuestaoRepository tipoQuestaoRepository;
    @Autowired
    private AlternativaRepository alternativaRepository;
    @Autowired
    private QuestaoFilterRepository questaoFilterRepository;
    @Autowired
    private SimuladoQuestoesRepository simuladoQuestoesRepository;
    @Autowired
    private AlternativaJDBC alternativaJDBC;


    /**
     * Metodo de criação e alteração de questão
     */
    @PostMapping(value = "/createUpdateQuestao")
    public ResponseEntity<Map> createUpdateQuestao(@RequestBody QuestaoDTO questaoDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {

            if(questaoDTO.getId() != null){
                int qtd = simuladoQuestoesRepository.countSimuladoAluno(questaoDTO.getId());
                if(qtd > 0){
                    map.put("success", false);
                    map.put("message", "Não foi possível efetuar a alteração. Motivo: A Questão possui vínculo com Simulado iniciado.");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }

            if(questaoDTO.getId() != null && questaoDTO.isStatus() == false){
                int qtd = simuladoQuestoesRepository.countSimuladoByQuestao(questaoDTO.getId());
                if(qtd > 0){
                    map.put("success", false);
                    map.put("message", "Não foi possível efetuar a inativação. Motivo: A Questão possui vínculo com Simulado.");
                    return new ResponseEntity<>(map, HttpStatus.OK);
                }
            }

        

            FonteEntity            fonte            = new FonteEntity();
            QuestaoEntity          questaoEntity    = new QuestaoEntity();
            ConteudoEntity         conteudo         = new ConteudoEntity();
            HabilidadeEntity       habilidade       = new HabilidadeEntity();
            TipoQuestaoEntity      tipo             = new TipoQuestaoEntity();
            TipoRespostaEntity     tipoResposta     = new TipoRespostaEntity();
            AreaConhecimentoEntity areaConhecimento = new AreaConhecimentoEntity();

            
            conteudo.setId(questaoDTO.getConteudo().getId());
            habilidade.setId(questaoDTO.getHabilidade().getId());
            areaConhecimento.setId(questaoDTO.getAreaConhecimento().getId());
            tipo.setId(questaoDTO.getTipo().getId());
            tipoResposta.setId(questaoDTO.getTipoResposta().getId());
            fonte.setId(questaoDTO.getFonte().getId());

            questaoEntity.setConteudo(conteudo);
            questaoEntity.setHabilidade(habilidade);
            questaoEntity.setAreaConhecimento(areaConhecimento);
            questaoEntity.setTipo(tipo);
            questaoEntity.setTipoResposta(tipoResposta);
            questaoEntity.setFonte(fonte);
            
            questaoEntity.setId(questaoDTO.getId());
            questaoEntity.setDescricao(questaoDTO.getDescricao());
            questaoEntity.setStatus(questaoDTO.isStatus());
            questaoEntity.setDiscursiva(true);
            questaoEntity.setDificuldade(questaoDTO.getDificuldade());
            questaoEntity.setAno(questaoDTO.getAno());
            questaoEntity.setAlterCorreta(questaoDTO.getAlterCorreta());
            questaoEntity.setImagem(questaoDTO.getImagem());
            questaoEntity = questaoRepository.save(questaoEntity);

            if(questaoEntity.getId() != null){
                alternativaJDBC.deleteAlternativa(questaoEntity.getId());
                System.out.println("Alternativas excluidas !");
            }
            
            if(questaoEntity.getId() != null && questaoDTO.getTipoResposta().getId() != 2){
                for(AlternativaDTO alternativaDTO : questaoDTO.getAlternativas()){
                    AlternativaEntity alternativaEntity = new AlternativaEntity();
    
                    alternativaEntity.setId(alternativaDTO.getId());
                    alternativaEntity.setCorreta(alternativaDTO.isCorreta());
                    alternativaEntity.setDescricao(alternativaDTO.getDescricao());
                    alternativaEntity.setQuestao(questaoEntity);
                    alternativaEntity = alternativaRepository.save(alternativaEntity);
                }
            }
            

            map.put("success", true);
            map.put("message", "Questão salva com sucesso !");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao criar ou alterar a questão");
            map.put("success", false);
            map.put("message", "Erro ao salvar a Questão !");         
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/deleteQuestao") 
    public ResponseEntity<Map> deleteQuestao(@RequestBody QuestaoDTO  questaoDTO){
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            int qtd = simuladoQuestoesRepository.countSimuladoByQuestao(questaoDTO.getId());
            if(qtd > 0){
                map.put("success", false);
                map.put("message", "Questão vinculada a simulados");
            }else{
                QuestaoEntity questao = questaoRepository.getOne(questaoDTO.getId());
                if(questao.isDiscursiva() == false){
    
                    List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(questao);
                    for(AlternativaEntity alter : alternativaEntities){
                        alternativaRepository.delete(alter);
                    }
                }
                questaoRepository.delete(questao);

                map.put("success", true);
                map.put("message", "Questão excluida");
            }
            return new ResponseEntity<>(map, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "Erro ao excluir a questão");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getQuestoesSimulado/{source}") 
    public ResponseEntity<List<QuestaoDTO>> getQuestoesSimulado(@PathVariable("source") String source,
                                                                @RequestBody QuestoesFilterDTO questoesFilterDTO){

        try {
            int qtdFilter = 0;
        
            String sql = " ";
            List<Long>   idsLongs               = new ArrayList<>();
            List<Long>   fonteLongs             = new ArrayList<>();
            List<Long>   habilidadeLongs        = new ArrayList<>();
            List<Long>   conteudosLongs         = new ArrayList<>();
            List<Long>   areConhecimentosLongs  = new ArrayList<>();
            List<Long>   tipoQuestaoLongs       = new ArrayList<>();
            List<String> anos                   = new ArrayList<>();
            

            if(source.equalsIgnoreCase("simulado")){
                sql += "where q.status = true ";
                qtdFilter++;
            }

            if(!questoesFilterDTO.getCodigos().isEmpty()) {   
                questoesFilterDTO.getCodigos().forEach(n -> idsLongs.add(n));             
                if(qtdFilter == 0) {
                    sql += "where q.id " + createIn(idsLongs);
                }else {
                    sql +=" and q.id " + createIn(idsLongs);
                }
                qtdFilter++;
            }  

            if(questoesFilterDTO.getEnade().equalsIgnoreCase("S") || questoesFilterDTO.getEnade().equalsIgnoreCase("N")) {
                boolean enade = false;
                if(questoesFilterDTO.getEnade().equalsIgnoreCase("S")) enade = true;
                if(qtdFilter == 0) {
                    sql += "where enade = " + enade;
                }else {
                    sql +=" and enade = " + enade;
                }
                qtdFilter++;
            } 
    
            if(questoesFilterDTO.getDiscursiva().equalsIgnoreCase("S") || questoesFilterDTO.getDiscursiva().equalsIgnoreCase("N")) {
                Long discursiva = 1l;
                if(questoesFilterDTO.getDiscursiva().equalsIgnoreCase("S")) discursiva = 2l;
if(qtdFilter == 0) {
                    sql += " where tipo_resposta_id = " + discursiva;
                }else {
                    sql +=" and tipo_resposta_id = " + discursiva;
                }
                qtdFilter++;
            }
            
            if(!questoesFilterDTO.getDificuldade().isEmpty()) {
                if(qtdFilter == 0) {
                    sql += " where dificuldade like '%" + questoesFilterDTO.getDificuldade() + "%' ";
                }else {
                    sql += " and dificuldade like '%" + questoesFilterDTO.getDificuldade() + "%' ";
                }
                qtdFilter++;
            }
            
    
            if(!questoesFilterDTO.getHabilidades().isEmpty()){
                questoesFilterDTO.getHabilidades().forEach(n -> habilidadeLongs.add(n.getId()));
                if(qtdFilter == 0) 
                    sql += " where q.habilidade_id " + createIn(habilidadeLongs);
                else
                    sql +=" and q.habilidade_id " + createIn(habilidadeLongs);
                
                qtdFilter++ ;
            }
            
            if(!questoesFilterDTO.getConteudos().isEmpty()){
                questoesFilterDTO.getConteudos().forEach(n -> conteudosLongs.add(n.getId()));
                if(qtdFilter == 0) 
                    sql += " where q.conteudo_id " + createIn(conteudosLongs);
                else
                    sql += " and q.conteudo_id " + createIn(conteudosLongs);
                
                qtdFilter++ ;
            }
    
            if(!questoesFilterDTO.getAreaConhecimentos().isEmpty()){
                questoesFilterDTO.getAreaConhecimentos().forEach(n -> areConhecimentosLongs.add(n.getId()));
                if(qtdFilter == 0) 
                    sql += " where q.area_conhecimento_id " + createIn(areConhecimentosLongs);
                else
                    sql += " and q.area_conhecimento_id " + createIn(areConhecimentosLongs);
            
                qtdFilter++ ;
            }

            if(!questoesFilterDTO.getFonte().isEmpty()){
                questoesFilterDTO.getFonte().forEach(n -> fonteLongs.add(n.getId()));
                if(qtdFilter == 0){
                    sql += " where q.fonte_id " + createIn(fonteLongs);
                }else{
                    sql += " and q.fonte_id " + createIn(fonteLongs);
                }
                qtdFilter++ ;
            }
            
            
            if(questoesFilterDTO.getTipo().getId() == 1 || questoesFilterDTO.getTipo().getId() == 2) {
                if(qtdFilter == 0) {
                    sql += " where tipo_id = " + questoesFilterDTO.getTipo().getId();
                }else {
                    sql +=" and tipo_id = " + questoesFilterDTO.getTipo().getId();
                }
                qtdFilter++;
            }

            if(!questoesFilterDTO.getAnos().isEmpty()){
                questoesFilterDTO.getAnos().forEach(n -> anos.add(n.getAno()));
                if(qtdFilter == 0) 
                    sql += " where ano " + createInString(anos);
                else
                    sql += " and ano " + createInString(anos);
            
                qtdFilter++ ;
            }
    
            
            
            System.out.println("Causula where " + sql);
            

            List<QuestaoDTO> questaoDTO = questaoFilterRepository.joinQuestao(sql);
            // List<QuestaoDTO> questaoDTO = new ArrayList<>();
            return ResponseEntity.ok(questaoDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     }

     private String createIn(List<Long> ids){
        int size = 0;    
        String in = "";
        try {
            in = " in(";
        while( size < ids.size()){
            in += ids.get(size).toString();
            size++;
            if(size < ids.size()){
                in += ", ";
            }
        }
        in += ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
     }

     private String createInString(List<String> ids){
        int size = 0;    
        String in = "";
        try {
            in = " in(";
        while( size < ids.size()){
            in += "'" + ids.get(size).toString() + "'";
            size++;
            if(size < ids.size()){
                in += ", ";
            }
        }
        in += ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
     }




     /**
     * Metodo para retornar todas as questões que estiverem no banco.
     */
    // @GetMapping(value = "/getQuestoes") 
    // public ResponseEntity<List<QuestaoDTO>> getQuestoes(){

    //     try {
    //         List<QuestaoDTO> questaoDTOs = new ArrayList<>();
    //         List<QuestaoEntity> questaoEntities = new ArrayList<>();
            
    //         questaoEntities = questaoRepository.findAll();
            
            
    //         for(QuestaoEntity qEntity : questaoEntities){
    //             FonteDTO            fonte            = new FonteDTO();
    //             QuestaoDTO          questaoDTO       = new QuestaoDTO();
    //             ConteudoDTO         conteudo         = new ConteudoDTO();
    //             HabilidadeDTO       habilidade       = new HabilidadeDTO();                
    //             AreaConhecimentoDTO areaConhecimento = new AreaConhecimentoDTO();
    //             TipoQuestaoDTO      tipo             = new TipoQuestaoDTO();
    //             TipoRespostaDTO     tipoResposta     = new TipoRespostaDTO();
    //             List<AlternativaDTO> alternativas    = new ArrayList<>();


    //             HabilidadeEntity habilidadeEntity = habilidadeRepository.getOne(qEntity.getHabilidade().getId());
    //             habilidade.setId(habilidadeEntity.getId());
    //             habilidade.setDescription(habilidadeEntity.getDescription());
    //             habilidade.setStatus(habilidadeEntity.isStatus());

    //             ConteudoEntity conteudoEntity = conteudoRepository.getOne(qEntity.getConteudo().getId());
    //             conteudo.setId(conteudoEntity.getId());
    //             conteudo.setDescription(conteudoEntity.getDescription());
    //             conteudo.setStatus(conteudoEntity.isStatus());
                
    //             AreaConhecimentoEntity areaConhecimentoEntity = areaConhecimentoRepository.getOne(qEntity.getAreaConhecimento().getId());
    //             areaConhecimento.setId(areaConhecimentoEntity.getId());
    //             areaConhecimento.setDescription(areaConhecimentoEntity.getDescription());
    //             areaConhecimento.setStatus(areaConhecimentoEntity.isStatus());

    //             FonteEntity fonteEntity = fonteRepository.getOne(qEntity.getFonte().getId());
    //             fonte.setId(fonteEntity.getId());
    //             fonte.setDescription(fonteEntity.getDescription());
    //             fonte.setStatus(fonteEntity.isStatus());

    //             TipoQuestaoEntity tipoQuestaoEntity = tipoQuestaoRepository.getOne(qEntity.getTipo().getId());
    //             tipo.setId(tipoQuestaoEntity.getId());
    //             tipo.setDescricao(tipoQuestaoEntity.getDescricao());

    //             if(qEntity.isDiscursiva() == false){

    //                 List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(qEntity);

    //                 for(AlternativaEntity alter : alternativaEntities){
    //                     AlternativaDTO alternativaDTO = new AlternativaDTO();
    
    //                     alternativaDTO.setId(alter.getId());
    //                     alternativaDTO.setCorreta(alter.isCorreta());
    //                     alternativaDTO.setDescricao(alter.getDescricao());

    //                     alternativas.add(alternativaDTO);
    //                 }
    //             }


    //             questaoDTO.setId(qEntity.getId());
    //             questaoDTO.setDescricao(qEntity.getDescricao());
    //             questaoDTO.setEnade(qEntity.isEnade());
    //             questaoDTO.setStatus(qEntity.isStatus());
    //             questaoDTO.setDiscursiva(qEntity.isDiscursiva());
    //             questaoDTO.setDificuldade(qEntity.getDificuldade());
    //             questaoDTO.setAno(qEntity.getAno());
    //             questaoDTO.setAlterCorreta(qEntity.getAlterCorreta());
    //             questaoDTO.setImagem(qEntity.getImagem());

    //             questaoDTO.setHabilidade(habilidade);
    //             questaoDTO.setConteudo(conteudo);
    //             questaoDTO.setAreaConhecimento(areaConhecimento);
    //             questaoDTO.setTipo(tipo);
    //             questaoDTO.setFonte(fonte);
    //             questaoDTO.setAlternativas(alternativas);

    //             questaoDTOs.add(questaoDTO);
    //         }

    //         return ResponseEntity.ok(questaoDTOs);

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Erro ao buscar as questões");
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }


    /** 
     * Meto para retorna questão por ID 
    */
    // @GetMapping(value = "/getQuestao") 
    // public ResponseEntity<QuestaoDTO> getQuestoesID(@RequestBody QuestaoDTO  questaoDTO){

    //     try {
    //         ConteudoDTO         conteudo         = new ConteudoDTO();
    //         HabilidadeDTO       habilidade       = new HabilidadeDTO();                
    //         AreaConhecimentoDTO areaConhecimento = new AreaConhecimentoDTO();
    //         FonteDTO            fonte            = new FonteDTO();
    //         TipoQuestaoDTO      tipo             = new TipoQuestaoDTO();
    //         List<AlternativaDTO> alternativas    = new ArrayList<>();

    //         QuestaoEntity questao = questaoRepository.getOne(questaoDTO.getId());

    //         HabilidadeEntity habilidadeEntity = habilidadeRepository.getOne(questao.getHabilidade().getId());
    //         habilidade.setId(habilidadeEntity.getId());
    //         habilidade.setDescription(habilidadeEntity.getDescription());
    //         habilidade.setStatus(habilidadeEntity.isStatus());

    //         ConteudoEntity conteudoEntity = conteudoRepository.getOne(questao.getConteudo().getId());
    //         conteudo.setId(conteudoEntity.getId());
    //         conteudo.setDescription(conteudoEntity.getDescription());
    //         conteudo.setStatus(conteudoEntity.isStatus());
                
    //         AreaConhecimentoEntity areaConhecimentoEntity = areaConhecimentoRepository.getOne(questao.getAreaConhecimento().getId());
    //         areaConhecimento.setId(areaConhecimentoEntity.getId());
    //         areaConhecimento.setDescription(areaConhecimentoEntity.getDescription());
    //         areaConhecimento.setStatus(areaConhecimentoEntity.isStatus());

    //         FonteEntity fonteEntity = fonteRepository.getOne(questao.getFonte().getId());
    //         fonte.setId(fonteEntity.getId());
    //         fonte.setDescription(fonteEntity.getDescription());
    //         fonte.setStatus(fonteEntity.isStatus());

    //         TipoQuestaoEntity tipoQuestaoEntity = tipoQuestaoRepository.getOne(questao.getTipo().getId());
    //         tipo.setId(tipoQuestaoEntity.getId());
    //         tipo.setDescricao(tipoQuestaoEntity.getDescricao());

    //         if(questao.isDiscursiva() == false){

    //             List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(questao);
    //             for(AlternativaEntity alter : alternativaEntities){
    //                 AlternativaDTO alternativaDTO = new AlternativaDTO();

    //                 alternativaDTO.setId(alter.getId());
    //                 alternativaDTO.setCorreta(alter.isCorreta());
    //                 alternativaDTO.setDescricao(alter.getDescricao());
    //                 alternativas.add(alternativaDTO);
    //             }
    //         }
    //         questaoDTO.setId(questao.getId());
    //         questaoDTO.setDescricao(questao.getDescricao());
    //         questaoDTO.setEnade(questao.isEnade());
    //         questaoDTO.setStatus(questao.isStatus());
    //         questaoDTO.setDiscursiva(questao.isDiscursiva());
    //         questaoDTO.setDificuldade(questao.getDificuldade());
    //         questaoDTO.setAno(questao.getAno());
    //         questaoDTO.setAlterCorreta(questao.getAlterCorreta());
    //         questaoDTO.setImagem(questao.getImagem());

    //         questaoDTO.setHabilidade(habilidade);
    //         questaoDTO.setConteudo(conteudo);
    //         questaoDTO.setAreaConhecimento(areaConhecimento);
    //         questaoDTO.setTipo(tipo);
    //         questaoDTO.setFonte(fonte);
    //         questaoDTO.setAlternativas(alternativas);
            
    //         return ResponseEntity.ok(questaoDTO);

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Erro ao buscar as questões");
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }
   
}