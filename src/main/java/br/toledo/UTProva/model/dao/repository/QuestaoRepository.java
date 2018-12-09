package br.toledo.UTProva.model.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.toledo.UTProva.model.dao.entity.QuestaoEntity;

public interface QuestaoRepository extends JpaRepository<QuestaoEntity, Long>{

}