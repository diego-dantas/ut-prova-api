package br.toledo.UTProva.reports.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.toledo.UTProva.reports.dto.AcertosDetalhado;
import br.toledo.UTProva.reports.dto.DetalhadoReports;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReportsDetalhadoRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DetalhadoReports> getDetalhado(Long idSimulado){

        try {
            
            String sql = 
                " select  	id_simulado as id_simulado, " + 
                " (select enade from simulados where id = ssa.simulado_id ) as enade, "+
                " 			ssa.id_aluno as id_aluno, " + 
                " 			ssa.nome_aluno as nome, " + 
                " 			count(distinct(sr.id_questao))   as questoes_respondidas,  " + 
                "             (  " + 
                "             	select count(*)  " + 
                "             	from simulado_resolucao sr  " + 
                "             	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
                "                 join   questoes qt on qt.id =  sr.id_questao " + 
                "             	where sr.id_aluno = ssa.id_aluno  " + 
                "             	and sr.id_simulado = ssa.simulado_id  " + 
                "                 and correta = true  " + 
                "             )  " + 
                "             as questoes_certas, " + 
                "             ( " + 
                " 				select count(distinct(qt.id))  " + 
                "             	from simulado_resolucao sr  " + 
                "             	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
                "                 join   questoes qt on qt.id =  sr.id_questao " + 
                "             	where qt.tipo_id = 1 " + 
                "             	and sr.id_simulado = ssa.simulado_id  " + 
                " 			)  as total_questoes_fg, " + 
                "             (  " + 
                "             	select count(*)  " + 
                "             	from simulado_resolucao sr  " + 
                "             	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
                "                 join   questoes qt on qt.id =  sr.id_questao " + 
                "             	where sr.id_aluno = ssa.id_aluno  " + 
                "             	and sr.id_simulado = ssa.simulado_id  " + 
                "                 and qt.tipo_id = 1 " + 
                "                 and correta = true  " + 
                "             )  " + 
                "             as questoes_certas_fg, " + 
                "              ( " + 
                " 				select count(distinct(qt.id))  " + 
                "             	from simulado_resolucao sr  " + 
                "             	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
                "                 join   questoes qt on qt.id =  sr.id_questao " + 
                "             	where qt.tipo_id = 2 " + 
                "             	and sr.id_simulado = ssa.simulado_id  " + 
                " 			)  as total_questoes_ce, " + 
                "             (  " + 
                "             	select count(*)  " + 
                "             	from simulado_resolucao sr  " + 
                "             	join 	alternativa a on a.questao_id = sr.id_questao and sr.id_alternativa = a.id  " + 
                "                 join   questoes qt on qt.id =  sr.id_questao " + 
                "             	where sr.id_aluno = ssa.id_aluno  " + 
                "             	and sr.id_simulado = ssa.simulado_id  " + 
                "                 and qt.tipo_id = 2 " + 
                "                 and correta = true  " + 
                "             )  " + 
                "             as questoes_certas_ce, " + 
                "                case  " +
                "                when  " +
                "                    (select count(nota_formacao_geral) " +
                "                    from aluno_questao_discursiva   " +
                "                    where simulado_id = ssa.simulado_id    " +
                "                    and id_aluno = ssa.id_aluno) > 0 " +
                "                then  " +
                "                    (select nota_formacao_geral " +
                "                    from aluno_questao_discursiva   " +
                "                    where simulado_id = ssa.simulado_id    " +
                "                    and id_aluno = ssa.id_aluno) " +
                "                else -1 " +
                "            end as nota_formacao_geral_discursiva,  " +
                "            case  " +
                "                when " +
                "                    (select count(nota_conhecimento_especifico)  " +
                "                    from aluno_questao_discursiva   " +
                "                    where simulado_id = ssa.simulado_id    " +
                "                    and id_aluno = ssa.id_aluno) > 0 " +
                "                then  " +
                "                    (select nota_conhecimento_especifico " +
                "                    from aluno_questao_discursiva   " +
                "                    where simulado_id = ssa.simulado_id    " +
                "                    and id_aluno = ssa.id_aluno) " +
                "                else -1  " +
                "            end as nota_conhecimento_especifico_discursiva " +
                "  from 	    simulado_status_aluno ssa  " + 
                "  join 		simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno  " + 
                "  join 		alternativa a on a.id = sr.id_alternativa     " + 
                "  join 		questoes q on q.id = sr.id_questao " + 
                "  where 	ssa.simulado_id = " + idSimulado + 
                "  and 		ssa.simulado_status_id 	= 3  " + 
                "  group by  " + 
                " 	id_simulado,  " + 
                " 	ssa.nome_aluno order by ssa.nome_aluno ";

                List<DetalhadoReports> detalhadoReports = this.jdbcTemplate.query(sql,
                    new Object[]{},
                    new RowMapper<DetalhadoReports>(){
                        public DetalhadoReports mapRow(ResultSet rs, int numRow) throws SQLException{

                            DetalhadoReports dReports = new DetalhadoReports();
                            double notaObjetivasFG    = 0;
                            double notaObjetivasCE    = 0;
                            double notaObjetivasTotal = 0;
                            double notaDiscursivaFG   = 0;
                            double notaDiscursivaCE   = 0;
                            if(rs.getDouble("total_questoes_fg") > 0)     notaObjetivasFG    = (((double) rs.getDouble("questoes_certas_fg")) / ((double) rs.getDouble("total_questoes_fg")))     * 100;
                            if(rs.getDouble("total_questoes_ce") > 0)     notaObjetivasCE    = (((double) rs.getDouble("questoes_certas_ce")) / ((double) rs.getDouble("total_questoes_ce")))     * 100;
                            if(rs.getDouble("questoes_respondidas") > 0)  notaObjetivasTotal = (((double) rs.getDouble("questoes_certas"))    / ((double) rs.getDouble("questoes_respondidas")))  * 100;
                            
                            if(rs.getDouble("nota_formacao_geral_discursiva") != -1)          notaDiscursivaFG = (double) (rs.getDouble("nota_formacao_geral_discursiva") * 10);
                            if(rs.getDouble("nota_conhecimento_especifico_discursiva") != -1) notaDiscursivaCE = (double) (rs.getDouble("nota_conhecimento_especifico_discursiva") * 10);

                            double notaFinalFG = 0;
                            double notaFinalCE = 0;
                            double notaFinal = 0;
                            
                            if(rs.getBoolean("enade") == true && (rs.getDouble("total_questoes_fg") > 0 && rs.getDouble("total_questoes_ce") > 0) && ((rs.getDouble("nota_formacao_geral_discursiva") != -1) && (rs.getDouble("nota_conhecimento_especifico_discursiva") != -1))){                               
                                notaFinalFG = (notaObjetivasFG * 0.6) + (notaDiscursivaFG * 0.4);
                                notaFinalCE = (notaObjetivasCE * 0.85) + (notaDiscursivaCE * 0.15);
                                notaFinal   = (notaFinalFG * 0.25) + (notaFinalCE * 0.75);                                                               
                            }else if(rs.getBoolean("enade") == false && (rs.getDouble("total_questoes_fg") > 0 && rs.getDouble("total_questoes_ce") > 0) && ((rs.getDouble("nota_formacao_geral_discursiva") != -1) && (rs.getDouble("nota_conhecimento_especifico_discursiva") != -1))){                               
                                notaFinalFG = (notaObjetivasFG * 0.5) + (notaDiscursivaFG * 0.5);
                                notaFinalCE = (notaObjetivasCE * 0.5) + (notaDiscursivaCE * 0.5);
                                notaFinal   = (notaFinalFG * 0.5) + (notaFinalCE * 0.5);                                
                            }else if(rs.getDouble("total_questoes_ce") > 0 && (rs.getDouble("nota_conhecimento_especifico_discursiva") != -1)){                                
                                notaFinalCE = (notaObjetivasCE * 0.5) + (notaDiscursivaCE * 0.5);
                                notaFinal   = notaFinalCE;                                
                            }else if(rs.getDouble("total_questoes_ce") > 0 && (rs.getDouble("nota_conhecimento_especifico_discursiva") == -1)){
                                notaFinalCE = notaObjetivasCE;
                                notaFinal   = notaObjetivasCE; 
                            }else if(rs.getDouble("total_questoes_fg") > 0 && (rs.getDouble("nota_formacao_geral_discursiva") != -1)){                                
                                notaFinalFG = (notaObjetivasFG * 0.5) + (notaDiscursivaFG * 0.5);
                                notaFinal   = notaFinalFG;                                
                            }else if(rs.getDouble("total_questoes_fg") > 0 && (rs.getDouble("nota_formacao_geral_discursiva") == -1)){
                                notaFinalFG = notaObjetivasFG;
                                notaFinal   = notaObjetivasFG; 
                            }
                            
                            
                            
                            dReports.setMatricula(rs.getString("id_aluno"));
                            dReports.setAluno(rs.getString("nome"));
                            dReports.setQtdAcertoFG(rs.getInt("questoes_certas_fg"));
                            dReports.setNotaObjetivasFG(formatDecimal(notaObjetivasFG));
                            dReports.setQtdAcertoCE(rs.getInt("questoes_certas_ce"));
                            dReports.setNotaObjetivasCE(formatDecimal(notaObjetivasCE));
                            dReports.setTotalAcertoObjetivas(rs.getInt("questoes_certas"));
                            dReports.setNotaObjetivasTotal(formatDecimal(notaObjetivasTotal));
                            dReports.setNotaDiscursivaFG(formatDecimal(notaDiscursivaFG));
                            dReports.setNotaDiscursivaCE(formatDecimal(notaDiscursivaCE));
                            dReports.setNotaFinalFG(formatDecimal(notaFinalFG));
                            dReports.setNotaFinalCE(formatDecimal(notaFinalCE));
                            dReports.setNotaTotal(formatDecimal(notaFinal));
                            return dReports;
                        }

                    }
                );

                return detalhadoReports;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<AcertosDetalhado> reportsAcertosDetalhados(Long idSimulado){
        try {            
            String sql = 
                " select 	ssa.id_aluno,  " + 
                " 		ssa.nome_aluno, " + 
                "         trim(c.description) as conteudo, " + 
                "         q.id as id_questao, " + 
                "         a.correta " + 
                " from 	simulado_status_aluno ssa " + 
                " inner join simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno " + 
                " inner join questoes q on q.id = sr.id_questao " + 
                " inner join conteudos c on c.id = q.conteudo_id " + 
                " left join alternativa a on a.id = sr.id_alternativa " + 
                " where 	 " + 
                " 	ssa.simulado_id = " +  idSimulado + 
                " order by ssa.id_aluno, trim(c.description), q.id ";

            List<AcertosDetalhado> rAcertosDetalhados = this.jdbcTemplate.query(sql, 
                new Object[]{},
                new RowMapper<AcertosDetalhado>(){
                    public AcertosDetalhado mapRow(ResultSet rs, int numRow) throws SQLException{
                        AcertosDetalhado acertosDetalhado = new AcertosDetalhado();
                        
                        acertosDetalhado.setIdAluno(rs.getLong("id_aluno"));
                        acertosDetalhado.setNomeAluno(rs.getString("nome_aluno"));
                        acertosDetalhado.setConteudo(rs.getString("conteudo"));
                        acertosDetalhado.setIdQuestao(rs.getLong("id_questao"));
                        acertosDetalhado.setCorreta(rs.getString("correta"));
                        return acertosDetalhado;
                    }
                }
            );                

            return rAcertosDetalhados;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<AcertosDetalhado> reportsAcertosDetalhadosResumo(Long idSimulado){
        try {            
            String sql = 
                " select 	ssa.id_aluno,  " + 
                " 		ssa.nome_aluno, " + 
                "         trim(c.description) as conteudo, " +                 
                "         sum(a.correta) qtd_correta" + 
                " from 	simulado_status_aluno ssa " + 
                " inner join simulado_resolucao sr on sr.id_simulado = ssa.simulado_id and sr.id_aluno = ssa.id_aluno " + 
                " inner join questoes q on q.id = sr.id_questao " + 
                " inner join conteudos c on c.id = q.conteudo_id " + 
                " left join alternativa a on a.id = sr.id_alternativa " + 
                " where 	 " + 
                " 	ssa.simulado_id = " +  idSimulado + 
                " group by ssa.id_aluno, ssa.nome_aluno, trim(c.description) " + 
                " order by ssa.id_aluno, ssa.nome_aluno, trim(c.description) ";

            List<AcertosDetalhado> rAcertosDetalhados = this.jdbcTemplate.query(sql, 
                new Object[]{},
                new RowMapper<AcertosDetalhado>(){
                    public AcertosDetalhado mapRow(ResultSet rs, int numRow) throws SQLException{
                        AcertosDetalhado acertosDetalhado = new AcertosDetalhado();
                        
                        acertosDetalhado.setIdAluno(rs.getLong("id_aluno"));
                        acertosDetalhado.setNomeAluno(rs.getString("nome_aluno"));
                        acertosDetalhado.setConteudo(rs.getString("conteudo"));                        
                        acertosDetalhado.setCorreta(rs.getString("qtd_correta"));
                        return acertosDetalhado;
                    }
                }
            );                

            return rAcertosDetalhados;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static double formatDecimal(Double valor){

        BigDecimal bd = new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
        double newValeu = bd.doubleValue();
        return newValeu;
    }
}