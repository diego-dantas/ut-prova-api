package br.toledo.UTProva.model.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.toledo.UTProva.model.dao.entity.ConteudoEntity;

public interface ConteudoRepository extends JpaRepository<ConteudoEntity, Long>{
    
}