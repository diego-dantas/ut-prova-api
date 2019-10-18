package br.toledo.UTProva.reports.enade;

import java.util.List;

import br.toledo.UTProva.model.dto.SimuladoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnadeReportsVO {

    private String ano;
    private Long codArea;
    private List<SimuladoDTO> simulados;
}