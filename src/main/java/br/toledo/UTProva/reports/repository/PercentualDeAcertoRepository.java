package br.toledo.UTProva.reports.repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import br.toledo.UTProva.model.dao.repository.ReportsRepository;
import br.toledo.UTProva.model.dto.SimuladoDashAluno;
import br.toledo.UTProva.reports.dto.PercentualDeAcerto;
import br.toledo.UTProva.reports.dto.PercentualDeAcertoCSV;




@Service
public class PercentualDeAcertoRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReportsRepository reportsRepository;


    public Map<String, Object> reports(Long idSimulado, Long tipo){
        
        String in = "";
        if(tipo == 1){
            in = "in(1)";
        }else if(tipo == 2){
            in = "in(2)";
        }else{
            in = "in(1, 2)";
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String sql = 
                " select 	id_simulado as id_simulado, " + 
                "			ssa.id_aluno as id_aluno, " + 
                "			ssa.nome_aluno as nome, " + 
                "			count(distinct(sr.id_questao))   as questoes_respondidas,  " + 
                "            (  " + 
                "            	select count(*)  " + 
                "            	from simulado_resolucao sr  " + 
                "            	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
                "                join questoes qt on qt.id =  sr.id_questao " + 
                "            	where sr.id_aluno = ssa.id_aluno  " + 
                "            	and sr.id_simulado = ssa.simulado_id  " + 
                "                and qt.tipo_id " + in +
                "                and correta = true  " + 
                "            )  " + 
                "            as questoes_certas " + 
                " from 	    simulado_status_aluno ssa  " + 
                " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
                " join 		alternativa a on a.id = sr.id_alternativa     " + 
                " join 		questoes q on q.id = sr.id_questao " + 
                " where 	ssa.simulado_id = " + idSimulado + 
                " and 		ssa.simulado_status_id 	= 3  " + 
                " and 		q.tipo_id " + in + 
                " group by id_simulado, ssa.nome_aluno "
            ;

            
            List<PercentualDeAcerto> percentualDeAcertos = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<PercentualDeAcerto>(){
                    public PercentualDeAcerto mapRow(ResultSet rs, int numRow) throws SQLException{
                        PercentualDeAcerto percentualDeAcerto = new PercentualDeAcerto();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                        
                        percentualDeAcerto.setIdAluno(rs.getString("id_aluno"));
                        percentualDeAcerto.setNomeAluno(rs.getString("nome"));
                        percentualDeAcerto.setTaxaAcerto(percentual);
                        return percentualDeAcerto;
                    }
                }
            );
            
            int qtdAlunos = 0; 
            double mediaSimulado = 0;
           
            for(PercentualDeAcerto p : percentualDeAcertos) {
                qtdAlunos++;
                mediaSimulado += p.getTaxaAcerto();
            }

            mediaSimulado = (double) (mediaSimulado / qtdAlunos);
            
            map.put("alunos", percentualDeAcertos);
            map.put("media", mediaSimulado);
            return map ;

        } catch (Exception e) {
            return null;
        }
    }

    public PercentualDeAcertoCSV reportsCSV(Long idSimulado, Long tipo){
        
        String in = "";
        if(tipo == 1){
            in = "in(1)";
        }else if(tipo == 2){
            in = "in(2)";
        }else{
            in = "in(1, 2)";
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String sql = 
                " select 	id_simulado as id_simulado, " + 
                "			ssa.id_aluno as id_aluno, " + 
                "			ssa.nome_aluno as nome, " + 
                "			count(distinct(sr.id_questao))   as questoes_respondidas,  " + 
                "            (  " + 
                "            	select count(*)  " + 
                "            	from simulado_resolucao sr  " + 
                "            	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
                "                join questoes qt on qt.id =  sr.id_questao " + 
                "            	where sr.id_aluno = ssa.id_aluno  " + 
                "            	and sr.id_simulado = ssa.simulado_id  " + 
                "                and qt.tipo_id " + in +
                "                and correta = true  " + 
                "            )  " + 
                "            as questoes_certas " + 
                " from 	    simulado_status_aluno ssa  " + 
                " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
                " join 		alternativa a on a.id = sr.id_alternativa     " + 
                " join 		questoes q on q.id = sr.id_questao " + 
                " where 	ssa.simulado_id = " + idSimulado + 
                " and 		ssa.simulado_status_id 	= 3  " + 
                " and 		q.tipo_id " + in + 
                " group by id_simulado, ssa.nome_aluno "
            ;

            
            List<PercentualDeAcerto> percentualDeAcertos = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<PercentualDeAcerto>(){
                    public PercentualDeAcerto mapRow(ResultSet rs, int numRow) throws SQLException{
                        PercentualDeAcerto percentualDeAcerto = new PercentualDeAcerto();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                        
                        percentualDeAcerto.setIdAluno(rs.getString("id_aluno"));
                        percentualDeAcerto.setNomeAluno(rs.getString("nome"));
                        percentualDeAcerto.setTaxaAcerto(percentual);
                        return percentualDeAcerto;
                    }
                }
            );
            
            int qtdAlunos = 0; 
            double mediaSimulado = 0;
           
            for(PercentualDeAcerto p : percentualDeAcertos) {
                qtdAlunos++;
                mediaSimulado += p.getTaxaAcerto();
            }

            mediaSimulado = (double) (mediaSimulado / qtdAlunos);
            
            PercentualDeAcertoCSV percentualDeAcertoCSV = new PercentualDeAcertoCSV();
            percentualDeAcertoCSV.setAlunos(percentualDeAcertos);
            percentualDeAcertoCSV.setMediaSimulado(mediaSimulado);
            return percentualDeAcertoCSV;

        } catch (Exception e) {
            return null;
        }
    }



    public List<PercentualDeAcerto> listUsers() {
        List<PercentualDeAcerto> percentualDeAcertos = new ArrayList<>();

        //create dummy users
        PercentualDeAcerto percentualDeAcerto = new PercentualDeAcerto();
        percentualDeAcerto.setIdAluno("1001");
        percentualDeAcerto.setNomeAluno("Teste");
        percentualDeAcerto.setTaxaAcerto(80.9);
        percentualDeAcertos.add(percentualDeAcerto);
        PercentualDeAcerto percentualDeAcerto2 = new PercentualDeAcerto();
        percentualDeAcerto2.setIdAluno("1001");
        percentualDeAcerto2.setNomeAluno("Teste");
        percentualDeAcerto2.setTaxaAcerto(80.9);
        percentualDeAcertos.add(percentualDeAcerto2);
        
        

        return percentualDeAcertos;
    }

}