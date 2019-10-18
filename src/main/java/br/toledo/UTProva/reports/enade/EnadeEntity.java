package br.toledo.UTProva.reports.enade;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planilha_enade")
public class EnadeEntity implements Serializable{

    private static final long serialVersionUID = 1L; 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ano_enade", nullable = false)
    private String ano;

    @Column(name = "cod_area", nullable = false)
    private Long codArea;

    @Column(name = "area", nullable = false)
    private String area;

    @Column(name = "cod_ies", nullable = false)
    private Long codIES;

    @Column(name = "nome_ies", nullable = false)
    private String nomeIES;

    @Column(name = "nota_bruta_fg", nullable = false)
    private Double notaBrutaFG;

    @Column(name = "nota_padronizada_fg", nullable = false)
    private Double notaPradonizadaFG;

    @Column(name = "nota_bruta_ce", nullable = false)
    private Double notaBrutaCE;
    
    @Column(name = "nota_padronizada_ce", nullable = false)
    private Double notaPradonizadaCE;

    @Column(name = "nota_bruta_geral", nullable = false)
    private Double notaBrutaGeral;

    @Column(name = "conceito_enade_continuo", nullable = false)
    private Double conceitoEnadeContinuo;

    @Column(name = "conceito_enade_faixa", nullable = false)
    private int conceitoEnadeFaixa;
    
    @Column(name = "data_hora_importacao", nullable = false)
    private LocalDateTime dataHoraImportacao;


}