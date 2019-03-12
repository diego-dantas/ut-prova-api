package br.toledo.UTProva.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.AlternativaEntity;
import br.toledo.UTProva.model.dao.entity.QuestaoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoCursosEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoDisciplinasEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoQuestoesEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoResolucaoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoStatusEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoTurmasEntity;
import br.toledo.UTProva.model.dao.repository.AlternativaRepository;
import br.toledo.UTProva.model.dao.repository.QuestaoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoCursosRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoDisciplinasRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoFilterRepositoty;
import br.toledo.UTProva.model.dao.repository.QuestaoFilterRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoQuestoesRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoResolucaoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoStatusRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoTurmasRepository;
import br.toledo.UTProva.model.dao.serviceJDBC.SimuladoJDBC;
import br.toledo.UTProva.model.dao.serviceJDBC.useful.DynamicSQL;
import br.toledo.UTProva.model.dto.AlternativaDTO;
import br.toledo.UTProva.model.dto.CursoRetornoDTO;
import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.DisciplinasDTO;
import br.toledo.UTProva.model.dto.DisciplinasRetornoDTO;
import br.toledo.UTProva.model.dto.QuestaoDTO;
import br.toledo.UTProva.model.dto.QuestaoRetornoDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;
import br.toledo.UTProva.model.dto.SimuladoResolucaoDTO;
import br.toledo.UTProva.model.dto.SimuladoRetornoDTO;
import br.toledo.UTProva.model.dto.SimuladoStatusDTO;
import br.toledo.UTProva.model.dto.TurmaRetornoDTO;
import br.toledo.UTProva.model.dto.TurmasDTO;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/api")
public class SimuladoController {

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
    @Autowired
    private SimuladoResolucaoRepository simuladoResolucaoRepository;
    @Autowired
    private QuestaoFilterRepository questaoFilterRepository;
    @Autowired
    private SimuladoJDBC simuladoJDBC;
    @Autowired
    private SimuladoStatusRepository simuladoStatusRepository;


    @PostMapping(value = "/createUpdateSimulado")
    public ResponseEntity<SimuladoDTO> postMethodName(@RequestBody SimuladoDTO simuladoDTO) {

        try {
            int qtd = 0;
            SimuladoEntity simulado = new SimuladoEntity();
            List<SimuladoQuestoesEntity> simuladoQuestoes = new ArrayList<>();
            List<SimuladoCursosEntity> simuladoCursos = new ArrayList<>();
            List<SimuladoTurmasEntity> simuladoTurmas= new ArrayList<>();
            List<SimuladoDisciplinasEntity> simuladoDisciplinas= new ArrayList<>();

            simulado.setId(simuladoDTO.getId());
            simulado.setNome(simuladoDTO.getNome());
            simulado.setRascunho(simuladoDTO.isRascunho());
            simulado.setStatus(simuladoDTO.getStatus());
            simulado.setDataHoraInicial(simuladoDTO.getDataHoraInicial());
            simulado.setDataHoraFinal(simuladoDTO.getDataHoraFinal());


            
            

            simulado = simuladoRepository.save(simulado);
            simuladoDTO.setId(simulado.getId());
            
            /** verifica se tem questoes para o simulado, caso tenha deleta e faz o insert das novas questoes */
            qtd = simuladoQuestoesRepository.countByIdSimulado(simulado.getId());
            if(qtd > 0) {
                List<SimuladoQuestoesEntity> questoesSimulado = simuladoQuestoesRepository.findBySimulado(simulado);
                simuladoQuestoesRepository.deleteAll(questoesSimulado);
                qtd = 0;
            }
            for (QuestaoDTO questao : simuladoDTO.getQuestoes()) {
                QuestaoEntity questaoEntity = new QuestaoEntity();
                SimuladoQuestoesEntity simuladoQuestao = new SimuladoQuestoesEntity();
                
                questaoEntity.setId(questao.getId());
                
                simuladoQuestao.setQuestao(questaoEntity);
                simuladoQuestao.setSimulado(simulado);

                simuladoQuestoes.add(simuladoQuestao);
            }            
            simuladoQuestoesRepository.saveAll(simuladoQuestoes);
            
              
             /** verifica se tem cursos para o simulado, caso tenha deleta e faz o insert das novas questoes */
            qtd = simuladoCursosRepository.countByIdSimulado(simulado.getId());
            if(qtd > 0) {
                List<SimuladoCursosEntity> cursosSimulado = simuladoCursosRepository.findBySimulado(simulado);
                simuladoCursosRepository.deleteAll(cursosSimulado);
                qtd = 0;
            }
            if(simuladoDTO.getCursos() != null && simuladoDTO.getCursos().size() > 0){
               for (CursosDTO curso : simuladoDTO.getCursos()) {
                   SimuladoCursosEntity cursosEntity = new SimuladoCursosEntity();
                   
                   
                   cursosEntity.setIdCurso(curso.getId()) ;
                   cursosEntity.setNome(curso.getNome());
                   cursosEntity.setIdPeriodoLetivo(curso.getIdPeriodoLetivo());
                   cursosEntity.setSimulado(simulado);
                  

                   simuladoCursos.add(cursosEntity);
               }            
               simuladoCursosRepository.saveAll(simuladoCursos);
            }
            
            

              /** verifica se tem Turmas para o simulado, caso tenha deleta e faz o insert das novas questoes */
              qtd = simuladoTurmasRepository.countByIdSimulado(simulado.getId());
              if(qtd > 0) {
                  List<SimuladoTurmasEntity> turmasSimulado = simuladoTurmasRepository.findBySimulado(simulado);
                  simuladoTurmasRepository.deleteAll(turmasSimulado);
                  qtd = 0;
              }
              if(simuladoDTO.getTurmas() != null && simuladoDTO.getTurmas().size() > 0){
                for (TurmasDTO turma : simuladoDTO.getTurmas()) {
                    SimuladoTurmasEntity turmasEntity = new SimuladoTurmasEntity();
                      
                      
                    turmasEntity.setIdTurma(turma.getId()) ;
                    turmasEntity.setNome(turma.getNome());
                    turmasEntity.setIdCurso(turma.getIdCurso());
                    turmasEntity.setIdPeriodoLetivo(turma.getIdPeriodoLetivo());
                    turmasEntity.setSimulado(simulado);
                     
      
                      simuladoTurmas.add(turmasEntity);
                  }            
                  simuladoTurmasRepository.saveAll(simuladoTurmas);
              }
              
             

               /** verifica se tem Disciplinas para o simulado, caso tenha deleta e faz o insert das novas questoes */
               qtd = simuladoDisciplinasRepository.countByIdSimulado(simulado.getId());
               if(qtd > 0) {
                   List<SimuladoDisciplinasEntity> disciplinasSimulado = simuladoDisciplinasRepository.findBySimulado(simulado);
                   simuladoDisciplinasRepository.deleteAll(disciplinasSimulado);
                   qtd = 0;
               }
               if(simuladoDTO.getDisciplinas() != null && simuladoDTO.getDisciplinas().size() > 0){
                for (DisciplinasDTO disciplina : simuladoDTO.getDisciplinas()) {
                    SimuladoDisciplinasEntity disciplinasEntity = new SimuladoDisciplinasEntity();
                      
                      
                    disciplinasEntity.setIdDisciplina(disciplina.getId()) ;
                    disciplinasEntity.setNome(disciplina.getNome());
                    disciplinasEntity.setIdTurma(disciplina.getIdTurma());
                    disciplinasEntity.setIdPeriodoLetivo(disciplina.getIdPeriodoLetivo());
                    disciplinasEntity.setSimulado(simulado);
                     
      
                    simuladoDisciplinas.add(disciplinasEntity);
                  }            
                  simuladoDisciplinasRepository.saveAll(simuladoDisciplinas);   
               }
               

            System.out.println(simuladoDTO.getDataHoraInicial());
            System.out.println(simuladoDTO.getDataHoraFinal());
            return ResponseEntity.ok(simuladoDTO);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar ou atualizar o simulado " + e);
        }
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/getSimulados")
    public ResponseEntity<List<Object>> getSimuladoAll(@RequestBody SimuladoDTO simuladoDTO) {

        try {           
            List<Object> retorno = new ArrayList<>();
            if(simuladoDTO.getCursos() != null){
                for (CursosDTO curso : simuladoDTO.getCursos()) {
                    
                    List<Long> idSimuladosCursos = new ArrayList<>();
                    
                    CursoRetornoDTO cursoRetornoDTO = new CursoRetornoDTO();

                    List<SimuladoCursosEntity> simuladosCurso = simuladoCursosRepository.findSimuladoByCursoAndPeriodo(curso.getIdPeriodoLetivo(), curso.getId());
                    simuladosCurso.forEach(n -> idSimuladosCursos.add(n.getSimulado().getId()));
                    cursoRetornoDTO.setSimulados(getSimulados(idSimuladosCursos));
                    
                    List<TurmaRetornoDTO> listTurma = new ArrayList<>();                     
                    List<SimuladoTurmasEntity> getTurmas = simuladoFilterRepositoty.getIdTurmas(curso.getIdPeriodoLetivo(), curso.getId());
                    for (SimuladoTurmasEntity turmas  : getTurmas) {
                        
                        List<Long> idSimuladosTurmas = new ArrayList<>();
                        TurmaRetornoDTO turmaRetornoDTO = new TurmaRetornoDTO();
                        turmaRetornoDTO.setId(turmas.getIdTurma());
                        turmaRetornoDTO.setNome(turmas.getNome());
                        turmaRetornoDTO.setIdPeriodoLetivo(turmas.getIdPeriodoLetivo());
                        turmaRetornoDTO.setIdCurso(curso.getId());
                        List<SimuladoTurmasEntity> simuladosTurma  = simuladoTurmasRepository.findByTurmasCursoAndPeriodo(curso.getIdPeriodoLetivo(), curso.getId(), turmas.getIdTurma());
                        simuladosTurma.forEach(n -> idSimuladosTurmas.add(n.getSimulado().getId()));
                        
                        turmaRetornoDTO.setSimulados(getSimulados(idSimuladosCursos));
                        listTurma.add(turmaRetornoDTO);

                        List<DisciplinasRetornoDTO> listDisciplinas = new ArrayList<>(); 
                        List<SimuladoDisciplinasEntity> getDisciplina = simuladoFilterRepositoty.getIdDisciplinas(curso.getIdPeriodoLetivo(), turmas.getIdTurma());
                        for (SimuladoDisciplinasEntity disciplina  : getDisciplina) {

                            List<Long> idSimuladosDisciplina = new ArrayList<>();
                            DisciplinasRetornoDTO disciplinasRetornoDTO = new DisciplinasRetornoDTO();
                            disciplinasRetornoDTO.setId(disciplina.getIdTurma());
                            disciplinasRetornoDTO.setNome(disciplina.getNome());
                            disciplinasRetornoDTO.setIdPeriodoLetivo(turmas.getIdPeriodoLetivo());
                            disciplinasRetornoDTO.setIdCurso(turmas.getIdCurso());
                            
                            List<SimuladoDisciplinasEntity> simuladosDisciplina  = simuladoDisciplinasRepository.findByTurmasAndPeriodo(curso.getIdPeriodoLetivo(), turmas.getIdTurma());
                            simuladosDisciplina.forEach(n -> idSimuladosDisciplina.add(n.getSimulado().getId()));

                            disciplinasRetornoDTO.setSimulados(getSimulados(idSimuladosCursos));
                            listDisciplinas.add(disciplinasRetornoDTO);
                        }
                        turmaRetornoDTO.setDisciplinas(listDisciplinas);
                    }

                    cursoRetornoDTO.setTurmas(listTurma);
                    cursoRetornoDTO.setId(curso.getId());
                    cursoRetornoDTO.setNome(curso.getNome());
                    cursoRetornoDTO.setIdPeriodoLetivo(curso.getIdPeriodoLetivo());

                    retorno.add(cursoRetornoDTO);
                }
            }

            if(simuladoDTO.getTurmas() != null){
                for (TurmasDTO turma : simuladoDTO.getTurmas()) {
                        
                    List<Long> idSimuladosTurmas = new ArrayList<>();
                    TurmaRetornoDTO turmaRetornoDTO = new TurmaRetornoDTO();
                    turmaRetornoDTO.setId(turma.getId());
                    turmaRetornoDTO.setNome(turma.getNome());
                    turmaRetornoDTO.setIdPeriodoLetivo(turma.getIdPeriodoLetivo());
                    turmaRetornoDTO.setIdCurso(turma.getIdCurso());

                    List<SimuladoTurmasEntity> simuladosTurma  = simuladoTurmasRepository.findByTurmaAndPeriodo(turma.getIdPeriodoLetivo(), turma.getId());
                    simuladosTurma.forEach(n -> idSimuladosTurmas.add(n.getSimulado().getId()));
                   
                    turmaRetornoDTO.setSimulados(getSimulados(idSimuladosTurmas));
                    

                    List<DisciplinasRetornoDTO> listDisciplinas = new ArrayList<>(); 
                    List<SimuladoDisciplinasEntity> getDisciplina = simuladoFilterRepositoty.getIdDisciplinas(turma.getIdPeriodoLetivo(), turma.getId());
                    
                    for (SimuladoDisciplinasEntity disciplina  : getDisciplina) {
                        
                        List<Long> idSimuladosDisciplina = new ArrayList<>();
                        DisciplinasRetornoDTO disciplinasRetornoDTO = new DisciplinasRetornoDTO();
                        disciplinasRetornoDTO.setId(disciplina.getIdDisciplina());
                        disciplinasRetornoDTO.setIdTurma(disciplina.getIdTurma());
                        disciplinasRetornoDTO.setNome(disciplina.getNome());
                        disciplinasRetornoDTO.setIdPeriodoLetivo(turma.getIdPeriodoLetivo());
                        disciplinasRetornoDTO.setIdCurso(turma.getIdCurso());
                        
                        List<SimuladoDisciplinasEntity> simuladosDisciplina  = simuladoDisciplinasRepository.findByTurmasAndPeriodo(turma.getIdPeriodoLetivo(), turma.getId());
                        simuladosDisciplina.forEach(n -> idSimuladosDisciplina.add(n.getSimulado().getId()));
                        disciplinasRetornoDTO.setSimulados(getSimulados(idSimuladosDisciplina));
                        listDisciplinas.add(disciplinasRetornoDTO);
                    }
                    turmaRetornoDTO.setDisciplinas(listDisciplinas);
                    


                    retorno.add(turmaRetornoDTO);
                }
            }

            if(simuladoDTO.getDisciplinas() != null){

                List<DisciplinasRetornoDTO> listDisciplinas = new ArrayList<>(); 
                for (DisciplinasDTO disciplina : simuladoDTO.getDisciplinas()) {

                    List<Long> idSimuladosDisciplina = new ArrayList<>();
                    DisciplinasRetornoDTO disciplinasRetornoDTO = new DisciplinasRetornoDTO();

                    disciplinasRetornoDTO.setId(disciplina.getId());
                    disciplinasRetornoDTO.setIdTurma(disciplina.getIdTurma());
                    disciplinasRetornoDTO.setNome(disciplina.getNome());
                    disciplinasRetornoDTO.setIdPeriodoLetivo(disciplina.getIdPeriodoLetivo());
                    
                    
                    List<SimuladoDisciplinasEntity> simuladosDisciplina  = simuladoDisciplinasRepository.findByDisciplinaAndPeriodo(disciplina.getIdPeriodoLetivo(), disciplina.getId());
                    simuladosDisciplina.forEach(n -> idSimuladosDisciplina.add(n.getSimulado().getId()));
                    disciplinasRetornoDTO.setSimulados(getSimulados(idSimuladosDisciplina));
                    
                    retorno.add(disciplinasRetornoDTO);
                }
            }

            return new ResponseEntity<>(retorno, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o simulado por curso" + e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/getAllSimulado")
    public ResponseEntity<List<SimuladoDTO>> getAllSimulado(@RequestBody SimuladoDTO simuladoDTO) {

        try {
            List<SimuladoDisciplinasEntity> simuladoDisciplinas = new ArrayList<>();
            List<SimuladoCursosEntity>      simuladoCursos      = new ArrayList<>();
            List<SimuladoTurmasEntity>      simuladoTurmas      = new ArrayList<>();
            List<SimuladoDTO>               simuladoDTOs        = new ArrayList<>();
            List<String> idsDisciplinas = new ArrayList<>();
            List<String> idsTurmas      = new ArrayList<>();
            List<String> idsCursos      = new ArrayList<>();
            List<Long>   idsSimulados   = new ArrayList<>();
            
            simuladoDTO.getCursos().forEach(n -> idsCursos.add(n.getId()));
            simuladoDTO.getTurmas().forEach(n -> idsTurmas.add(n.getId()));
            simuladoDTO.getDisciplinas().forEach(n -> idsDisciplinas.add(n.getId()));

            if(simuladoDTO.getCursos() != null && simuladoDTO.getCursos().size() > 0){
                simuladoDTO.getCursos().forEach(n -> idsCursos.add(n.getId()));
                simuladoCursos = simuladoCursosRepository.findByCursoAndPeriodo(simuladoDTO.getCursos().get(0).getIdPeriodoLetivo(), idsCursos);
                if(!simuladoCursos.isEmpty()){
                    simuladoCursos.forEach(n -> idsSimulados.add(n.getSimulado().getId()));
                }

                simuladoTurmas = simuladoTurmasRepository.findTurmasByCursosAndPeriodo(simuladoDTO.getCursos().get(0).getIdPeriodoLetivo(), idsCursos);
                if(!simuladoTurmas.isEmpty()){
                    simuladoTurmas.forEach(n -> idsTurmas.add(n.getIdTurma())); 
                    simuladoTurmas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));
                    simuladoDisciplinas = simuladoDisciplinasRepository.findByInTurmasAndPeriodo(simuladoDTO.getCursos().get(0).getIdPeriodoLetivo(), idsTurmas);
                }
                    
                if(!simuladoDisciplinas.isEmpty()){
                    simuladoDisciplinas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));
                }
                    
            }

            if(simuladoDTO.getTurmas() != null && simuladoDTO.getTurmas().size() > 0){
                simuladoDTO.getTurmas().forEach(n -> idsTurmas.add(n.getId()));
                simuladoTurmas = simuladoTurmasRepository.findByTurmasAndPeriodo(simuladoDTO.getTurmas().get(0).getIdPeriodoLetivo(), idsTurmas);
                if(!simuladoTurmas.isEmpty()){
                    simuladoTurmas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));
                    simuladoDisciplinas  = simuladoDisciplinasRepository.findByInTurmasAndPeriodo(simuladoDTO.getCursos().get(0).getIdPeriodoLetivo(), idsTurmas);
                }
                    
                if(!simuladoDisciplinas.isEmpty()){
                    simuladoDisciplinas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));
                }       
            }
            
            if(simuladoDTO.getDisciplinas() != null && simuladoDTO.getDisciplinas().size() > 0){
                simuladoDTO.getDisciplinas().forEach(n -> idsDisciplinas.add(n.getId()));
                simuladoDisciplinas = simuladoDisciplinasRepository.findByDisciplinasAndPeriodo(simuladoDTO.getDisciplinas().get(0).getIdPeriodoLetivo(), idsDisciplinas);
                if(!simuladoDisciplinas.isEmpty()){
                    simuladoDisciplinas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));
                }
            }   
            
            if(idsSimulados.size() > 0){
                List<SimuladoEntity> simuladosRetorno = simuladoJDBC.findSimuladosAdmin(idsSimulados);
                for(SimuladoEntity  simuladoRetorno : simuladosRetorno){
                    
                    
                    SimuladoDTO sDTO = new SimuladoDTO();
                    List<CursosDTO> cursosDTOs = new ArrayList<>();
                    List<TurmasDTO> turmasDTOs = new ArrayList<>();
                    List<DisciplinasDTO> disciplinasDTOs = new ArrayList<>();
                    
                    sDTO.setId(simuladoRetorno.getId());
                    sDTO.setNome(simuladoRetorno.getNome());
                    sDTO.setRascunho(simuladoRetorno.isRascunho());
                    sDTO.setStatus(simuladoRetorno.getStatus());
                    sDTO.setDataHoraInicial(simuladoRetorno.getDataHoraInicial());
                    sDTO.setDataHoraFinal(simuladoRetorno.getDataHoraFinal());
                    
                    List<SimuladoCursosEntity> sc = simuladoCursosRepository.findCursoBySimulado(simuladoRetorno.getId());
                    for (SimuladoCursosEntity si : sc) {
                        CursosDTO cursosDTO = new CursosDTO();    
                        cursosDTO.setId(si.getIdCurso());
                        cursosDTO.setNome(si.getNome());
                        cursosDTOs.add(cursosDTO);
                    }
                    sDTO.setCursos(cursosDTOs);
    
    
                    List<SimuladoTurmasEntity> st = simuladoTurmasRepository.findTurmasBySimulado(simuladoRetorno.getId());
                    for (SimuladoTurmasEntity si : st) {
                       TurmasDTO turmasDTO = new  TurmasDTO(); 
                        turmasDTO.setId(si.getIdTurma());
                        turmasDTO.setNome(si.getNome());
                        turmasDTOs.add(turmasDTO);
                    }
                    sDTO.setTurmas(turmasDTOs);
    
    
                    List<SimuladoDisciplinasEntity> sd = simuladoDisciplinasRepository.findDisciplinasBySimulado(simuladoRetorno.getId());
                    for (SimuladoDisciplinasEntity si : sd) {
                        DisciplinasDTO disciplinasDTO = new DisciplinasDTO();    
                        disciplinasDTO.setId(si.getIdDisciplina());
                        disciplinasDTO.setNome(si.getNome());
                        disciplinasDTOs.add(disciplinasDTO);
                    }
                    sDTO.setDisciplinas(disciplinasDTOs);
                  
                    simuladoDTOs.add(sDTO);
                }
            }
            
            return ResponseEntity.ok(simuladoDTOs);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar todos os simulado " + e);
        }
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

  

    @GetMapping(value = "/getSimuladoId/{id}")
    public ResponseEntity<List<SimuladoDTO>> getSimuladoId(@PathVariable("id") Long idSimulado) {
        
        try {
           
            List<QuestaoDTO> questaoDTOs = new ArrayList<>();
            List<SimuladoDTO> simuladoDTOs = new ArrayList<>();

            SimuladoEntity simuladoRetorno = simuladoRepository.getOne(idSimulado);
            
            
            SimuladoDTO sDTO = new SimuladoDTO();
            List<CursosDTO> cursosDTOs = new ArrayList<>();
            List<TurmasDTO> turmasDTOs = new ArrayList<>();
            List<DisciplinasDTO> disciplinasDTOs = new ArrayList<>();
            sDTO.setId(simuladoRetorno.getId());
            sDTO.setNome(simuladoRetorno.getNome());
            sDTO.setRascunho(simuladoRetorno.isRascunho());
            sDTO.setStatus(simuladoRetorno.getStatus());
            sDTO.setDataHoraInicial(simuladoRetorno.getDataHoraInicial());
            sDTO.setDataHoraFinal(simuladoRetorno.getDataHoraFinal());
            
            List<SimuladoCursosEntity> sc = simuladoCursosRepository.findCursoBySimulado(idSimulado);
            for (SimuladoCursosEntity si : sc) {
                CursosDTO cursosDTO = new CursosDTO();    
                cursosDTO.setId(si.getIdCurso());
                cursosDTO.setNome(si.getNome());
                cursosDTO.setIdPeriodoLetivo(si.getIdPeriodoLetivo());
                cursosDTOs.add(cursosDTO);
            }
            sDTO.setCursos(cursosDTOs);
            List<SimuladoTurmasEntity> st = simuladoTurmasRepository.findTurmasBySimulado(idSimulado);
            for (SimuladoTurmasEntity si : st) {
               TurmasDTO turmasDTO = new  TurmasDTO(); 
                turmasDTO.setId(si.getIdTurma());
                turmasDTO.setNome(si.getNome());
                turmasDTO.setIdCurso(si.getIdCurso());
                turmasDTO.setIdPeriodoLetivo(si.getIdPeriodoLetivo());
                turmasDTOs.add(turmasDTO);
            }
            sDTO.setTurmas(turmasDTOs);
            List<SimuladoDisciplinasEntity> sd = simuladoDisciplinasRepository.findDisciplinasBySimulado(idSimulado);
            for (SimuladoDisciplinasEntity si : sd) {
                DisciplinasDTO disciplinasDTO = new DisciplinasDTO();    
                disciplinasDTO.setId(si.getIdDisciplina());
                disciplinasDTO.setNome(si.getNome());
                disciplinasDTO.setIdTurma(si.getIdTurma());
                disciplinasDTO.setIdPeriodoLetivo(si.getIdPeriodoLetivo());
                disciplinasDTOs.add(disciplinasDTO);
            }
            sDTO.setDisciplinas(disciplinasDTOs);
            
                
                
            List<SimuladoQuestoesEntity> sq = simuladoQuestoesRepository.findByQuestoes(idSimulado);
            for(SimuladoQuestoesEntity sim : sq){
                QuestaoEntity questaoEntity = questaoRepository.getOne(sim.getQuestao().getId());
                QuestaoDTO questaoDTO = new QuestaoDTO();
                questaoDTO.setId(questaoEntity.getId());
                questaoDTO.setDescricao(questaoEntity.getDescricao());
                questaoDTO.setEnade(questaoEntity.isEnade());
                questaoDTO.setStatus(questaoEntity.isStatus());
                questaoDTO.setDiscursiva(questaoEntity.isDiscursiva());
                questaoDTO.setFonte(questaoEntity.getFonte());
                questaoDTO.setAno(questaoEntity.getAno());
                questaoDTO.setAlterCorreta(questaoEntity.getAlterCorreta());
                questaoDTO.setImagem(questaoEntity.getImagem());
                
                if(questaoEntity.isDiscursiva() == false){
                    List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(questaoEntity);
                    List<AlternativaDTO> alternativas    = new ArrayList<>();
                    for(AlternativaEntity alter : alternativaEntities){
                        AlternativaDTO alternativaDTO = new AlternativaDTO();
                        alternativaDTO.setId(alter.getId());
                        alternativaDTO.setCorreta(alter.isCorreta());
                        alternativaDTO.setDescricao(alter.getDescricao());
                        alternativas.add(alternativaDTO);
                    }
                    Collections.shuffle(alternativas);
                    questaoDTO.setAlternativas(alternativas);
                }
                questaoDTOs.add(questaoDTO);
            }
            Collections.shuffle(questaoDTOs);
            sDTO.setQuestoes(questaoDTOs);
            simuladoDTOs.add(sDTO);
            
            return ResponseEntity.ok(simuladoDTOs);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o simulado pelo id " + idSimulado+  " \n" + e);
        } 
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/getSimuladoIdAlunoQuestao/{id}/{aluno}")
    public ResponseEntity<List<QuestaoRetornoDTO>> getSimuladoIdAlunoQuestao(@PathVariable("id") Long idSimulado,
                                                                             @PathVariable("aluno") String aluno) {
        try {
            List<QuestaoRetornoDTO> questaoRetornoDTOs = questaoFilterRepository.getQuestao(idSimulado, aluno);

            return ResponseEntity.ok(questaoRetornoDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o simulado pelo id " + idSimulado+  " \n" + e);
        } 
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/getSimuladoIdAluno/{id}")
    public ResponseEntity<List<SimuladoDTO>> getSimuladoIdAluno(@PathVariable("id") Long idSimulado) {
        System.out.println("id do simulado " + idSimulado);
        try {
           
            List<QuestaoDTO> questaoDTOs = new ArrayList<>();
            List<SimuladoDTO> simuladoDTOs = new ArrayList<>();

            SimuladoEntity simuladoRetorno = simuladoRepository.getOne(idSimulado);
            SimuladoDTO sDTO = new SimuladoDTO();
            List<CursosDTO> cursosDTOs = new ArrayList<>();
            List<TurmasDTO> turmasDTOs = new ArrayList<>();
            List<DisciplinasDTO> disciplinasDTOs = new ArrayList<>();
            
            
            sDTO.setId(simuladoRetorno.getId());
            sDTO.setNome(simuladoRetorno.getNome());
            sDTO.setRascunho(simuladoRetorno.isRascunho());
            sDTO.setDataHoraInicial(simuladoRetorno.getDataHoraInicial());
            sDTO.setDataHoraFinal(simuladoRetorno.getDataHoraFinal());


            
            List<SimuladoCursosEntity> sc = simuladoCursosRepository.findCursoBySimuladoAluno(idSimulado);
            for (SimuladoCursosEntity si : sc) {
                CursosDTO cursosDTO = new CursosDTO();    
                cursosDTO.setId(si.getIdCurso());
                cursosDTO.setNome(si.getNome());
                cursosDTO.setIdPeriodoLetivo(si.getIdPeriodoLetivo());
                cursosDTOs.add(cursosDTO);
            }
            sDTO.setCursos(cursosDTOs);
            List<SimuladoTurmasEntity> st = simuladoTurmasRepository.findTurmasBySimulado(idSimulado);
            for (SimuladoTurmasEntity si : st) {
               TurmasDTO turmasDTO = new  TurmasDTO(); 
                turmasDTO.setId(si.getIdTurma());
                turmasDTO.setNome(si.getNome());
                turmasDTO.setIdCurso(si.getIdCurso());
                turmasDTO.setIdPeriodoLetivo(si.getIdPeriodoLetivo());
                turmasDTOs.add(turmasDTO);
            }
            sDTO.setTurmas(turmasDTOs);
            List<SimuladoDisciplinasEntity> sd = simuladoDisciplinasRepository.findDisciplinasBySimulado(idSimulado);
            for (SimuladoDisciplinasEntity si : sd) {
                DisciplinasDTO disciplinasDTO = new DisciplinasDTO();    
                disciplinasDTO.setId(si.getIdDisciplina());
                disciplinasDTO.setNome(si.getNome());
                disciplinasDTO.setIdTurma(si.getIdTurma());
                disciplinasDTO.setIdPeriodoLetivo(si.getIdPeriodoLetivo());
                disciplinasDTOs.add(disciplinasDTO);
            }
            sDTO.setDisciplinas(disciplinasDTOs);
            
                
                
            List<SimuladoQuestoesEntity> sq = simuladoQuestoesRepository.findByQuestoes(idSimulado);
            for(SimuladoQuestoesEntity sim : sq){
                QuestaoEntity questaoEntity = questaoRepository.getOne(sim.getQuestao().getId());
                
                QuestaoDTO questaoDTO = new QuestaoDTO();
                questaoDTO.setId(questaoEntity.getId());
                questaoDTO.setDescricao(questaoEntity.getDescricao());
                questaoDTO.setEnade(questaoEntity.isEnade());
                questaoDTO.setStatus(questaoEntity.isStatus());
                questaoDTO.setDiscursiva(questaoEntity.isDiscursiva());
                questaoDTO.setFonte(questaoEntity.getFonte());
                questaoDTO.setAno(questaoEntity.getAno());
                questaoDTO.setImagem(questaoEntity.getImagem());
                
                
                if(questaoEntity.isDiscursiva() == false){
                    List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(questaoEntity);
                    List<AlternativaDTO> alternativas    = new ArrayList<>();
                    
                    for(AlternativaEntity alter : alternativaEntities){
                        AlternativaDTO alternativaDTO = new AlternativaDTO();
                        alternativaDTO.setId(alter.getId());
                        alternativaDTO.setDescricao(alter.getDescricao());
                        alternativas.add(alternativaDTO);
                    }
                    Collections.shuffle(alternativas);
                    questaoDTO.setAlternativas(alternativas);
                }
                questaoDTOs.add(questaoDTO);

            }
            Collections.shuffle(questaoDTOs);
            sDTO.setQuestoes(questaoDTOs);
            simuladoDTOs.add(sDTO);
            
            return ResponseEntity.ok(simuladoDTOs);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o simulado pelo id " + idSimulado+  " \n" + e);
        } 
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/getSimuladoIdAlunoV2/{id}")
    public ResponseEntity<SimuladoDTO> getSimuladoIdAlunoV2(@PathVariable("id") Long idSimulado) {
        System.out.println("id do simulado " + idSimulado);
        try {
           
            SimuladoEntity simuladoRetorno = simuladoRepository.getOne(idSimulado);
            SimuladoDTO sDTO = new SimuladoDTO();
            List<CursosDTO> cursosDTOs = new ArrayList<>();
            List<TurmasDTO> turmasDTOs = new ArrayList<>();
            List<DisciplinasDTO> disciplinasDTOs = new ArrayList<>();
            
            sDTO.setId(simuladoRetorno.getId());
            sDTO.setNome(simuladoRetorno.getNome());
            sDTO.setRascunho(simuladoRetorno.isRascunho());
            sDTO.setStatus(simuladoRetorno.getStatus());
            sDTO.setDataHoraInicial(simuladoRetorno.getDataHoraInicial());
            sDTO.setDataHoraFinal(simuladoRetorno.getDataHoraFinal());

            
            //simuladoDTOs = simuladoJDBC.getSimuladoID(idSimulado);
            
            
           
            
            return ResponseEntity.ok(sDTO);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar o simulado pelo id " + idSimulado+  " \n" + e);
        } 
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "/updateStatus")
    public ResponseEntity<Map> alterStatus(@RequestBody SimuladoDTO simuladoDTO) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            map = simuladoJDBC.simuladoStatusRascunho(simuladoDTO.getId(), simuladoDTO.isRascunho());
        
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao alterar o status do simulado " + e);
        }
       return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }



    @PostMapping(value = "/statusSimulado")
    public ResponseEntity<Map> statusSimulado(@RequestBody SimuladoDTO simuladoDTO) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
           

            SimuladoEntity simuladoRetorno = simuladoRepository.getOne(simuladoDTO.getId());
            simuladoRetorno.setStatus(simuladoDTO.getStatus());
            simuladoRetorno = simuladoRepository.save(simuladoRetorno);

           
            map.put("success", true);
            map.put("message", "Status alterado com sucesso.");

        return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao alterar o status do simulado " + e);
            map.put("success", false);
            map.put("message", "Erro ao alterar o status");
        }
       return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
    
    public List<SimuladoRetornoDTO> getSimulados(List<Long> idsSimulados ){
        
        try {
            List<QuestaoDTO> questaoDTOs = new ArrayList<>();
            List<SimuladoRetornoDTO> simuladoDTOs = new ArrayList<>();
            for(Long s : idsSimulados){
                SimuladoEntity simuladoRetorno = simuladoRepository.getOne(s);
                SimuladoRetornoDTO sDTO = new SimuladoRetornoDTO();

                
                
                sDTO.setId(simuladoRetorno.getId());
                sDTO.setNome(simuladoRetorno.getNome());
                sDTO.setRascunho(simuladoRetorno.isRascunho());
                //sDTO.setStatus();
                sDTO.setDataHoraInicial(simuladoRetorno.getDataHoraInicial());
                sDTO.setDataHoraFinal(simuladoRetorno.getDataHoraFinal());
                
                List<SimuladoQuestoesEntity> sq = simuladoQuestoesRepository.findByQuestoes(s);

                for(SimuladoQuestoesEntity sim : sq){
                    QuestaoEntity questaoEntity = questaoRepository.getOne(sim.getQuestao().getId());
                    QuestaoDTO questaoDTO = new QuestaoDTO();

                    questaoDTO.setId(questaoEntity.getId());
                    questaoDTO.setDescricao(questaoEntity.getDescricao());
                    questaoDTO.setEnade(questaoEntity.isEnade());
                    questaoDTO.setStatus(questaoEntity.isStatus());
                    questaoDTO.setDiscursiva(questaoEntity.isDiscursiva());
                    questaoDTO.setFonte(questaoEntity.getFonte());
                    questaoDTO.setAno(questaoEntity.getAno());
                    questaoDTO.setAlterCorreta(questaoEntity.getAlterCorreta());
                    questaoDTO.setImagem(questaoEntity.getImagem());

                    if(questaoEntity.isDiscursiva() == false){
                        List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(questaoEntity);
                        List<AlternativaDTO> alternativas    = new ArrayList<>();
                        for(AlternativaEntity alter : alternativaEntities){
                            AlternativaDTO alternativaDTO = new AlternativaDTO();
                            alternativaDTO.setId(alter.getId());
                            alternativaDTO.setCorreta(alter.isCorreta());
                            alternativaDTO.setDescricao(alter.getDescricao());
                            alternativas.add(alternativaDTO);
                        }
                        questaoDTO.setAlternativas(alternativas);
                    }

                    questaoDTOs.add(questaoDTO);

                }
                sDTO.setQuestoes(questaoDTOs);
                simuladoDTOs.add(sDTO);
            }

            return simuladoDTOs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar os simulados " + e);
        }
        return null;
    }


    @PostMapping(value = "/simuladoResposta")
    public ResponseEntity<SimuladoResolucaoDTO> simuladoResposta(@RequestBody SimuladoResolucaoDTO simulado) {
        try {

            SimuladoResolucaoEntity simuladoResolucaoEntity = new SimuladoResolucaoEntity();

            simuladoResolucaoEntity.setId(simulado.getId());
            simuladoResolucaoEntity.setIdQuestao(simulado.getIdQuestao());
            simuladoResolucaoEntity.setIdAlternativa(simulado.getIdAlternativa());
            simuladoResolucaoEntity.setIdSimulado(simulado.getIdSimulado());
            simuladoResolucaoEntity.setIdAluno(simulado.getIdUltilizador());
            
            
            int delete = simuladoFilterRepositoty.simuladoResponse(simulado.getIdSimulado(), simulado.getIdQuestao(), simulado.getIdUltilizador(), simulado.getIdAlternativa());
            //simuladoResolucaoEntity = simuladoResolucaoRepository.save(simuladoResolucaoEntity);
            //simulado.setId(simuladoResolucaoEntity.getId());

            System.out.println("retorno do update "+ delete);
            return ResponseEntity.ok(simulado);
            
        } catch (Exception e) {
           System.out.println("Erro ao salvar a alternativa respondida " + e);
        }
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    


    @GetMapping(value = "/deleteSimulado/{id}")
    public ResponseEntity<Map> deleteSimulado(@PathVariable("id") Long idSimulado) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {

            if(simuladoJDBC.deleteSimulado(idSimulado)){
                map.put("success", true);
                map.put("message", "SIMULADO DELETADO COM SUCESSO");
            }else{
                map.put("success", false);
                map.put("message", "Não foi possível efetuar a exclusão. Motivo: O Simulado possui vínculo com Aluno.");
            }
            
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "Erro ao tentar Excluir o simulado");
        }
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

}