package br.toledo.UTProva.model.dao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.toledo.UTProva.model.dao.entity.SimuladoCursosEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;

public interface SimuladoCursosRepository extends JpaRepository<SimuladoCursosEntity, Long>{
    
    List<SimuladoCursosEntity> findBySimulado(SimuladoEntity simulado);

    @Query(value = "select * from simulado_cursos where simulado_id =:simulado ", nativeQuery = true)
    List<SimuladoCursosEntity> findCursoBySimulado(@Param("simulado") Long simulado);

    @Query(value = "select * from simulado_cursos where simulado_id =:simulado ", nativeQuery = true)
    List<SimuladoCursosEntity> findCursoBySimuladoAluno(@Param("simulado") Long simulado);

    @Query(value = "select * from simulado_cursos where periodo_letivo  =:periodoLetivo and id_curso =:id ", nativeQuery = true)
    List<SimuladoCursosEntity> findSimuladoByCursoAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                             @Param("id") String id);
    
    @Query(value = "select * from simulado_cursos where periodo_letivo  =:periodoLetivo and id_curso in:ids ", nativeQuery = true)
    List<SimuladoCursosEntity> findByCursoAndPeriodo(@Param("periodoLetivo") int peridoLetivo, 
                                                     @Param("ids") List<String> cursos);



    @Query(value = "select count(simulado_id) from simulado_cursos where simulado_id =:id ", nativeQuery = true)
    int countByIdSimulado(@Param("id") Long id);
    
   
}