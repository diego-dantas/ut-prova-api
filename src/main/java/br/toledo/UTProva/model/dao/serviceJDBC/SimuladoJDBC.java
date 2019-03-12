package br.toledo.UTProva.model.dao.serviceJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.serviceJDBC.useful.DynamicSQL;
import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;

@Service
public class SimuladoJDBC {


    @Autowired
    private JdbcTemplate jdbcTemplate;


    public SimuladoDTO getSimuladoID(Long idSimulado){
        try {

            String sql = "select * from simulados where id = " + idSimulado;

           

            List<SimuladoDTO> simulados = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<SimuladoDTO>() {
                public SimuladoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SimuladoDTO simulado = new SimuladoDTO();
                    simulado.setId(rs.getLong("id"));
                    simulado.setNome(rs.getString("nome"));
                    simulado.setRascunho(rs.getBoolean("rascunho"));
                    simulado.setStatus(rs.getString("status"));
                    simulado.setDataHoraInicial(rs.getDate("data_hora_inicial"));
                    simulado.setDataHoraFinal(rs.getDate("data_hora_inicial"));
                    return simulado;
                }
            });

            SimuladoDTO simuladoDTO = new SimuladoDTO();
            simuladoDTO = simulados.get(0);
            String sqlCurso = "select * from simulado_cursos where simulado_id = " + idSimulado;

            List<CursosDTO> cursos = this.jdbcTemplate.query(sqlCurso,
            new Object[]{},
            new RowMapper<CursosDTO>() {
                public CursosDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    CursosDTO curso = new CursosDTO();
                    curso.setId(rs.getString("id_curso"));
                    curso.setNome(rs.getString("nome"));
                    curso.setIdPeriodoLetivo(rs.getInt("periodo_letivo"));
                    return curso;
                }
            });
            
            simuladoDTO.setCursos(cursos);
            return simuladoDTO;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO BUSCAR O SIMULADO POR ID " + e);
        }
        return null;
    }


    public SimuladoDTO getAllSimulado(Long idSimulado){
        try {

            String sql = "select * from simulados where id = " + idSimulado;

           

            List<SimuladoDTO> simulados = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<SimuladoDTO>() {
                public SimuladoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SimuladoDTO simulado = new SimuladoDTO();
                    simulado.setId(rs.getLong("id"));
                    simulado.setNome(rs.getString("nome"));
                    simulado.setRascunho(rs.getBoolean("rascunho"));
                    simulado.setStatus(rs.getString("status"));
                    simulado.setDataHoraInicial(rs.getDate("data_hora_inicial"));
                    simulado.setDataHoraFinal(rs.getDate("data_hora_inicial"));
                    return simulado;
                }
            });

            SimuladoDTO simuladoDTO = new SimuladoDTO();
            simuladoDTO = simulados.get(0);
            String sqlCurso = "select * from simulado_cursos where simulado_id = " + idSimulado;

            List<CursosDTO> cursos = this.jdbcTemplate.query(sqlCurso,
            new Object[]{},
            new RowMapper<CursosDTO>() {
                public CursosDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    CursosDTO curso = new CursosDTO();
                    curso.setId(rs.getString("id_curso"));
                    curso.setNome(rs.getString("nome"));
                    curso.setIdPeriodoLetivo(rs.getInt("periodo_letivo"));
                    return curso;
                }
            });
            
            simuladoDTO.setCursos(cursos);
            return simuladoDTO;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO BUSCAR O SIMULADO POR ID " + e);
        }
        return null;
    }



    /**
     * Metodo para busca os simulados por ids 
     */
    public List<SimuladoEntity> getSimuladosID(List<Long> idSimulados, String idAluno){
        
        try {
            List<String> ids = new ArrayList<>();
            idSimulados.forEach(n -> ids.add(n.toString()));
            String sql = "select 	s.id, " +
                                    " s.data_hora_inicial, " +
                                    " s.data_hora_final, " +
                                    " s.nome, " +
                                    " if(sa.simulado_status_id > 0, " +
                                    "    (select descricao from simulado_status where id =	sa.simulado_status_id), " +
                                    "    'Pendente' "+
                                    " ) as status "+
                        " from 	simulados s "+
                        " left join simulado_status_aluno sa on sa.simulado_id = s.id and sa.id_aluno = " + idAluno + 
                        " where s.id " + DynamicSQL.createInString(ids); 

            
            List<SimuladoEntity> simulados = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<SimuladoEntity>() {
                public SimuladoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                    System.out.println(rs.getDate("data_hora_inicial"));
                    SimuladoEntity simulado = new SimuladoEntity();
                    simulado.setId(rs.getLong("id"));
                    simulado.setNome(rs.getString("nome"));
                    simulado.setStatus(rs.getString("status"));
                    simulado.setDataHoraInicial(rs.getDate("data_hora_inicial"));
                    simulado.setDataHoraFinal(rs.getDate("data_hora_inicial"));
                    return simulado;
                }
            });

            simulados.forEach(n -> System.out.println("Data e Hora  " + n.getDataHoraInicial()));
            return simulados;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO BUSCAR O SIMULADO POR ID E ALUNOS" + e);
        }
        return null;
    }


    /**
     * Metodo para deletar o simulado por id
     */
    public boolean deleteSimulado(Long idSimulado){
        boolean delete = false;
        try {
            String sqlStatus = "select count(id) from simulado_status_aluno where simulado_id = " + idSimulado;
            int qtd = this.jdbcTemplate.queryForObject(sqlStatus, Integer.class);

            if(qtd > 0){
                return delete;
            }else{
                String sqlq = "delete from simulado_questoes where simulado_id = " + idSimulado;
                String sqlc = "delete from simulado_cursos where simulado_id = " + idSimulado;
                String sqlt = "delete from simulado_turmas where simulado_id = " + idSimulado;
                String sqld = "delete from simulado_disciplinas where simulado_id = " + idSimulado;
                String sqls = "delete from simulados where id = " + idSimulado;

                int del = 0;

                this.jdbcTemplate.update(sqlq);
                this.jdbcTemplate.update(sqlc);
                this.jdbcTemplate.update(sqlt);
                this.jdbcTemplate.update(sqld);
                this.jdbcTemplate.update(sqls);
                
                delete = true;
            }                                 
            return delete;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO DELETAR O SIMULADO" + e);
        }
        return delete;
    }


    public Map simuladoStatusRascunho(Long idSimulado, boolean status){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String sqlStatus = "select count(id) from simulado_status_aluno where simulado_id = " + idSimulado;
            int qtd = this.jdbcTemplate.queryForObject(sqlStatus, Integer.class);
            
            if(qtd > 0){
                map.put("success", false);
                map.put("message", "Não foi possível efetuar a alteração. Motivo: O Simulado já foi iniciado.");
            }else{
                String sqlUpdate =  "update simulados set rascunho =  " + status + " where id = " + idSimulado;
                this.jdbcTemplate.update(sqlUpdate);
                map.put("success", true);
                map.put("message", "Status alterado com sucesso.");
            }

            return map;
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao alterar o status do simulado " + e);
            map.put("success", false);
            map.put("message", "Erro ao alterar o status");
        }
       return map;
    }

    public List<SimuladoEntity> findSimuladosAdmin(List<Long> idSimulados){
        //date_format(s.data_hora_inicial, '%Y-%m-%d %H:%i:%s') as data_hora_inicial,  date_format(s.data_hora_final, '%Y-%m-%d %H:%i:%s') as data_hora_final,
        try {
            List<String> ids = new ArrayList<>();
            idSimulados.forEach(n -> ids.add(n.toString()));
            String  sql = "select 	distinct s.id, " +
            " s.data_hora_inicial, " +
            " s.data_hora_final, " +
            " s.nome, " +
            " s.rascunho, " +
            " if(sa.simulado_status_id > 0, 'Realizado', 'Pendente') as status_aluno" +
        " from 	simulados s "+
        " left join simulado_status_aluno sa on sa.simulado_id = s.id " +
        " where s.id " + DynamicSQL.createInString(ids); 

            
            List<SimuladoEntity> simulados = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<SimuladoEntity>() {
                public SimuladoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SimuladoEntity simulado = new SimuladoEntity();
                    simulado.setId(rs.getLong("id"));
                    simulado.setNome(rs.getString("nome"));
                    simulado.setStatus(rs.getString("status_aluno"));
                    simulado.setRascunho(rs.getBoolean("rascunho"));
                    simulado.setDataHoraInicial(rs.getTimestamp("data_hora_inicial"));
                    simulado.setDataHoraFinal(rs.getTimestamp("data_hora_final"));
                    return simulado;
                }
            });
            return simulados;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO BUSCAR O SIMULADO POR ID E ALUNOS" + e);
        }
        return null;
    }
   
}