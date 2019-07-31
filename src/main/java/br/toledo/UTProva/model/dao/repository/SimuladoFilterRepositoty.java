package br.toledo.UTProva.model.dao.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import br.toledo.UTProva.model.dao.entity.SimuladoDisciplinasEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoTurmasEntity;
import br.toledo.UTProva.model.dto.SimuladoResolucaoDTO;


@Service
public class SimuladoFilterRepositoty {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<SimuladoTurmasEntity> getIdTurmas(int periodo, String idCurso){
        try {

            String sql = "select distinct id_turma, nome, id_curso from simulado_turmas where periodo_letivo  = "+periodo+" and id_curso =" + idCurso;

            List<SimuladoTurmasEntity> turmas = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<SimuladoTurmasEntity>() {
            public SimuladoTurmasEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                SimuladoTurmasEntity turmas = new SimuladoTurmasEntity();
                turmas.setIdTurma(rs.getString("id_turma"));
                turmas.setNome(rs.getString("nome"));
                turmas.setIdCurso(rs.getString("id_curso"));
                return turmas;
            }
            });

            
            return turmas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SimuladoDisciplinasEntity> getIdDisciplinas(int periodo, String idTurma){
        try {

            String sql = "select distinct id_disciplina, nome, id_turma from simulado_disciplinas where periodo_letivo  =  " +periodo+ " and id_turma = '" + idTurma + "'";

            List<SimuladoDisciplinasEntity> disciplinas = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<SimuladoDisciplinasEntity>() {
            public SimuladoDisciplinasEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                SimuladoDisciplinasEntity disciplinas = new SimuladoDisciplinasEntity();
                disciplinas.setIdTurma(rs.getString("id_turma"));
                disciplinas.setNome(rs.getString("nome"));
                disciplinas.setIdDisciplina(rs.getString("id_disciplina"));
                return disciplinas;
            }
            });

            
            return disciplinas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int simuladoResponse(Long idSimulado, Long idQuestao, String idAluno, Long idAlternativa){
        try {

            String sql = "update simulado_resolucao set id_alternativa = " + idAlternativa + " where id_simulado = "+ idSimulado +" and id_questao = "+ idQuestao +" and id_aluno = '"+ idAluno +"'";
            System.out.println(sql);
            int retorno = this.jdbcTemplate.update(sql);
           
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}