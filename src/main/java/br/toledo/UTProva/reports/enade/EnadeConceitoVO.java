package br.toledo.UTProva.reports.enade;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnadeConceitoVO {

    private String cod;
    private String nome;
    private Long idSimulado;
    private double conceito;
    private int faixa;
}