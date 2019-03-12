package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.toledo.UTProva.model.dao.entity.AreaConhecimentoEntity;

public interface AreaConhecimentoRepository extends JpaRepository<AreaConhecimentoEntity, Long>{

    @Query(value = "select count(id) from questoes s where s.area_conhecimento_id =:id ", nativeQuery = true)
    int countQuestao(@Param("id") Long id);

    @Query(value = "select * from areas_conhecimento where status = true", nativeQuery = true)
    List<AreaConhecimentoEntity>  findAreaAtivas();
    
}