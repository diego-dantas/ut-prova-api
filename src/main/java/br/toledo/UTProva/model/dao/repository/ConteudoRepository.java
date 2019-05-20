package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.toledo.UTProva.model.dao.entity.AreaConhecimentoEntity;
import br.toledo.UTProva.model.dao.entity.ConteudoEntity;

public interface ConteudoRepository extends JpaRepository<ConteudoEntity, Long>{

    @Query(value = "select count(id) from questoes s where s.conteudo_id =:id ", nativeQuery = true)
    int countQuestao(@Param("id") Long id);

    @Query(value = "select * from conteudos where status = true order by description", nativeQuery = true)
    List<ConteudoEntity>  findAtivas();

    @Query(value = "select * from conteudos where area_conhecimento_id =:id order by description", nativeQuery = true)
    List<ConteudoEntity> findByAreaConhecimento(@Param("id") Long id);

    @Query(value = "select * from conteudos where status = true and area_conhecimento_id in:id order by description", nativeQuery = true)
    List<ConteudoEntity> findConteudosAtivosByAreaConhecimento(@Param("id") List<Long> id);
    
}