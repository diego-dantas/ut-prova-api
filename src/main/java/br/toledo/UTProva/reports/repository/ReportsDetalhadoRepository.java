package br.toledo.UTProva.reports.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import javax.print.Doc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import br.toledo.UTProva.reports.dto.DetalhadoReports;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReportsDetalhadoRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static DecimalFormat df = new DecimalFormat("#.##");


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
                "             ( " + 
                "             	select nota_formacao_geral  " + 
                "                 from aluno_questao_discursiva  " + 
                "                 where simulado_id = ssa.simulado_id   " + 
                "                 and id_aluno = ssa.id_aluno  " + 
                "             ) as nota_formacao_geral_discursiva, " + 
                "             ( " + 
                "             	select nota_conhecimento_especifico  " + 
                "                 from aluno_questao_discursiva  " + 
                "                 where simulado_id = ssa.simulado_id   " + 
                "                 and id_aluno = ssa.id_aluno  " + 
                "             ) as nota_conhecimento_especifico_discursiva " + 
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
                            double percentual = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                            double notaObjetivasFG = (((double) rs.getInt("questoes_certas_fg")) / ((double) rs.getInt("total_questoes_fg")))  * 100;
                            double notaObjetivasCE = (((double) rs.getInt("questoes_certas_ce")) / ((double) rs.getInt("total_questoes_ce")))  * 100;
                            double notaObjetivasTotal = (((double) rs.getInt("questoes_certas")) / ((double) rs.getInt("questoes_respondidas")))  * 100;
                            
                            double notaDiscursivaFG = (double) (rs.getInt("nota_formacao_geral_discursiva") * 10);
                            double notaDiscursivaCE = (double) (rs.getInt("nota_conhecimento_especifico_discursiva") * 10);

                            double notaFinalFG = 0;
                            double notaFinalCE = 0;
                            double notaFinal = 0;
                            if(rs.getBoolean("enade")){
                                notaFinalFG = (notaObjetivasFG * 0.6) + (notaDiscursivaFG * 0.4);
                                notaFinalCE = (notaObjetivasCE * 0.85) + (notaDiscursivaCE * 0.15);
                                notaFinal   = (notaFinalFG * 0.25) + (notaFinalCE * 0.75);
                            }else{
                                notaFinalFG = (notaObjetivasFG * 0.5) + (notaDiscursivaFG * 0.5);
                                notaFinalCE = (notaObjetivasCE * 0.5) + (notaDiscursivaCE * 0.5);
                                notaFinal   = (notaFinalFG * 0.5) + (notaFinalCE * 0.5);
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



    private static double formatDecimal(Double valor){

        BigDecimal bd = new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
        double newValeu = bd.doubleValue();
        return newValeu;
    }
}