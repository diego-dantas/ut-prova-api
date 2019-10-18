package br.toledo.UTProva.reports.enade;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnadeRepository extends JpaRepository<EnadeEntity, Long>{
    
    @Query(value = "select count(distinct(ano_enade)) qtd from planilha_enade where ano_enade =:ano and cod_area =:codArea ", nativeQuery = true)
    int countEnadeAno(@Param("ano") String ano, @Param("codArea") Long codArea);

    @Query(value = "select distinct ano_enade, cod_area, area, data_hora_importacao from planilha_enade ", nativeQuery = true)
    List<EnadeEntity> getDistinctAnoCodArea();

    @Query(value = "select * from planilha_enade where ano_enade =:ano and cod_area =:codArea ", nativeQuery = true)
    List<EnadeEntity> getAllPlanEnade(@Param("ano") String ano, @Param("codArea") Long codArea);
    
}