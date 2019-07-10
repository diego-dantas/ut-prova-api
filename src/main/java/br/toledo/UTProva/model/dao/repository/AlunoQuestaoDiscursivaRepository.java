package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.toledo.UTProva.model.dao.entity.AlunoQuestaoDiscursiva;

public interface AlunoQuestaoDiscursivaRepository extends JpaRepository<AlunoQuestaoDiscursiva, Long>{

    @Query(value = "select * from aluno_questao_discursiva where simulado_id =:idSimulado", nativeQuery = true)
    List<AlunoQuestaoDiscursiva> findByIdSimulado(@Param("idSimulado") Long idSimulado);
}