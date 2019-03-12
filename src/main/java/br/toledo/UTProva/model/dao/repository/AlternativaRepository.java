package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.toledo.UTProva.model.dao.entity.AlternativaEntity;
import br.toledo.UTProva.model.dao.entity.QuestaoEntity;

public interface AlternativaRepository extends JpaRepository<AlternativaEntity, Long>{

    List<AlternativaEntity> findByQuestao(QuestaoEntity questaoEntity);

    @Query(value = "select * from alternativa where questao_id =:id ", nativeQuery = true)
    List<AlternativaEntity> findAlternativasByQuestao(@Param("id") Long id);

}