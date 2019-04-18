package br.toledo.UTProva.model.dao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoQuestoesEntity;

public interface SimuladoQuestoesRepository extends JpaRepository<SimuladoQuestoesEntity, Long>{
    
    List<SimuladoQuestoesEntity> findBySimulado(SimuladoEntity simulado);
    
    @Query(value = "select * from simulado_questoes where simulado_id =:idSimulado ", nativeQuery = true)
    List<SimuladoQuestoesEntity> findByQuestoes(@Param("idSimulado") Long idSimulado);

    @Query(value = "select count(simulado_id) from simulado_questoes where simulado_id =:id ", nativeQuery = true)
    int countByIdSimulado(@Param("id") Long id);

    @Query(value = "select count(simulado_id) from simulado_questoes where questao_id =:id ", nativeQuery = true)
    int countSimuladoByQuestao(@Param("id") Long id);
    
    @Query(value = "select count(id) from simulado_resolucao where id_questao =:id", nativeQuery = true)
    int countSimuladoAluno(@Param("id") Long id);
   
}