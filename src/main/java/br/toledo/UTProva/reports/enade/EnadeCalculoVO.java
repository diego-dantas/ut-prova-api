package br.toledo.UTProva.reports.enade;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnadeCalculoVO {

    private Long ies;
    private double fg_jk; 
    private double fg_jk_mFG; 
    private double zFG; 
    private double npFG; 
    private double ce_jk; 
    private double ce_jk_mCE; 
    private double zCE; 
    private double npCE;
    private double conceito;
    private int faixa;     
}