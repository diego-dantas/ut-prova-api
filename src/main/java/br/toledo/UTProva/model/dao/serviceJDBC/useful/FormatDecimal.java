package br.toledo.UTProva.model.dao.serviceJDBC.useful;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatDecimal {

    public static double formatDecimal(Double valor){

        BigDecimal bd = new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP);
        double newValeu = bd.doubleValue();
        return newValeu;
    }
}