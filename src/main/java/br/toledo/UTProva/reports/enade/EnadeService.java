package br.toledo.UTProva.reports.enade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.toledo.UTProva.reports.ExcelUseful;
import br.toledo.UTProva.reports.dto.DetalhadoReports;
import br.toledo.UTProva.reports.repository.ReportsDetalhadoRepository;


@Service
public class EnadeService {

    @Autowired
    EnadeRepository enadeRepository;
    
    @Autowired 
    ReportsDetalhadoRepository reportsDetalhadoRepository;

    @Autowired
    ExcelUseful excelUseful;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<EnadeVO> getAllPlanilhasEnade(){        
        try {            
            String sql = "select distinct ano_enade, cod_area, area, data_hora_importacao from planilha_enade";

            List<EnadeVO> enadeVOs = this.jdbcTemplate.query(sql,
            new Object[]{},
                new RowMapper<EnadeVO>(){
                    public EnadeVO mapRow(ResultSet rs, int numRow) throws SQLException{

                        EnadeVO enadeVO = new EnadeVO();
                        enadeVO.setAno(rs.getString("ano_enade"));
                        enadeVO.setCodArea(rs.getLong("cod_area"));
                        enadeVO.setArea(rs.getString("area"));                        
                        enadeVO.setDataHoraImportacao(rs.getTimestamp("data_hora_importacao").toLocalDateTime());
                        return enadeVO;
                    }
                }
            );

           
            return enadeVOs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deletePlanilha(String ano, Long codArea){
        int retorno =  -1;
        try {
            String sql = "delete from planilha_enade where id > 0 and ano_enade = " + ano + " and cod_area = " + codArea;
            retorno = this.jdbcTemplate.update(sql);
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
            return retorno;
        }
    }
    
    public void convertExcelToVO(MultipartFile file){

        try{
            List<EnadeVO> enadeVOs =  excelUseful.parseExcelToFile(file);        
            List<EnadeEntity> enadeEntities = new ArrayList<>();
    
            LocalDateTime dataHora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
            enadeVOs.forEach(n -> {
                EnadeEntity enadeEntity = new EnadeEntity();
                if((n.getAno() != null && !n.getAno().isEmpty()) && (n.getCodIES() != 1418)){
                    enadeEntity.setAno(n.getAno());
                    enadeEntity.setCodArea(n.getCodArea());
                    enadeEntity.setArea(n.getArea());
                    enadeEntity.setCodIES(n.getCodIES());
                    enadeEntity.setNomeIES(n.getNomeIES());
                    enadeEntity.setNotaBrutaFG(n.getNotaBrutaFG());
                    enadeEntity.setNotaPradonizadaFG(n.getNotaPradonizadaFG());
                    enadeEntity.setNotaBrutaCE(n.getNotaBrutaCE());
                    enadeEntity.setNotaPradonizadaCE(n.getNotaPradonizadaCE());
                    enadeEntity.setNotaBrutaGeral(n.getNotaBrutaGeral());
                    enadeEntity.setConceitoEnadeContinuo(n.getConceitoEnadeContinuo());
                    enadeEntity.setConceitoEnadeFaixa(n.getConceitoEnadeFaixa());
                    enadeEntity.setDataHoraImportacao(dataHora);
                    enadeEntities.add(enadeEntity);
                }                            
            });
            int qtd = enadeRepository.countEnadeAno(enadeEntities.get(0).getAno(), enadeEntities.get(0).getCodArea());
            System.out.println("quantidade " + qtd);
            if(qtd > 0) {
                String sql = "delete from planilha_enade where id > 0 and ano_enade = " + enadeEntities.get(0).getAno() + " and cod_area = " + enadeEntities.get(0).getCodArea();
                this.jdbcTemplate.update(sql);
            }
            enadeRepository.saveAll(enadeEntities); 
        } catch (Exception e) {
            e.printStackTrace();
        }
                
    }
   
    public List<EnadeConceitoVO> conceitoEnadeAluno(Long idSimulado){
        List<EnadeCalculoVO> enadeCalculoVOs = new ArrayList<>();
        List<EnadeConceitoVO> enadeConceitoVOs = new ArrayList<>();
        try {
            int qtdAlunos = 0, i = 0;
            double fgCK = 0, somaFG = 0, sfgK = 0, minFG = 0, maxFG = 0;
            double ceCK = 0, somaCE = 0, sceK = 0, minCE = 0, maxCE = 0;                   
            
            List<DetalhadoReports> detalhadoReports = reportsDetalhadoRepository.getDetalhado(idSimulado);
            
            
            for(DetalhadoReports n : detalhadoReports){
                EnadeCalculoVO enadeCalculoVO = new EnadeCalculoVO();                
                enadeCalculoVO.setIes(Long.valueOf(n.getMatricula()));
                enadeCalculoVO.setFg_jk(n.getNotaFinalFG());
                enadeCalculoVO.setCe_jk(n.getNotaFinalCE());
                enadeCalculoVOs.add(enadeCalculoVO);                
                somaFG += n.getNotaFinalFG();                
                somaCE += n.getNotaFinalCE();
                ++qtdAlunos;                
            }

            double[] arrayFG = new double[qtdAlunos];
            double[] arrayCE = new double[qtdAlunos];
            int ifg = 0, ice = 0;
            for(EnadeCalculoVO n : enadeCalculoVOs){
                arrayFG[ifg] = n.getFg_jk(); 
                ifg++;
            }
            for(EnadeCalculoVO n : enadeCalculoVOs){
                arrayCE[ice] = n.getCe_jk(); 
                ice++;
            }
            
            fgCK = somaFG / qtdAlunos;
            ceCK = somaCE / qtdAlunos;
            sfgK = Math.sqrt(new Variance().evaluate(arrayFG));
            sceK = Math.sqrt(new Variance().evaluate(arrayCE));        
            enadeCalculoVOs.clear();
            
            for(DetalhadoReports n : detalhadoReports){
                EnadeCalculoVO enadeCalculoVO = new EnadeCalculoVO();                
                enadeCalculoVO.setIes(Long.valueOf(n.getMatricula()));
                enadeCalculoVO.setFg_jk(n.getNotaFinalFG());
                enadeCalculoVO.setCe_jk(n.getNotaFinalCE());
                enadeCalculoVO.setFg_jk_mFG(n.getNotaFinalFG() - fgCK);
                enadeCalculoVO.setCe_jk_mCE(n.getNotaFinalCE() - ceCK);

                double zfg = 0, zce = 0;
                zfg = enadeCalculoVO.getFg_jk_mFG() / sfgK;
                zce = enadeCalculoVO.getCe_jk_mCE() / sceK;
                if(zfg < -3 || zfg > 3) zfg = 0;
                if(zce < -3 || zce > 3) zce = 0;
                enadeCalculoVO.setZFG(zfg);
                enadeCalculoVO.setZCE(zce);

                if(i == 0){
                    minFG = zfg;
                    maxFG = zfg;
                    minCE = zce;
                    maxCE = zce;
                    i++;
                }else{
                    if(zfg < minFG) minFG = zfg;;
                    if(zfg > maxFG) maxFG = zfg;;
                    if(zce < minCE) minCE = zce;;
                    if(zce > maxCE) maxCE = zce;;
                }
                
                enadeCalculoVOs.add(enadeCalculoVO);                                                                                                 
            }
            final double minF = minFG, maxF = maxFG;
            final double minC = minCE, maxC = maxCE;
            
            enadeCalculoVOs.forEach(n -> {
                n.setNpFG(notaPadronizada(n.getZFG(), minF, maxF));
                n.setNpCE(notaPadronizada(n.getZCE(), minC, maxC));   
                n.setConceito(conceitoEnade(n.getNpFG(), n.getNpCE()));                                          
                n.setFaixa(faixaEnade(n.getConceito())); 
                EnadeConceitoVO conceitoVO = new EnadeConceitoVO();
                conceitoVO.setCod(String.valueOf(n.getIes()));
                conceitoVO.setNome("");
                conceitoVO.setIdSimulado(idSimulado);
                conceitoVO.setConceito(n.getConceito()); 
                conceitoVO.setFaixa(n.getFaixa()); 

                enadeConceitoVOs.add(conceitoVO);
            
            });  
            return enadeConceitoVOs;                        
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return enadeConceitoVOs;
    }

    public EnadeConceitoVO conceitoEnade(Long idSimulado){
        List<EnadeCalculoVO> enadeCalculoVOs = new ArrayList<>();
        try {
            int qtdAlunos = 0, qtdAlunosUT = 0, i = 0;
            double fgCK = 0, somaFG = 0, sfgK = 0, minFG = 0, maxFG = 0;
            double ceCK = 0, somaCE = 0, sceK = 0, minCE = 0, maxCE = 0;                   
            double somaFGUT = 0, notaFGUT = 0;
            double somaCEUT = 0, notaCEUT = 0;
            List<DetalhadoReports> detalhadoReports = reportsDetalhadoRepository.getDetalhado(idSimulado);
            
            
            for(DetalhadoReports n : detalhadoReports){                              
                somaFGUT += n.getNotaFinalFG();                
                somaCEUT += n.getNotaFinalCE();
                ++qtdAlunosUT;                
            }
            
            notaFGUT = somaFGUT / qtdAlunosUT;
            notaCEUT = somaCEUT / qtdAlunosUT;
            

            List<EnadeEntity> enadeEntities = enadeRepository.getAllPlanEnade("2014", 72L);
            List<EnadeCalculoVO> enadeCalculoVOs2 = new ArrayList<>();
            for(EnadeEntity n : enadeEntities){
                EnadeCalculoVO enadeCalculoVO = new EnadeCalculoVO();                
                if((n.getAno() != null && !n.getAno().isEmpty()) && (n.getCodIES() != 1418)){
                    enadeCalculoVO.setIes(n.getCodIES());
                    enadeCalculoVO.setFg_jk(n.getNotaBrutaFG());
                    enadeCalculoVO.setCe_jk(n.getNotaBrutaCE());
                    enadeCalculoVOs.add(enadeCalculoVO);
                    somaFG += n.getNotaBrutaFG();                
                    somaCE += n.getNotaBrutaCE();
                    ++qtdAlunos;
                }   
            }
            //Alimenta com as notas da toledo !!
            EnadeCalculoVO enadeCalculoUT = new EnadeCalculoVO();                
            enadeCalculoUT.setIes(1418L);
            enadeCalculoUT.setFg_jk(notaFGUT);
            enadeCalculoUT.setCe_jk(notaCEUT);
            enadeCalculoVOs.add(enadeCalculoUT);

            somaFG += notaFGUT;
            somaCE += notaCEUT;
            ++qtdAlunos;

            double[] arrayFG = new double[qtdAlunos];
            double[] arrayCE = new double[qtdAlunos];
            int ifg = 0, ice = 0;
            for(EnadeCalculoVO n : enadeCalculoVOs){
                arrayFG[ifg] = n.getFg_jk(); 
                ifg++;
            }
            for(EnadeCalculoVO n : enadeCalculoVOs){
                arrayCE[ice] = n.getCe_jk(); 
                ice++;
            }
            
            fgCK = somaFG / qtdAlunos;
            ceCK = somaCE / qtdAlunos;


            
            sfgK = Math.sqrt(new Variance().evaluate(arrayFG));
            sceK = Math.sqrt(new Variance().evaluate(arrayCE));        
           

            for(EnadeCalculoVO n : enadeCalculoVOs){
                EnadeCalculoVO enadeCalculoVO = new EnadeCalculoVO();                
                enadeCalculoVO.setIes(n.getIes());
                enadeCalculoVO.setFg_jk(n.getFg_jk());
                enadeCalculoVO.setCe_jk(n.getCe_jk());
                enadeCalculoVO.setFg_jk_mFG(n.getFg_jk() - fgCK);
                enadeCalculoVO.setCe_jk_mCE(n.getCe_jk() - ceCK);

                double zfg = 0, zce = 0;
                zfg = enadeCalculoVO.getFg_jk_mFG() / sfgK;
                zce = enadeCalculoVO.getCe_jk_mCE() / sceK;
                if(zfg < -3 || zfg > 3) zfg = 0;
                if(zce < -3 || zce > 3) zce = 0;
                enadeCalculoVO.setZFG(zfg);
                enadeCalculoVO.setZCE(zce);

                if(i == 0){
                    minFG = zfg;
                    maxFG = zfg;
                    minCE = zce;
                    maxCE = zce;
                    i++;
                }else{
                    if(zfg < minFG) minFG = zfg;;
                    if(zfg > maxFG) maxFG = zfg;;
                    if(zce < minCE) minCE = zce;;
                    if(zce > maxCE) maxCE = zce;;
                }
                
                enadeCalculoVOs2.add(enadeCalculoVO);                                                                                                 
            }
            final double minF = minFG, maxF = maxFG;
            final double minC = minCE, maxC = maxCE;
            EnadeConceitoVO conceitoVO = new EnadeConceitoVO();
            enadeCalculoVOs2.forEach(n -> {
                n.setNpFG(notaPadronizada(n.getZFG(), minF, maxF));
                n.setNpCE(notaPadronizada(n.getZCE(), minC, maxC));   
                n.setConceito(conceitoEnade(n.getNpFG(), n.getNpCE()));                                          
                n.setFaixa(faixaEnade(n.getConceito())); 
                if(n.getIes() == 1418) {
                    conceitoVO.setCod(String.valueOf(1418));
                    conceitoVO.setNome("UNITOLEDO");
                    conceitoVO.setIdSimulado(idSimulado);
                    conceitoVO.setConceito(n.getConceito()); 
                    conceitoVO.setFaixa(n.getFaixa()); 
                }                
            });  
            
            return conceitoVO;
        } catch (Exception e) {
            e.printStackTrace();            
        }
        return null;
    }

    private double notaPadronizada(double z, double min, double max){
        double nota = 5 * (z - min) / (max - min);
        return nota;
    }

    private double conceitoEnade(double npFG, double npCE){
        double conceito = (0.25 * npFG) + (0.75 * npCE);
        return conceito;
    }

    private int faixaEnade(double conceito){
        int faixa = 0; 
        if(conceito < 0.945){
            faixa = 1;  
        }else if(conceito < 1.945) {
            faixa = 2; 
        }else if(conceito < 2.945){
            faixa = 3;
        }else if(conceito < 3.945){
            faixa = 4;
        }else{
            faixa = 5;  
        } 
        return faixa;
    }
}