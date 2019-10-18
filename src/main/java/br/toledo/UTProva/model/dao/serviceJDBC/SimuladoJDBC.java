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
import br.toledo.UTProva.model.dao.serviceJDBC.useful.FormatDecimal;
import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;
import br.toledo.UTProva.model.dto.SimuladoDashAluno;

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
                    simulado.setDataHoraInicial(rs.getTimestamp("data_hora_inicial"));
                    simulado.setDataHoraFinal(rs.getTimestamp("data_hora_inicial"));
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
                    simulado.setDataHoraInicial(rs.getTimestamp("data_hora_inicial"));
                    simulado.setDataHoraFinal(rs.getTimestamp("data_hora_inicial"));
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
                        " where s.rascunho = false  and s.id " + DynamicSQL.createInString(ids) + 
                        " order by s.id desc"; 

            
            List<SimuladoEntity> simulados = this.jdbcTemplate.query(sql,
            new Object[]{},
            new RowMapper<SimuladoEntity>() {
                public SimuladoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                    
                    SimuladoEntity simulado = new SimuladoEntity();
                    simulado.setId(rs.getLong("id"));
                    simulado.setNome(rs.getString("nome"));
                    simulado.setStatus(rs.getString("status"));
                    simulado.setDataHoraInicial(rs.getTimestamp("data_hora_inicial"));
                    simulado.setDataHoraFinal(rs.getTimestamp("data_hora_final"));
                    return simulado;
                }
            });

            
            return simulados;
        } catch (Exception e) {
            e.printStackTrace();
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
                    " where s.id " + DynamicSQL.createInString(ids) +
                    " order by s.id desc " 
            ; 

            
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
        }
        return null;
    }

    public List<SimuladoEntity> findSimuladosEnade(List<Long> idSimulados){
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
                    " where enade = true and s.id " + DynamicSQL.createInString(ids) +
                    " order by s.id desc " 
            ; 

            
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
        }
        return null;
    }


    public int finalizaSimulado(Long idSimulado, String idAluno){
        try {

            String sql = "update simulado_status_aluno set simulado_status_id = 3, data_final = now() where simulado_id = "+ idSimulado +" and id_aluno = '"+ idAluno +"'";
            System.out.println(sql);
            int retorno = this.jdbcTemplate.update(sql);
           
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    //public List<SimuladoDTO> getSimuladosQuestoesDiscursivas(){
    public List<Map<String, Object>> getSimuladosQuestoesDiscursivas(int periodo_letivo, List<String> idCursos, List<String> idTurmas, List<String> idDisciplinas){
        try {
            String sqlCurso = "";
            String sqlTurma = "";
            String sqlDisciplina = "";
            
            
            if(idCursos.size() > 0){
                sqlCurso = 
                    " select 	sc.simulado_id as simuladoId  " + 
                    " from 	simulado_cursos sc  " + 
                    " where	sc.periodo_letivo = " + periodo_letivo  +  
                    " and 	sc.id_curso " + DynamicSQL.createInString(idCursos);
            }

            if(idTurmas.size() > 0){
                String union = " ";
                if(idCursos.size() > 0) {
                    union = " union ";
                }
                sqlTurma = 
                    union +
                    " select 	st.simulado_id as simuladoId  " +
                    " from 	simulado_turmas st  " +
                    " where 	st.periodo_letivo = " + periodo_letivo  + 
                    " and 		st.id_turma " + DynamicSQL.createInString(idTurmas);
            }

            if(idDisciplinas.size() > 0){
                String union = " ";
                if(idTurmas.size() > 0 || idCursos.size() > 0) {
                    union = " union ";
                }
                sqlDisciplina = 
                    union +
                    " select sd.simulado_id as simuladoId " + 
                    " from  simulado_disciplinas sd " + 
                    " where	sd.periodo_letivo  = " + periodo_letivo  +  
                    " and 	sd.id_disciplina " + DynamicSQL.createInString(idDisciplinas);
            }
            

            String sql = "" + 
                " select     distinct(s.id) as id, " +
                "            s.nome as nome " +
                " from 		 simulados s " +
                " inner join simulado_questoes sq on sq.simulado_id = s.id " +
                " inner join questoes q           on q.id = questao_id " +
                " where 	 q.tipo_resposta_id = 2 " +
                " and 		 s.rascunho         = false " +
                " and   s.id in(" +
                sqlCurso + "\n " +
                sqlTurma + "\n" +
                sqlDisciplina + ") order by s.id desc "
            ;            
            List<Map<String, Object>> simulados = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<Map<String, Object>>() {
                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, Object> map = new HashMap();
                        map.put("id",   rs.getLong("id"));
                        map.put("nome", rs.getString("nome"));
                        return map;
                    }
                }

            );

            return simulados;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map getDashAdmin(List<Long> idsSimulados){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String sql = 
            " select  		s.id as id_simulado, " + 
			" 			    s.nome, " + 
            "               s.data_hora_inicial as data_inicio, " + 
            "               s.data_hora_final as data_final, " + 
            "               count(distinct(sr.id_simulado)) as simulados_finalizados, " + 
			" 			    count(sr.id_questao)  as questoes_respondidas,  " + 
            "             (  " + 
			"             	select count(*)  " + 
			"             	from simulado_resolucao sr  " + 
			"             	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
            "                 join   questoes qt on qt.id =  sr.id_questao " + 
			"             	where sr.id_simulado = ssa.simulado_id  			            	 " + 
            "                 and correta = true  " + 
			"             )  " + 
            "             as questoes_certas                     " + 
            "  from 	    simulados s " + 
			"  join 	    simulado_status_aluno ssa on ssa.simulado_id = s.id " + 
            "  join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
            "  join 		alternativa a on a.id = sr.id_alternativa     " + 
            "  join 		questoes q on q.id = sr.id_questao " + 
            "  where 	ssa.simulado_id " + DynamicSQL.createInLongs(idsSimulados) +
            "  and 		ssa.simulado_status_id 	= 3  " + 
            "  group by  " + 
			" 	s.id order by s.id desc " ;
            
            List<SimuladoDashAluno> dashAdmin = this.jdbcTemplate.query(sql,
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
                        simulado.setDataInicio(rs.getTimestamp("data_inicio"));
                        simulado.setDataFinal(rs.getTimestamp("data_final"));
                        simulado.setNome(rs.getString("nome"));
                        return simulado;
                    }
                }
            );
            int qtdsimulado = 0;
            int qtdQuestaoRespondida = 0;
            int qtdQuestaoRespondidaCerta = 0;
            double percentualTotal = 0;
            for( SimuladoDashAluno sis : dashAdmin){
                qtdsimulado += sis.getSimuladosFinalizados();
                qtdQuestaoRespondida += sis.getQuestoesRespondidas();
                qtdQuestaoRespondidaCerta += sis.getQuestoesCertas();
            }
            percentualTotal = ( ((double) qtdQuestaoRespondidaCerta) / ((double) qtdQuestaoRespondida) ) * 100; 
                


            Map<String, Object> mapTotal = new HashMap<String, Object>();
            mapTotal.put("totalSimulado", qtdsimulado);
            mapTotal.put("totalQuestoesCertas", qtdQuestaoRespondidaCerta);
            mapTotal.put("totalQuestoesRespondidas", qtdQuestaoRespondida);
            mapTotal.put("totalPercentual", FormatDecimal.formatDecimal(percentualTotal));

            map.put("total", mapTotal);
            map.put("list", dashAdmin);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<SimuladoDTO> getGestor(List<Long> idsSimulados){
        try {
            
            String sql = 
            " select * from  simulados s " + 
            " where 1=1 and  " + 
            "     s.id " + DynamicSQL.createInLongs(idsSimulados) +  " and " + 
            "     s.rascunho = false and  " + 
            "     s.data_hora_inicial < date_format((now() + time('03:00:00')), '%Y-%m-%d %H:%i:%s') and  " + 
            "     s.data_hora_final   > date_format((now() + time('03:00:00')), '%Y-%m-%d %H:%i:%s') " + 
			" order by s.id desc " ;
            
            List<SimuladoDTO> simulados = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<SimuladoDTO>(){
                    public SimuladoDTO mapRow(ResultSet rs, int numRow) throws SQLException{
                        SimuladoDTO simulado = new SimuladoDTO();
                        simulado.setId(rs.getLong("id"));
                        simulado.setNome(rs.getString("nome"));
                        simulado.setRascunho(rs.getBoolean("rascunho"));
                        simulado.setDataHoraInicial(rs.getTimestamp("data_hora_inicial"));
                        simulado.setDataHoraFinal(rs.getTimestamp("data_hora_final"));
                        return simulado;
                    }
                }
            );
        
            return simulados;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Map acompanhamentoSimulado(Long idsSimulado){
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            String sql = 
            " select 	 	ssa.id_aluno as id_aluno, " + 
	        " 			ssa.nome_aluno as nome_aluno, " + 
            "            count(distinct(sr.id_questao))   as questoes_respondidas,  " + 
            "            (  " + 
	        "            	select count(*)  " + 
	        "            	from simulado_resolucao sr  " + 
	        "            	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
	        "            	where sr.id_aluno = ssa.id_aluno  " + 
	        "            	and sr.id_simulado = ssa.simulado_id  " + 
	        "            	and correta = true  " + 
	        "            )  " + 
            "            as questoes_certas,  " + 
            "            ssa.data_inicio as data_inicio,   " + 
	        "            ssa.data_final as data_final  " + 
            " from 	    simulado_status_aluno ssa  " + 
            " join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
	        " join 	alternativa a on a.id = sr.id_alternativa  " + 
            " where 	1=1  " + 
            " and 		ssa.simulado_id = " + idsSimulado +
            " group by ssa.id_aluno order by ssa.nome_aluno desc ";
            
            List<SimuladoDashAluno> dashAdmin = this.jdbcTemplate.query(sql,
                new Object[]{},
                new RowMapper<SimuladoDashAluno>(){
                    public SimuladoDashAluno mapRow(ResultSet rs, int numRow) throws SQLException{
                        SimuladoDashAluno simulado = new SimuladoDashAluno();
                        double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;

                        simulado.setIdSimulado(rs.getLong("id_aluno"));
                        simulado.setQuestoesRespondidas(rs.getInt("questoes_respondidas"));
                        simulado.setQuestoesCertas(rs.getInt("questoes_certas"));
                        simulado.setPercentual(FormatDecimal.formatDecimal(percentual));
                        simulado.setDataInicio(rs.getTimestamp("data_inicio"));
                        simulado.setDataFinal(rs.getTimestamp("data_final"));
                        simulado.setNome(rs.getString("nome_aluno"));
                        return simulado;
                    }
                }
            );
            int qtdQuestaoRespondida = 0;
            int qtdQuestaoRespondidaCerta = 0;
            double percentualTotal = 0;
            for( SimuladoDashAluno sis : dashAdmin){
                qtdQuestaoRespondida += sis.getQuestoesRespondidas();
                qtdQuestaoRespondidaCerta += sis.getQuestoesCertas();
            }
            percentualTotal = ( ((double) qtdQuestaoRespondidaCerta) / ((double) qtdQuestaoRespondida) ) * 100; 
                
       

            Map<String, Object> mapTotal = new HashMap<String, Object>();
            mapTotal.put("totalQuestoesCertas", qtdQuestaoRespondidaCerta);
            mapTotal.put("totalQuestoesRespondidas", qtdQuestaoRespondida);
            mapTotal.put("totalPercentual", FormatDecimal.formatDecimal(percentualTotal));

            map.put("total", mapTotal);
            map.put("list", dashAdmin);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}