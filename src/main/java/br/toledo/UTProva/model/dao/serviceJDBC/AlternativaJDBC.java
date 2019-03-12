package br.toledo.UTProva.model.dao.serviceJDBC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlternativaJDBC {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int deleteAlternativa(Long idQuestao){
        int del = 0;
        try {
            String sql = "delete from alternativa where questao_id = " + idQuestao;
            del = this.jdbcTemplate.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return del;
    }
}