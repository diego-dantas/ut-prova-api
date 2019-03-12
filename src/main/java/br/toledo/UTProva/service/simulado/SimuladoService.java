package br.toledo.UTProva.service.simulado;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.toledo.UTProva.model.dao.entity.SimuladoCursosEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoDisciplinasEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoTurmasEntity;
import br.toledo.UTProva.model.dao.repository.AlternativaRepository;
import br.toledo.UTProva.model.dao.repository.QuestaoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoCursosRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoDisciplinasRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoFilterRepositoty;
import br.toledo.UTProva.model.dao.repository.SimuladoQuestoesRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoTurmasRepository;
import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.DisciplinasDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;
import br.toledo.UTProva.model.dto.TurmasDTO;

@Service
public class SimuladoService {

    @Autowired
    private SimuladoRepository simuladoRepository;
    @Autowired
    private SimuladoQuestoesRepository simuladoQuestoesRepository;
    @Autowired
    private SimuladoCursosRepository simuladoCursosRepository;
    @Autowired
    private SimuladoTurmasRepository simuladoTurmasRepository;
    @Autowired
    private SimuladoDisciplinasRepository simuladoDisciplinasRepository;
    @Autowired
    private QuestaoRepository questaoRepository;
    @Autowired
    private AlternativaRepository alternativaRepository;
    @Autowired
    private SimuladoFilterRepositoty simuladoFilterRepositoty;

    public List<SimuladoDTO> getQuestoes(List<Long> ids) {
        
        List<SimuladoDTO> simuladoDTOs = new ArrayList<>();

        for(Long s : ids){
            SimuladoEntity simuladoRetorno = simuladoRepository.getOne(s);
            SimuladoDTO sDTO = new SimuladoDTO();
            List<CursosDTO> cursosDTOs = new ArrayList<>();
            List<TurmasDTO> turmasDTOs = new ArrayList<>();
            List<DisciplinasDTO> disciplinasDTOs = new ArrayList<>();

            sDTO.setId(simuladoRetorno.getId());
            sDTO.setNome(simuladoRetorno.getNome());
            sDTO.setRascunho(simuladoRetorno.isRascunho());
            sDTO.setDataHoraInicial(simuladoRetorno.getDataHoraInicial());
            sDTO.setDataHoraFinal(simuladoRetorno.getDataHoraFinal());
            
            List<SimuladoCursosEntity> sc = simuladoCursosRepository.findCursoBySimulado(s);
            for (SimuladoCursosEntity si : sc) {
                CursosDTO cursosDTO = new CursosDTO();    
                cursosDTO.setId(si.getIdCurso());
                cursosDTO.setNome(si.getNome());
                cursosDTOs.add(cursosDTO);
            }
            sDTO.setCursos(cursosDTOs);


            List<SimuladoTurmasEntity> st = simuladoTurmasRepository.findTurmasBySimulado(s);
            for (SimuladoTurmasEntity si : st) {
               TurmasDTO turmasDTO = new  TurmasDTO(); 
                turmasDTO.setId(si.getIdTurma());
                turmasDTO.setNome(si.getNome());
                turmasDTOs.add(turmasDTO);
            }
            sDTO.setTurmas(turmasDTOs);


            List<SimuladoDisciplinasEntity> sd = simuladoDisciplinasRepository.findDisciplinasBySimulado(s);
            for (SimuladoDisciplinasEntity si : sd) {
                DisciplinasDTO disciplinasDTO = new DisciplinasDTO();    
                disciplinasDTO.setId(si.getIdDisciplina());
                disciplinasDTO.setNome(si.getNome());
                disciplinasDTOs.add(disciplinasDTO);
            }
            sDTO.setDisciplinas(disciplinasDTOs);
          
            simuladoDTOs.add(sDTO);
        }

        return simuladoDTOs;
    }


    
}