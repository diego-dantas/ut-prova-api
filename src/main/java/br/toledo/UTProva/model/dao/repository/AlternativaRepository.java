package br.toledo.UTProva.model.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.toledo.UTProva.model.dao.entity.AlternativaEntity;
import br.toledo.UTProva.model.dao.entity.QuestaoEntity;

public interface AlternativaRepository extends JpaRepository<AlternativaEntity, Long>{

    List<AlternativaEntity> findByQuestao(QuestaoEntity questaoEntity);
}