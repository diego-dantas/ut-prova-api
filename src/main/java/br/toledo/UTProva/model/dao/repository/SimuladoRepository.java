package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.toledo.UTProva.model.dao.entity.SimuladoEntity;

public interface SimuladoRepository extends JpaRepository<SimuladoEntity, Long>{

    @Query(value = "select * from simulados where rascunho = false and id =:id ", nativeQuery = true)
    SimuladoEntity findByAluno(@Param("id") Long id);


    @Query(value = "select 	s.id, " +
                        " s.data_hora_inicial, " +
                        " s.data_hora_final, " +
                        " s.nome, " +
                        " s.rascunho, " +
                        " if(sa.simulado_status_id > 0, " +
                        "    (select descricao from simulado_status where id =	sa.simulado_status_id), " +
                        "    'Pendente' "+
                        " ) as status "+
                    " from 	simulados s "+
                    " left join simulado_status_aluno sa on sa.simulado_id = s.id and sa.id_aluno =:ididAluno " +
                    " where s.id in:idsSimulado and s.rascunho = false ", nativeQuery = true)
    List<SimuladoEntity> findSimuladosAlunos(@Param("ididAluno") String idAluno, @Param("idsSimulado") List<Long> idsSimulado);
}