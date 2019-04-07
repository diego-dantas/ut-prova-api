package br.toledo.UTProva.model.dao.serviceJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Service;


import br.toledo.UTProva.model.dao.serviceJDBC.useful.DynamicSQL;
import br.toledo.UTProva.model.dto.SimuladoDashAluno;

@Service
public class AlunoJDBC{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<SimuladoDashAluno> getDashAluno(List<Long> idsSimulados, String idAluno){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String sql = 
            " select 	 id_simulado as id_simulado, " +
            "            count(distinct(sr.id_simulado)) as simulados_finalizados, " +
            "            count(distinct(sr.id_questao))   as questoes_respondidas, " +
            "            (select count(distinct(ate.id)) " +
            "                from alternativa ate " +
            "                where 	questao_id = sr.id_questao " +
            "                and ate.correta = true) " +
            "            as questooes_certas " +
            " from 	    simulado_status_aluno ssa " +
            " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno " +
            " join 		alternativa a on a.id = sr.id_alternativa " +
            " where 	ssa.id_aluno = " + idAluno +
            " and 		ssa.simulado_id " + DynamicSQL.createInLongs(idsSimulados) + 
            " and 		ssa.simulado_status_id 	= 3 " +
            " group by id_simulado " ;
            
            List<SimuladoDashAluno> simuladoDashAluno = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<SimuladoDashAluno>(){
                    public SimuladoDashAluno mapRow(ResultSet rs, int numRow) throws SQLException{
                        SimuladoDashAluno simulado = new SimuladoDashAluno();
                        simulado.setIdSimulado(rs.getLong("id_simulado"));
                        simulado.setSimuladosFinalizados(rs.getInt("simulados_finalizados"));
                        simulado.setQuestoesRespondidas(rs.getInt("questoes_respondidas"));
                        simulado.setQuestooesCertas(rs.getInt("questooes_certas"));
                        return simulado;
                    }
                }
            );
            
            return simuladoDashAluno;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}