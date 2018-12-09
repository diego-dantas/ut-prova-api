package br.toledo.UTProva.model.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.toledo.UTProva.model.dao.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>{

    UsuarioEntity findByUsuario(String usuario);
}