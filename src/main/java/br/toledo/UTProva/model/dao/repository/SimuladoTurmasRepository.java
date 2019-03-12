package br.toledo.UTProva.model.dao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoTurmasEntity;

public interface SimuladoTurmasRepository extends JpaRepository<SimuladoTurmasEntity, Long>{
    
    List<SimuladoTurmasEntity> findBySimulado(SimuladoEntity simulado);

    @Query(value = "select * from simulado_turmas where simulado_id =:simulado ", nativeQuery = true)
    List<SimuladoTurmasEntity> findTurmasBySimulado(@Param("simulado") Long simulado);

    @Query(value = "select * from simulado_turmas where periodo_letivo  =:periodoLetivo and id_turma in:ids ", nativeQuery = true)
    List<SimuladoTurmasEntity> findByTurmasAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                     @Param("ids") List<String> turmas);

    @Query(value = "select * from simulado_turmas where periodo_letivo  =:periodoLetivo and id_turma =:id ", nativeQuery = true)
    List<SimuladoTurmasEntity> findByTurmaAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                     @Param("id") String turmas);

    @Query(value = "select * from simulado_turmas where periodo_letivo  =:periodoLetivo and id_curso =:id and id_turma =:idTurma", nativeQuery = true)
    List<SimuladoTurmasEntity> findByTurmasCursoAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                            @Param("id") String idCurso,
                                                            @Param("idTurma") String idTurma);

    @Query(value = "select * from simulado_turmas where periodo_letivo  =:periodoLetivo and id_curso in:ids ", nativeQuery = true)
    List<SimuladoTurmasEntity> findTurmasByCursosAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                     @Param("ids") List<String> cursos);

    @Query(value = "select distinct id_turma, nome, id_curso from simulado_turmas where periodo_letivo  =:periodoLetivo and id_curso =:id ", nativeQuery = true)
    List<SimuladoTurmasEntity> findIdTurmasByCursosAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                @Param("id") String cursos);
    
    @Query(value = "select count(simulado_id) from simulado_turmas where simulado_id =:id ", nativeQuery = true)
    int countByIdSimulado(@Param("id") Long id);   
}