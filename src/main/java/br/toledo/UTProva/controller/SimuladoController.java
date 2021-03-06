package br.toledo.UTProva.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.AlternativaEntity;
import br.toledo.UTProva.model.dao.entity.FonteEntity;
import br.toledo.UTProva.model.dao.entity.QuestaoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoCursosEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoDisciplinasEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoQuestoesEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoResolucaoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoTurmasEntity;
import br.toledo.UTProva.model.dao.entity.TipoRespostaEntity;
import br.toledo.UTProva.model.dao.repository.AlternativaRepository;
import br.toledo.UTProva.model.dao.repository.FonteRepository;
import br.toledo.UTProva.model.dao.repository.QuestaoFilterRepository;
import br.toledo.UTProva.model.dao.repository.QuestaoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoCursosRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoDisciplinasRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoFilterRepositoty;
import br.toledo.UTProva.model.dao.repository.SimuladoQuestoesRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoTurmasRepository;
import br.toledo.UTProva.model.dao.repository.TipoRespostaRepository;
import br.toledo.UTProva.model.dao.serviceJDBC.AlunoJDBC;
import br.toledo.UTProva.model.dao.serviceJDBC.SimuladoJDBC;
import br.toledo.UTProva.model.dto.AlternativaDTO;
import br.toledo.UTProva.model.dto.AlternativaRetornoDTO;
import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.DisciplinasDTO;
import br.toledo.UTProva.model.dto.FonteDTO;
import br.toledo.UTProva.model.dto.QuestaoDTO;
import br.toledo.UTProva.model.dto.QuestaoRetornoDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;
import br.toledo.UTProva.model.dto.SimuladoResolucaoDTO;
import br.toledo.UTProva.model.dto.SimuladoRetornoDTO;
import br.toledo.UTProva.model.dto.TipoRespostaDTO;
import br.toledo.UTProva.model.dto.TurmasDTO;

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
    private QuestaoFilterRepository questaoFilterRepository;
    @Autowired
    private SimuladoJDBC simuladoJDBC;
    @Autowired
    private FonteRepository fonteRepository;
    @Autowired
    private TipoRespostaRepository tipoRespostaRepository;
    @Autowired
    private AlunoJDBC alunoJDBC;



    private static String UPLOAD_DIR = System.getProperty("user.dir");
    public static String getUPLOAD_DIR() {
        return UPLOAD_DIR;
    }


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
            simulado.setEnade(simuladoDTO.isEnade());
            simulado.setStatus(simuladoDTO.getStatus());
            simulado.setContent(simuladoDTO.isContent());
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
            return ResponseEntity.ok(simuladoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
                    sDTO.setEnade(simuladoRetorno.isEnade());
                    sDTO.setContent(simuladoRetorno.isContent());
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getAllSimuladoEnade")
    public ResponseEntity<List<SimuladoDTO>> getAllSimuladoEnade(@RequestBody SimuladoDTO simuladoDTO) {

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
                List<SimuladoEntity> simuladosRetorno = simuladoJDBC.findSimuladosEnade(idsSimulados);
                for(SimuladoEntity  simuladoRetorno : simuladosRetorno){
                    
                    
                    SimuladoDTO sDTO = new SimuladoDTO();
                    List<CursosDTO> cursosDTOs = new ArrayList<>();
                    List<TurmasDTO> turmasDTOs = new ArrayList<>();
                    List<DisciplinasDTO> disciplinasDTOs = new ArrayList<>();
                    
                    sDTO.setId(simuladoRetorno.getId());
                    sDTO.setNome(simuladoRetorno.getNome());
                    sDTO.setRascunho(simuladoRetorno.isRascunho());
                    sDTO.setEnade(simuladoRetorno.isEnade());
                    sDTO.setContent(simuladoRetorno.isContent());
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
            sDTO.setEnade(simuladoRetorno.isEnade());
            sDTO.setContent(simuladoRetorno.isContent());
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
                FonteEntity fonteEntity = fonteRepository.getOne(questaoEntity.getFonte().getId());
                TipoRespostaEntity tipoRespostaEntity = tipoRespostaRepository.getOne(questaoEntity.getTipoResposta().getId());

                QuestaoDTO questaoDTO = new QuestaoDTO();

                TipoRespostaDTO tipoResposta = new TipoRespostaDTO();
                tipoResposta.setId(tipoRespostaEntity.getId());
                tipoResposta.setDescricao(tipoRespostaEntity.getDescricao());
                questaoDTO.setTipoResposta(tipoResposta);
                
                FonteDTO fonte = new FonteDTO();
                fonte.setId(fonteEntity.getId());
                fonte.setDescription(fonteEntity.getDescription());
                fonte.setStatus(fonteEntity.isStatus());
                questaoDTO.setFonte(fonte);

                questaoDTO.setId(questaoEntity.getId());
                questaoDTO.setDescricao(questaoEntity.getDescricao());
                questaoDTO.setStatus(questaoEntity.isStatus());
                questaoDTO.setAno(questaoEntity.getAno());
                questaoDTO.setAlterCorreta(questaoEntity.getAlterCorreta());
                questaoDTO.setImagem(questaoEntity.getImagem());
                
                
                if(questaoEntity.getTipoResposta().getId() != 2){
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }


    @GetMapping(value = "/getSimuladoIdAlunoQuestao/{id}/{aluno}/{nomeAluno}")
    public ResponseEntity<List<QuestaoRetornoDTO>> getSimuladoIdAlunoQuestao(@PathVariable("id") Long idSimulado,
                                                                             @PathVariable("aluno") String aluno,
                                                                             @PathVariable("nomeAluno") String nomeAluno) {
        try {
            List<QuestaoRetornoDTO> questaoRetornoDTOs = questaoFilterRepository.getQuestao(idSimulado, aluno, nomeAluno);

            return ResponseEntity.ok(questaoRetornoDTOs);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/getSimuladoIdAluno/{id}")
    public ResponseEntity<List<SimuladoDTO>> getSimuladoIdAluno(@PathVariable("id") Long idSimulado) {
        
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
            sDTO.setEnade(simuladoRetorno.isEnade());
            sDTO.setContent(simuladoRetorno.isContent());
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

                FonteEntity fonteEntity = fonteRepository.getOne(questaoEntity.getFonte().getId());
                TipoRespostaEntity tipoRespostaEntity = tipoRespostaRepository.getOne(questaoEntity.getTipoResposta().getId());

                QuestaoDTO questaoDTO = new QuestaoDTO();

                TipoRespostaDTO tipoResposta = new TipoRespostaDTO();
                tipoResposta.setId(tipoRespostaEntity.getId());
                tipoResposta.setDescricao(tipoRespostaEntity.getDescricao());
                questaoDTO.setTipoResposta(tipoResposta);
                
                FonteDTO fonte = new FonteDTO();
                fonte.setId(fonteEntity.getId());
                fonte.setDescription(fonteEntity.getDescription());
                fonte.setStatus(fonteEntity.isStatus());
                questaoDTO.setFonte(fonte);

                questaoDTO.setId(questaoEntity.getId());
                questaoDTO.setDescricao(questaoEntity.getDescricao());
                questaoDTO.setStatus(questaoEntity.isStatus());
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }


    @GetMapping(value = "/finalizaSimulado/{id}/{aluno}")
    public ResponseEntity<Map> finalizaSimulado(@PathVariable("id") Long idSimulado,
                                                @PathVariable("aluno") String aluno) {
            Map<String, Object> map = new HashMap<String, Object>();
        try {

            int retorno = simuladoJDBC.finalizaSimulado(idSimulado, aluno);
            if(retorno > 0){

                map.put("success", true);
                map.put("message", "Simulado finalizado.");
            }

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "Erro ao finalizar o simulado.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } 
    }

    @GetMapping(value = "/getResult/{id}/{aluno}")
    public ResponseEntity<Map> getResult(@PathVariable("id") Long idSimulado,
                                                @PathVariable("aluno") String aluno) {
            Map<String, Object> map = new HashMap<String, Object>();
        try {
        
            return new ResponseEntity<>(alunoJDBC.getResultAluno(idSimulado, aluno), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "Erro ao buscar o resultado do simulado.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        } 
    }

    /**
     * Metodo para realizar a alteração do status de rascunho para true ou false
     */
    @PostMapping(value = "/updateStatus")
    public ResponseEntity<Map> alterStatus(@RequestBody SimuladoDTO simuladoDTO) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            map = simuladoJDBC.simuladoStatusRascunho(simuladoDTO.getId(), simuladoDTO.isRascunho());
        
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
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
            map.put("success", false);
            map.put("message", "Erro ao alterar o status");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }
    
    
    public ResponseEntity<List<SimuladoRetornoDTO>> getSimulados(List<Long> idsSimulados ){
        
        try {
            List<QuestaoDTO> questaoDTOs = new ArrayList<>();
            List<SimuladoRetornoDTO> simuladoDTOs = new ArrayList<>();
            for(Long s : idsSimulados){
                SimuladoEntity simuladoRetorno = simuladoRepository.getOne(s);
                SimuladoRetornoDTO sDTO = new SimuladoRetornoDTO();
                
                sDTO.setId(simuladoRetorno.getId());
                sDTO.setNome(simuladoRetorno.getNome());
                sDTO.setRascunho(simuladoRetorno.isRascunho());
                sDTO.setEnade(simuladoRetorno.isEnade());
                sDTO.setContent(simuladoRetorno.isContent());
                sDTO.setDataHoraInicial(simuladoRetorno.getDataHoraInicial());
                sDTO.setDataHoraFinal(simuladoRetorno.getDataHoraFinal());
                
                List<SimuladoQuestoesEntity> sq = simuladoQuestoesRepository.findByQuestoes(s);

                for(SimuladoQuestoesEntity sim : sq){
                    QuestaoEntity questaoEntity = questaoRepository.getOne(sim.getQuestao().getId());
                    FonteEntity fonteEntity = fonteRepository.getOne(questaoEntity.getFonte().getId());
                    TipoRespostaEntity tipoRespostaEntity = tipoRespostaRepository.getOne(questaoEntity.getTipoResposta().getId());

                    QuestaoDTO questaoDTO = new QuestaoDTO();

                    TipoRespostaDTO tipoResposta = new TipoRespostaDTO();
                    tipoResposta.setId(tipoRespostaEntity.getId());
                    tipoResposta.setDescricao(tipoRespostaEntity.getDescricao());
                    questaoDTO.setTipoResposta(tipoResposta);
                    
                    FonteDTO fonte = new FonteDTO();
                    fonte.setId(fonteEntity.getId());
                    fonte.setDescription(fonteEntity.getDescription());
                    fonte.setStatus(fonteEntity.isStatus());
                    questaoDTO.setFonte(fonte);

                    questaoDTO.setId(questaoEntity.getId());
                    questaoDTO.setDescricao(questaoEntity.getDescricao());
                    questaoDTO.setStatus(questaoEntity.isStatus());
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

            return new ResponseEntity<>(simuladoDTOs, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
            
            return ResponseEntity.ok(simulado);
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping(value = "/getSimuladosQuestoesDiscursivas")
    public ResponseEntity<List<Map<String, Object>>> getSimuladosQuestoesDiscursivas(@RequestBody SimuladoDTO simuladoDTO){
        try {
            List<String> idsDisciplinas = new ArrayList<>();
            List<String> idsTurmas      = new ArrayList<>();
            List<String> idsCursos      = new ArrayList<>();
            List<Long>   idsSimulados   = new ArrayList<>();
            int peridoLetivo = 0;
            
            
            
            if(simuladoDTO.getCursos() != null && simuladoDTO.getCursos().size() > 0){
                simuladoDTO.getCursos().forEach(n -> idsCursos.add(n.getId()));
                peridoLetivo = simuladoDTO.getCursos().get(0).getIdPeriodoLetivo();
            }
                
            if(simuladoDTO.getTurmas() != null && simuladoDTO.getTurmas().size() > 0){
                simuladoDTO.getTurmas().forEach(n -> idsTurmas.add(n.getId()));
                peridoLetivo = simuladoDTO.getTurmas().get(0).getIdPeriodoLetivo();  
            }
                 
            if(simuladoDTO.getDisciplinas() != null && simuladoDTO.getDisciplinas().size() > 0){
                simuladoDTO.getDisciplinas().forEach(n -> idsDisciplinas.add(n.getId()));
                peridoLetivo = simuladoDTO.getDisciplinas().get(0).getIdPeriodoLetivo();  
            }
                  
            // List<SimuladoDTO> simuladosDTOs = simuladoJDBC.getSimuladosQuestoesDiscursivas();
            List<Map<String, Object>> simuladosDTOs = simuladoJDBC.getSimuladosQuestoesDiscursivas(peridoLetivo, idsCursos, idsTurmas, idsDisciplinas);
            return new ResponseEntity<>(simuladosDTOs, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/getDashAdmin")
    public ResponseEntity<Map<String, Object>> getAllSigetDashAdminmulado(@RequestBody SimuladoDTO simuladoDTO) {
        
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
               return new ResponseEntity<>(simuladoJDBC.getDashAdmin(idsSimulados), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/getGestor")
    public ResponseEntity<List<SimuladoDTO>> getGestor(@RequestBody SimuladoDTO simuladoDTO) {
        
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
               return new ResponseEntity<>(simuladoJDBC.getGestor(idsSimulados), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/acompanhamentoSimulado/{idSimulado}")
    public ResponseEntity<Map<String, Object>> reports1(@PathVariable("idSimulado") Long idSimulado){
        try {
            return new ResponseEntity<>(simuladoJDBC.acompanhamentoSimulado(idSimulado), HttpStatus.OK);    
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
    }



    @GetMapping(value = "/print/simulado/{idSimulado}/{tipo}")
    public void printSimulado(@PathVariable("idSimulado") Long idSimulado, 
                              @PathVariable("tipo") int tipo,
                              HttpSession session,
                              HttpServletResponse response)
                throws DocumentException, MalformedURLException, IOException {

        System.out.println("id " + idSimulado);
        
        try {

            SimuladoEntity simulado = simuladoRepository.getOne(idSimulado);
            if(simulado.getNome().isEmpty() == false){
                String name = simulado.getNome().replace(" ", "_");
                name = name.replace("/", "_");
                name = name.replace("\\", "_") + ".pdf";
                File directory = new File(getUPLOAD_DIR() + "/reports/");
    
                if (!directory.exists()) {
                    directory.mkdir();
                }
                String source = getUPLOAD_DIR() + "/reports/" + "" + name;
    
                
                Image imgSoc = Image.getInstance(getUPLOAD_DIR() + "/imgs/unitoledo.png");
                imgSoc.scaleToFit(100,100);
    
                Document document = new Document(PageSize.A4);
        
                PdfWriter writer =  PdfWriter.getInstance(document, new FileOutputStream(source));
                document.open();
                Font f = new Font(Font.FontFamily.COURIER, 15, Font.BOLD);
                
                Paragraph p1 = new Paragraph();            
                p1.add(imgSoc);
                Paragraph p2 = new Paragraph(simulado.getNome(), f);
                p2.setAlignment(Element.ALIGN_CENTER);
                document.add(p1);
                document.add(p2);            
                
                List<QuestaoRetornoDTO> questaoRetornoDTOs = questaoFilterRepository.getQuestaoAll(idSimulado, tipo);
                Integer qt = 1;
                for(QuestaoRetornoDTO q : questaoRetornoDTOs) {
                    System.out.println("id da questão " + q.getId());
                    
                    Paragraph p = new Paragraph("Questão " + String.valueOf(qt++), f);
                    p.setAlignment(Element.ALIGN_LEFT);
                    document.add(p);
                    StringBuilder htmlBuilder = new StringBuilder();
                    htmlBuilder.append(new String("<hr/><br/>")); 
                    
                    // String castDesc = q.getDescricao().replace("\"", "'");
    
                    
                    // if(q.getDescricao().contains("float:right;")){
                    //     castDesc = castDesc.replace("style='text-align: center", "style='text-align: right'");                    
                    // } else if(q.getDescricao().contains("float:left;")) {
                    //     castDesc = castDesc.replace("style='text-align: center'", "style='text-align: left'");  
                    // }
    
                    String[] descQuestao = castStringHtml(q.getDescricao());
                    for (int i = 0; i < descQuestao.length; i++) {                        
                        htmlBuilder.append(new String(descQuestao[i])); 
                    }
                    
                    if(q.getTipoResposta() == 2){
                        htmlBuilder.append(new String("<br/><br/><br/>"));
                        for(int i = 0; i < 15; i++){                            
                            htmlBuilder.append(new String("<hr/>")); 
                        }
                        htmlBuilder.append(new String("<br/><br/><br/>"));
                    }else{
                        String[] alter = {"A", "B", "C", "D", "E"};
                        int at = 0;
                        htmlBuilder.append(new String("<br/><br/><p>Alternativas </p><br/>")); 
    
                        for(AlternativaRetornoDTO a : q.getAlternativas()){
                            htmlBuilder.append(new String("<p>" + String.valueOf(alter[at++]) + ")</p>")); 
    
                            // String castAlterDesc = a.getDescricao().replace("\"", "'");
                            // String[] teste = a.getDescricao().split("><");
                            // System.out.println(castAlterDesc+"\n\n\n");
                            // if(a.getDescricao().contains("float:right;")){
                            //     castAlterDesc = castAlterDesc.replace("style='text-align: center;", "style='text-align: right;");                    
                            // }else if(a.getDescricao().contains("float:left;")) {
                            //     castAlterDesc = castAlterDesc.replace("style='text-align: center;", "style='text-align: left;");  
                            // }
                            //htmlBuilder.append(new String(castAlterDesc)); 
    
                            String[] descAlter = castStringHtml(a.getDescricao());
                            for (int i = 0; i < descAlter.length; i++) {
                                
                                htmlBuilder.append(new String("<p>"+descAlter[i])+"</p><br/>"); 
                            }
    
    
                        }
                    }
                    
                    htmlBuilder.append(new String("<br/><br/><br/>"));                
                    
    
                    InputStream is = new ByteArrayInputStream(htmlBuilder.toString().getBytes());
                    is = new ByteArrayInputStream(htmlBuilder.toString().getBytes());
                    XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
    
         
                }            
            
                document.close();
                
                String filePathToBeServed = source;
                File fileToDownload = new File(filePathToBeServed);
    
                InputStream inputStream = new FileInputStream(fileToDownload);
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment; filename="+name); 
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
                inputStream.close();
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/getSimuladoIdPrint/{id}")
    public ResponseEntity<List<SimuladoDTO>> getSimuladoIdPrint(@PathVariable("id") Long idSimulado) {
        
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
            sDTO.setEnade(simuladoRetorno.isEnade());
            sDTO.setContent(simuladoRetorno.isContent());
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
                FonteEntity fonteEntity = fonteRepository.getOne(questaoEntity.getFonte().getId());
                TipoRespostaEntity tipoRespostaEntity = tipoRespostaRepository.getOne(questaoEntity.getTipoResposta().getId());

                QuestaoDTO questaoDTO = new QuestaoDTO();

                TipoRespostaDTO tipoResposta = new TipoRespostaDTO();
                tipoResposta.setId(tipoRespostaEntity.getId());
                tipoResposta.setDescricao(tipoRespostaEntity.getDescricao());
                questaoDTO.setTipoResposta(tipoResposta);
                
                FonteDTO fonte = new FonteDTO();
                fonte.setId(fonteEntity.getId());
                fonte.setDescription(fonteEntity.getDescription());
                fonte.setStatus(fonteEntity.isStatus());
                questaoDTO.setFonte(fonte);

                questaoDTO.setId(questaoEntity.getId());
                questaoDTO.setDescricao(questaoEntity.getDescricao());
                questaoDTO.setStatus(questaoEntity.isStatus());
                questaoDTO.setAno(questaoEntity.getAno());
                questaoDTO.setAlterCorreta(questaoEntity.getAlterCorreta());
                questaoDTO.setImagem(questaoEntity.getImagem());
                
                
                if(questaoEntity.getTipoResposta().getId() != 2){
                    List<AlternativaEntity> alternativaEntities = alternativaRepository.findByQuestao(questaoEntity);
                    List<AlternativaDTO> alternativas    = new ArrayList<>();
                    for(AlternativaEntity alter : alternativaEntities){
                        AlternativaDTO alternativaDTO = new AlternativaDTO();
                        alternativaDTO.setId(alter.getId());
                        alternativaDTO.setCorreta(alter.isCorreta());
                        alternativaDTO.setDescricao(alter.getDescricao());
                        alternativas.add(alternativaDTO);
                    }
                    //Collections.shuffle(alternativas);
                    questaoDTO.setAlternativas(alternativas);
                }
                questaoDTOs.add(questaoDTO);
            }
            //Collections.shuffle(questaoDTOs);
            sDTO.setQuestoes(questaoDTOs);
            simuladoDTOs.add(sDTO);
            
            return ResponseEntity.ok(simuladoDTOs);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }

    public String[] castStringHtml(String html){
        try {
            
            html = html.replace("\"", "'");
            html = html.replace("<br>", "<br/>");
            html = html.replace("><img", ">#<img");
            html = html.replace("/></", "/>#</");
            html = html.replace("><", ">,<");
            String[] htmlArray = html.split(",");
            
            for(int i = 0; i < htmlArray.length; i++){                
                if(htmlArray[i].contains("float:right;")){
                    htmlArray[i] = htmlArray[i].replace("style='text-align: center", "style='text-align: right");                    
                } else if(htmlArray[i].contains("float:left;")) {
                    htmlArray[i] = htmlArray[i].replace("style='text-align: center", "style='text-align: left");  
                }
                htmlArray[i] = htmlArray[i].replace(">#<", "><");             
            }
            return htmlArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
       
    }

}