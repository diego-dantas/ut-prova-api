package br.toledo.UTProva.reports.enade;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnadeVO {

    private Long id;
    private String ano;
    private Long codArea;
    private String area;
    private Long codIES;
    private String nomeIES;
    private Double notaBrutaFG;
    private Double notaPradonizadaFG;
    private Double notaBrutaCE;
    private Double notaPradonizadaCE;    
    private Double notaBrutaGeral;    
    private Double conceitoEnadeContinuo;    
    private int conceitoEnadeFaixa;
    private LocalDateTime dataHoraImportacao;
    
}