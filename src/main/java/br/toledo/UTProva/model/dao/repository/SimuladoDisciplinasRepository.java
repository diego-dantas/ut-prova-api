package br.toledo.UTProva.model.dao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.toledo.UTProva.model.dao.entity.SimuladoDisciplinasEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;

public interface SimuladoDisciplinasRepository extends JpaRepository<SimuladoDisciplinasEntity, Long>{
    
    List<SimuladoDisciplinasEntity> findBySimulado(SimuladoEntity simulado);
    
    @Query(value = "select * from simulado_disciplinas where simulado_id =:simulado ", nativeQuery = true)
    List<SimuladoDisciplinasEntity> findDisciplinasBySimulado(@Param("simulado") Long simulado);

    @Query(value = "select * from simulado_disciplinas where periodo_letivo  =:periodoLetivo and id_disciplina in:ids ", nativeQuery = true)
    List<SimuladoDisciplinasEntity> findByDisciplinasAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                     @Param("ids") List<String> disciplinas);

    @Query(value = "select * from simulado_disciplinas where periodo_letivo  =:periodoLetivo and id_disciplina =:id ", nativeQuery = true)
    List<SimuladoDisciplinasEntity> findByDisciplinaAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                     @Param("id") String disciplinas);
    
    @Query(value = "select * from simulado_disciplinas where periodo_letivo  =:periodoLetivo and id_turma =:id ", nativeQuery = true)
    List<SimuladoDisciplinasEntity> findByTurmasAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                            @Param("id") String turmas);

    @Query(value = "select * from simulado_disciplinas where periodo_letivo  =:periodoLetivo and id_turma in:ids ", nativeQuery = true)
    List<SimuladoDisciplinasEntity> findByInTurmasAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                            @Param("ids") List<String> turmas);

    @Query(value = "select count(simulado_id) from simulado_disciplinas where simulado_id =:id ", nativeQuery = true)
    int countByIdSimulado(@Param("id") Long id);
    
   
}