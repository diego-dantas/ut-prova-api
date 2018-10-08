package br.toledo.UTProva.model.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.toledo.UTProva.model.dao.entity.HabilidadeEntity;

public interface HabilidadeRepository extends JpaRepository<HabilidadeEntity, Long>{
    
}