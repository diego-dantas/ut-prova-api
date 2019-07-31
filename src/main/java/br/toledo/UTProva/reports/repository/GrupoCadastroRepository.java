package br.toledo.UTProva.reports.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.toledo.UTProva.model.dao.repository.ReportsRepository;
import br.toledo.UTProva.model.dao.serviceJDBC.useful.FormatDecimal;
import br.toledo.UTProva.reports.dto.AreaConhecimentoReports;
import br.toledo.UTProva.reports.dto.ConteudoReports;
import br.toledo.UTProva.reports.dto.GrupoCadastro;
import br.toledo.UTProva.reports.dto.HabilidadesReports;
import org.springframework.jdbc.core.RowMapper;


@Service
public class GrupoCadastroRepository{


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReportsRepository reportsRepository;
    

    public GrupoCadastro repoGrupoCadastro(Long idSimulado){

        try {
            GrupoCadastro grupoCadastro = new GrupoCadastro();    
    
            grupoCadastro.setAreaConhecimento(areaConhecimentoReports(idSimulado));
            grupoCadastro.setConteudos(conteudosReports(idSimulado));
            grupoCadastro.setHabilidades(habilidadesReports(idSimulado));


            return grupoCadastro;
        } catch (Exception e) {
            System.err.println("Erro na hora de buscar os valores do banco " + e);
            return null;
        }
    }



    public List<AreaConhecimentoReports> areaConhecimentoReports(Long idSimulado){

        try {
            String sql = 
                " select  	    id_simulado as id_simulado, " + 
                " ac.description as area_conhecimento,                           " + 
                "             count(sr.id_questao)   as questoes_respondidas,  " + 
                "             (  " + 
                "                 SELECT COUNT( sr.id )  " + 
                "                 FROM simulado_resolucao sr " + 
                "                 JOIN alternativa a ON a.questao_id = sr.id_questao AND sr.id_alternativa = a.id " + 
                "                 JOIN questoes qt ON qt.id = sr.id_questao " + 
                "                 WHERE sr.id_simulado = ssa.simulado_id " + 
                "                 AND qt.area_conhecimento_id = ac.id " + 
                "                 AND correta = true " + 
                "             )  " + 
                "             as questoes_certas 						 " + 
                " from 	    simulado_status_aluno ssa  " + 
                " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
                " join 		alternativa a on a.id = sr.id_alternativa     " + 
                " join 		questoes q on q.id = sr.id_questao " + 
                " join 		tipo_questao tq on tq.id = q.tipo_id " + 
                " join       areas_conhecimento ac on ac.id = q.area_conhecimento_id " + 
                " where 	    ssa.simulado_id = " + idSimulado +  
                " and 		ssa.simulado_status_id 	= 3            " + 
                " group by  " + 
                "     ac.description, " + 
                "     id_simulado " 
            ;

            List<AreaConhecimentoReports> areaConhecimentoReports = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<AreaConhecimentoReports>(){
                    public AreaConhecimentoReports mapRow(ResultSet rs, int numRow) throws SQLException{

                        AreaConhecimentoReports aReports = new AreaConhecimentoReports();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                        aReports.setId(rs.getLong("id_simulado"));
                        aReports.setDescricao(rs.getString("area_conhecimento"));
                        aReports.setPercentual(FormatDecimal.formatDecimal(percentual));
                        return aReports;
                    }
                }
            );
            return areaConhecimentoReports;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<ConteudoReports> conteudosReports(Long idSimulado){

        try {
            String sql = 
                " select  	    id_simulado as id_simulado, " + 
                "             c.description as conteudo,                           " + 
                "             count(sr.id_questao)   as questoes_respondidas,  " + 
                "             (  " + 
                "                 SELECT COUNT( sr.id )  " + 
                "                 FROM simulado_resolucao sr " + 
                "                 JOIN alternativa a ON a.questao_id = sr.id_questao " + 
                "                 AND sr.id_alternativa = a.id " + 
                "                 JOIN questoes qt ON qt.id = sr.id_questao " + 
                "                 WHERE sr.id_simulado = ssa.simulado_id " + 
                "                 AND qt.conteudo_id = c.id " + 
                "                 AND correta = true " + 
                "             )  " + 
                "             as questoes_certas 						 " + 
                " from 	    simulado_status_aluno ssa  " + 
                " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
                " join 		alternativa a on a.id = sr.id_alternativa     " + 
                " join 		questoes q on q.id = sr.id_questao " + 
                " join 		tipo_questao tq on tq.id = q.tipo_id " + 
                " join       conteudos c on c.id = q.conteudo_id " + 
                " where 	    ssa.simulado_id = " + idSimulado + 
                " and 		ssa.simulado_status_id 	= 3            " + 
                " group by  " + 
                "     c.description, " + 
                "     id_simulado " 
            ;

            List<ConteudoReports> conteudoReports = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<ConteudoReports>(){
                    public ConteudoReports mapRow(ResultSet rs, int numRow) throws SQLException{

                        ConteudoReports cReports = new ConteudoReports();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                        cReports.setId(rs.getLong("id_simulado"));
                        cReports.setDescricao(rs.getString("conteudo"));
                        cReports.setPercentual(FormatDecimal.formatDecimal(percentual));
                        return cReports;
                    }
                }
            );
            return conteudoReports;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<HabilidadesReports> habilidadesReports(Long idSimulado){

        try {
            String sql = 
                " select  	    id_simulado as id_simulado, " + 
                "             h.description as habilidade,                           " + 
                "             count(sr.id_questao)   as questoes_respondidas,  " + 
                "             (  " + 
                "                 SELECT COUNT( sr.id )  " + 
                "                 FROM simulado_resolucao sr " + 
                "                 JOIN alternativa a ON a.questao_id = sr.id_questao AND sr.id_alternativa = a.id " + 
                "                 JOIN questoes qt ON qt.id = sr.id_questao " + 
                "                 WHERE sr.id_simulado = ssa.simulado_id " + 
                "                 AND qt.habilidade_id = h.id " + 
                "                 AND correta = true " + 
                "             )  " + 
                "             as questoes_certas 						 " + 
                " from 	    simulado_status_aluno ssa  " + 
                " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
                " join 		alternativa a on a.id = sr.id_alternativa     " + 
                " join 		questoes q on q.id = sr.id_questao " + 
                " join 		tipo_questao tq on tq.id = q.tipo_id " + 
                " join       habilidades h on h.id = q.habilidade_id " + 
                " where 	    ssa.simulado_id = " + idSimulado +  
                " and 		ssa.simulado_status_id 	= 3            " + 
                " group by  " + 
                "     h.description, " + 
                "     id_simulado " 
                ;

            List<HabilidadesReports> habilidadesReports = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<HabilidadesReports>(){
                    public HabilidadesReports mapRow(ResultSet rs, int numRow) throws SQLException{

                        HabilidadesReports hReports = new HabilidadesReports();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                        hReports.setId(rs.getLong("id_simulado"));
                        hReports.setDescricao(rs.getString("habilidade"));
                        hReports.setPercentual(FormatDecimal.formatDecimal(percentual));
                        return hReports;
                    }
                }
            );
            return habilidadesReports;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}