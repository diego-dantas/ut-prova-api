package br.toledo.UTProva.model.dao.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import br.toledo.UTProva.model.dao.entity.AlunoQuestaoDiscursiva;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoResolucaoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoStatusAlunoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoStatusEntity;
import br.toledo.UTProva.model.dao.entity.TipoQuestaoEntity;
import br.toledo.UTProva.model.dto.AlternativaDTO;
import br.toledo.UTProva.model.dto.AlternativaRetornoDTO;
import br.toledo.UTProva.model.dto.AreaConhecimentoDTO;
import br.toledo.UTProva.model.dto.ConteudoDTO;
import br.toledo.UTProva.model.dto.FonteDTO;
import br.toledo.UTProva.model.dto.HabilidadeDTO;
import br.toledo.UTProva.model.dto.QuestaoDTO;
import br.toledo.UTProva.model.dto.QuestaoRetornoDTO;
import br.toledo.UTProva.model.dto.TipoQuestaoDTO;
import br.toledo.UTProva.model.dto.TipoRespostaDTO;



@Service
public class QuestaoFilterRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimuladoResolucaoRepository simuladoResolucaoRepository;
    @Autowired
    private SimuladoStatusAlunoRepository simuladoStatusAlunoRepository;
    @Autowired
    private AlunoQuestaoDiscursivaRepository alunoQuestaoDiscursivaRepository;
    
    public List<Long> questaoFilter(String sql){
       
        try {
            List<QuestaoDTO> questaoDTO = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<QuestaoDTO>() {
            public QuestaoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                QuestaoDTO questaoDTO = new QuestaoDTO();
                questaoDTO.setId(rs.getLong("id"));
                questaoDTO.setDescricao(rs.getString("descricao"));
                return questaoDTO;
            }
            });

            List<Long> ids = new ArrayList<>();
            questaoDTO.forEach(n -> ids.add( n.getId() )) ;
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<QuestaoDTO> joinQuestao(String sql){
        String sql2 = 
        "select 	q.id as id_questao, "+
            "q.descricao as descricao_questao, "+
            "q.alternativa_correta, "+
            "q.ano, "+
            "q.dificuldade, "+   
            "q.imagem , "+
            "q.status as status_questao, "+
            "h.id as id_habilidade, "+
            "h.description as descricao_habilidade, "+
            "c.id as id_conteudo, "+
            "c.description as descricao_conteudo, "+
            "ac.id as id_area_conhecimento, "+
            "ac.description as descricao_area_conhecimento, "+
            "tq.id as id_tipo, "+
            "tq.descricao as descricao_tipo, "+
            "f.id as id_fonte, " + 
            "f.description as descricao_fonte, " +
            "tr.id as id_tipo_resposta, " +
            "tr.descricao as descricao_tipo_resposta " +
        "from questoes q "+
        "join habilidades h on h.id = q.habilidade_id "+
        "join conteudos c on c.id = q.conteudo_id "+
        "join areas_conhecimento ac on ac.id = q.area_conhecimento_id "+
        "join tipo_questao tq on tq.id = q.tipo_id " + 
        "join tipo_resposta tr on tr.id = q.tipo_resposta_id " +
        "join fonte f on f.id = q.fonte_id \n" + sql;

        try {
            List<QuestaoDTO> questaoDTO = this.jdbcTemplate.query(sql2,
            new Object[]{},
            new RowMapper<QuestaoDTO>() {
            public QuestaoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                
                QuestaoDTO          questaoDTO       = new QuestaoDTO();
                ConteudoDTO         conteudo         = new ConteudoDTO();
                HabilidadeDTO       habilidade       = new HabilidadeDTO();
                FonteDTO            fonte            = new FonteDTO(); 
                TipoQuestaoDTO      tipo             = new TipoQuestaoDTO();
                TipoRespostaDTO     tipoResposta     = new TipoRespostaDTO();
                AreaConhecimentoDTO areaConhecimento = new AreaConhecimentoDTO();
                
                questaoDTO.setId(rs.getLong("id_questao"));
                questaoDTO.setDescricao(rs.getString("descricao_questao"));
                questaoDTO.setStatus(rs.getBoolean("status_questao"));
                questaoDTO.setDificuldade(rs.getString("dificuldade"));
                questaoDTO.setAno(rs.getString("ano"));                
                questaoDTO.setAlterCorreta(rs.getString("alternativa_correta").charAt(0));
                questaoDTO.setImagem(rs.getString("imagem"));

            
                
                habilidade.setId(rs.getLong("id_habilidade"));
                habilidade.setDescription(rs.getString("descricao_habilidade"));
                // habilidade.setStatus(habilidadeEntity.isStatus());

                
                conteudo.setId(rs.getLong("id_conteudo"));
                conteudo.setDescription(rs.getString("descricao_conteudo"));
                //conteudo.setStatus(conteudoEntity.isStatus());
                
                
                areaConhecimento.setId(rs.getLong("id_area_conhecimento"));
                areaConhecimento.setDescription(rs.getString("descricao_area_conhecimento"));
                //areaConhecimento.setStatus(areaConhecimentoEntity.isStatus());

                fonte.setId(rs.getLong("id_fonte"));
                fonte.setDescription(rs.getString("descricao_fonte"));

                
                tipo.setId(rs.getLong("id_tipo"));
                tipo.setDescricao(rs.getString("descricao_tipo"));

                tipoResposta.setId(rs.getLong("id_tipo_resposta"));
                tipoResposta.setDescricao(rs.getString("descricao_tipo_resposta"));
                
                questaoDTO.setTipo(tipo);
                questaoDTO.setFonte(fonte);
                questaoDTO.setConteudo(conteudo);
                questaoDTO.setHabilidade(habilidade);
                questaoDTO.setTipoResposta(tipoResposta);
                questaoDTO.setAreaConhecimento(areaConhecimento);
                
                
                
                return questaoDTO;
            }
            });

            List<QuestaoDTO> questoes = new ArrayList<>();
            for (QuestaoDTO q  : questaoDTO) {
                QuestaoDTO questao = new QuestaoDTO();
                questao.setId(q.getId());
                questao.setDescricao(q.getDescricao());

                List<AlternativaDTO> alternativaDTOs = this.jdbcTemplate.query("select * from alternativa where questao_id = " + q.getId(),
                new Object[]{},
                new RowMapper<AlternativaDTO>() {
                public AlternativaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AlternativaDTO alternativaDTO = new AlternativaDTO();
                    alternativaDTO.setId(rs.getLong("id"));
                    alternativaDTO.setDescricao(rs.getString("descricao"));
                    alternativaDTO.setCorreta(rs.getBoolean("correta"));
                    
                    return alternativaDTO;
                }
                });

                questao.setAlternativas(alternativaDTOs);
                questoes.add(questao);
            }
           


            return questaoDTO;
        } catch (Exception e) {
            e.printStackTrace();        }
        return null;
    }


    public List<QuestaoRetornoDTO> getQuestao(Long idSimulado, String idAluno, String nomeAluno){
        
        String sql2 = "select q.id, q.descricao, q.imagem, sr.id_alternativa "+
                      "from questoes q "+
                      "join simulado_questoes sq on sq.questao_id = q.id "+
                      "left join simulado_resolucao sr on sr.id_questao = q.id and sr.id_aluno = '" + idAluno + "' and sq.simulado_id = sr.id_simulado "+
                      "where q.tipo_resposta_id = 1 " +
                      "and sq.simulado_id = " + idSimulado;
                      
 
         try {
            String sqlStatus = "select count(id) from simulado_status_aluno where id_aluno = '" + idAluno + "' and simulado_id = " + idSimulado;
            int qtd = this.jdbcTemplate.queryForObject(sqlStatus, Integer.class);


            if(qtd == 0){

                
                SimuladoEntity simulado = new SimuladoEntity();
                simulado.setId(idSimulado);
                
                // inicia o status do simulado 
                SimuladoStatusEntity status = new SimuladoStatusEntity();
                status.setId(2l);

                
                
                String sqlQ = 
                        " select distinct(questao_id) from simulado_questoes sq " +
                        " inner join questoes q on q.id = sq.questao_id " +
                        " where tipo_resposta_id = 1 " + 
                        " and sq.simulado_id = " + idSimulado;
                List<Long> idsSimulados = this.jdbcTemplate.queryForList(sqlQ, Long.class);

              
                // Randomiza os ids das questoes
                Collections.shuffle(idsSimulados);

                List<SimuladoResolucaoEntity> sis = new ArrayList<>();
                for(Long idQuestao : idsSimulados){
                    SimuladoResolucaoEntity simuladoResolucaoEntity = new SimuladoResolucaoEntity();
                    
                    simuladoResolucaoEntity.setId(0l);
                    simuladoResolucaoEntity.setIdAlternativa(0l);
                    simuladoResolucaoEntity.setIdQuestao(idQuestao);
                    simuladoResolucaoEntity.setIdSimulado(idSimulado);
                    simuladoResolucaoEntity.setIdAluno(idAluno);
                    
                    sis.add(simuladoResolucaoEntity);
                }

                simuladoResolucaoRepository.saveAll(sis);

                Date data_atual = new Date();
                SimuladoStatusAlunoEntity simuAlunoEntity = new SimuladoStatusAlunoEntity();
                simuAlunoEntity.setIdAluno(idAluno);
                simuAlunoEntity.setNomeAluno(nomeAluno);
                simuAlunoEntity.setSimulado(simulado);
                simuAlunoEntity.setSimuladoStatus(status);
                simuAlunoEntity.setDataInicio(data_atual);

                simuladoStatusAlunoRepository.save(simuAlunoEntity);


                String sqlDiscursivas = 
                        "select  " + 
                        "     distinct(tq.id) as id " + 
                        " from simulado_questoes sq " + 
                        " inner join questoes q on q.id = sq.questao_id and q.tipo_resposta_id = 2 " + 
                        " inner join tipo_questao tq on tq.id  = q.tipo_id " + 
                        " where sq.simulado_id = " + idSimulado +  
                        " order by tq.id "; 
                List<Long> tipoQuestaoEntities = this.jdbcTemplate.queryForList(sqlDiscursivas, Long.class);
                if (tipoQuestaoEntities.size() > 0) {
                    AlunoQuestaoDiscursiva alunoQuestaoDiscursiva = new AlunoQuestaoDiscursiva();                    
                
                    alunoQuestaoDiscursiva.setIdAluno(idAluno);
                    alunoQuestaoDiscursiva.setNomeAluno(nomeAluno);
                    alunoQuestaoDiscursiva.setSimulado(simulado); 
                    tipoQuestaoEntities.forEach(n -> {
                        if(n == 1) alunoQuestaoDiscursiva.setNotaFormacaoGeral("0");                    
                    });
                    tipoQuestaoEntities.forEach(n -> {
                        if(n == 2) alunoQuestaoDiscursiva.setNotaConhecimentoEspecifico("0");                    
                    });
                    alunoQuestaoDiscursivaRepository.save(alunoQuestaoDiscursiva);
                }
                    
            }

            
             List<QuestaoRetornoDTO> questaoDTO = this.jdbcTemplate.query(sql2,
             new Object[]{},
             new RowMapper<QuestaoRetornoDTO>() {
             public QuestaoRetornoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                QuestaoRetornoDTO questao = new QuestaoRetornoDTO();
                questao.setId(rs.getLong("id"));
                questao.setDescricao(rs.getString("descricao"));
                questao.setImagem(rs.getString("imagem"));
                questao.setRespondida(rs.getLong("id_alternativa"));
                 
                 return questao;
             }
             });
 
            List<QuestaoRetornoDTO> questoes = new ArrayList<>();
            for (QuestaoRetornoDTO q  : questaoDTO) {
                QuestaoRetornoDTO questao = new QuestaoRetornoDTO();
                 questao.setId(q.getId());
                 questao.setDescricao(q.getDescricao());
                 questao.setImagem(q.getImagem());
                 questao.setRespondida(q.getRespondida());

                 List<AlternativaRetornoDTO> alternativaDTOs = this.jdbcTemplate.query("select * from alternativa where questao_id = " + q.getId(),
                 new Object[]{},
                 new RowMapper<AlternativaRetornoDTO>() {
                 public AlternativaRetornoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AlternativaRetornoDTO alternativaDTO = new AlternativaRetornoDTO();
                     alternativaDTO.setId(rs.getLong("id"));
                     alternativaDTO.setDescricao(rs.getString("descricao"));
                     
                     return alternativaDTO;
                 }
                 });
 
                 questao.setAlternativas(alternativaDTOs);
                 questoes.add(questao);
             }
            
 
 
             return questoes;
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null;
     }
}