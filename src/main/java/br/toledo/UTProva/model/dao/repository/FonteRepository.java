package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.toledo.UTProva.model.dao.entity.FonteEntity;

public interface FonteRepository extends JpaRepository<FonteEntity, Long>{

    @Query(value = "select count(id) from questoes s where s.fonte_id =:id ", nativeQuery = true)
    int countQuestao(@Param("id") Long id);

    @Query(value = "select * from fonte where status = true order by description", nativeQuery = true)
    List<FonteEntity>  findFontesAtivas();
}