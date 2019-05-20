package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.toledo.UTProva.model.dao.entity.HabilidadeEntity;

public interface HabilidadeRepository extends JpaRepository<HabilidadeEntity, Long>{

    @Query(value = "select count(id) from questoes s where s.habilidade_id =:id ", nativeQuery = true)
    int countQuestaoByHabilidade(@Param("id") Long id);

    @Query(value = "select * from habilidades where status = true order by description", nativeQuery = true)
    List<HabilidadeEntity>  findAtivas();

    @Query(value = "select * from habilidades where status = true and area_conhecimento_id in:id order by description", nativeQuery = true)
    List<HabilidadeEntity>  findAtivasByAreaCenhecimento(@Param("id") List<Long> id);
    
}