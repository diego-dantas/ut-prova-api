package br.toledo.UTProva.model.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.toledo.UTProva.model.RoleName;
import br.toledo.UTProva.model.dao.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>{
    Optional<RoleEntity> findByName(RoleName roleName);
}