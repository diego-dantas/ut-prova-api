package br.toledo.UTProva.model.dao.repository;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import br.toledo.UTProva.model.dao.entity.QuestaoEntity;

public interface QuestaoRepository extends JpaRepository<QuestaoEntity, Long>{


    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Query(value = "select * from questoes where id in:ids ",
        nativeQuery = true)
    List<QuestaoEntity> findByFilter(
                            @Param("ids")       List<Long> questoes
                        );
   
}