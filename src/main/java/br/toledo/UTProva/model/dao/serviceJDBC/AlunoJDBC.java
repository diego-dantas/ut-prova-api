package br.toledo.UTProva.model.dao.serviceJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Service;

import br.toledo.UTProva.model.dao.serviceJDBC.useful.DynamicSQL;
import br.toledo.UTProva.model.dao.serviceJDBC.useful.FormatDecimal;
import br.toledo.UTProva.model.dto.SimuladoDashAluno;

@Service
public class AlunoJDBC{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Map getDashAluno(List<Long> idsSimulados, String idAluno){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String sql = 
            " select 	 id_simulado as id_simulado, " +
            "            count(distinct(sr.id_simulado)) as simulados_finalizados, " +
            "            count(distinct(sr.id_questao))   as questoes_respondidas, " +
            "            ( " +
			"            	select count(*) " +
			"            	from simulado_resolucao sr " +
			"            	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id " +
			"            	where sr.id_aluno = ssa.id_aluno " +
			"            	and sr.id_simulado = ssa.simulado_id " + 
			"            	and correta = true " +
			"            ) " +
            "            as questoes_certas, " +
            "            ssa.data_inicio as data_inicio,  " +
			"            ssa.data_final as data_final " +
            " from 	    simulado_status_aluno ssa " +
            " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno " +
            " join 		alternativa a on a.id = sr.id_alternativa " +
            " where 	ssa.id_aluno = " + idAluno +
            " and 		ssa.simulado_id " + DynamicSQL.createInLongs(idsSimulados) + 
            " and 		ssa.simulado_status_id 	= 3 " +
            " group by id_simulado order by id_simulado desc" ;
            
            List<SimuladoDashAluno> simuladoDashAluno = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<SimuladoDashAluno>(){
                    public SimuladoDashAluno mapRow(ResultSet rs, int numRow) throws SQLException{
                        SimuladoDashAluno simulado = new SimuladoDashAluno();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;

                        simulado.setIdSimulado(rs.getLong("id_simulado"));
                        simulado.setSimuladosFinalizados(rs.getInt("simulados_finalizados"));
                        simulado.setQuestoesRespondidas(rs.getInt("questoes_respondidas"));
                        simulado.setQuestoesCertas(rs.getInt("questoes_certas"));
                        simulado.setPercentual(percentual);
                        simulado.setDataInicio(rs.getDate("data_inicio"));
                        simulado.setDataFinal(rs.getDate("data_final"));
                        return simulado;
                    }
                }
            );
            int qtdsimulado = 0;
            int qtdQuestaoRespondida = 0;
            int qtdQuestaoRespondidaCerta = 0;
            double percentualTotal = 0;
            for( SimuladoDashAluno sis : simuladoDashAluno){
                qtdsimulado += sis.getSimuladosFinalizados();
                qtdQuestaoRespondida += sis.getQuestoesRespondidas();
                qtdQuestaoRespondidaCerta += sis.getQuestoesCertas();
            }
            percentualTotal = ( ((double) qtdQuestaoRespondidaCerta) / ((double) qtdQuestaoRespondida) ) * 100; 
                

            Map<String, Object> mapTotal = new HashMap<String, Object>();
            mapTotal.put("totalSimulado", qtdsimulado);
            mapTotal.put("totalQuestoesCertas", qtdQuestaoRespondidaCerta);
            mapTotal.put("totalQuestoesRespondidas", qtdQuestaoRespondida);
            mapTotal.put("totalPercentual", percentualTotal );

            map.put("total", mapTotal);
            map.put("list", simuladoDashAluno);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public Map getResultAluno(Long idsSimulados, String idAluno){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String sql = 
            " select 	 id_simulado as id_simulado, " +
            "            count(distinct(sr.id_simulado)) as simulados_finalizados, " +
            "            count(distinct(sr.id_questao))   as questoes_respondidas, " +
            "            ( " +
			"            	select count(*) " +
			"            	from simulado_resolucao sr " +
			"            	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id " +
			"            	where sr.id_aluno = ssa.id_aluno " +
			"            	and sr.id_simulado = ssa.simulado_id " + 
			"            	and correta = true " +
			"            ) " +
            "            as questoes_certas, " +
            "            ssa.data_inicio as data_inicio,  " +
			"            ssa.data_final as data_final " +
            " from 	    simulado_status_aluno ssa " +
            " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno " +
            " join 		alternativa a on a.id = sr.id_alternativa " +
            " where 	ssa.id_aluno = " + idAluno +
            " and 		ssa.simulado_id = " + idsSimulados + 
            " and 		ssa.simulado_status_id 	= 3 " +
            " group by id_simulado " ;
            
            List<SimuladoDashAluno> simuladoDashAluno = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<SimuladoDashAluno>(){
                    public SimuladoDashAluno mapRow(ResultSet rs, int numRow) throws SQLException{
                        SimuladoDashAluno simulado = new SimuladoDashAluno();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                        simulado.setIdSimulado(rs.getLong("id_simulado"));
                        simulado.setSimuladosFinalizados(rs.getInt("simulados_finalizados"));
                        simulado.setQuestoesRespondidas(rs.getInt("questoes_respondidas"));
                        simulado.setQuestoesCertas(rs.getInt("questoes_certas"));
                        simulado.setPercentual(FormatDecimal.formatDecimal(percentual));
                        simulado.setDataInicio(rs.getDate("data_inicio"));
                        simulado.setDataFinal(rs.getDate("data_final"));
                        return simulado;
                    }
                }
            );
         
            map.put("result", simuladoDashAluno);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}