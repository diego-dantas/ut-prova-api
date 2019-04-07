package br.toledo.UTProva.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.toledo.UTProva.model.dao.entity.SimuladoCursosEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoDisciplinasEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoTurmasEntity;
import br.toledo.UTProva.model.dao.repository.SimuladoCursosRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoDisciplinasRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoStatusRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoTurmasRepository;
import br.toledo.UTProva.model.dao.repository.UsuarioRepository;
import br.toledo.UTProva.model.dao.serviceJDBC.AlunoJDBC;
import br.toledo.UTProva.model.dao.serviceJDBC.SimuladoJDBC;
import br.toledo.UTProva.model.dto.ContextoDTO;
import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.DisciplinasDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;
import br.toledo.UTProva.model.dto.TurmasDTO;
import br.toledo.UTProva.model.dto.UsuarioDTO;
import br.toledo.UTProva.service.Gateway;
import br.toledo.UTProva.service.ServiceGet;
import br.toledo.UTProva.service.ServicePost;
import br.toledo.UTProva.service.academico.AlunoService;
import br.toledo.UTProva.service.academico.CoordenadoresService;
import br.toledo.UTProva.service.academico.LogoutService;
import br.toledo.UTProva.service.academico.ProfessoresService;
import br.toledo.UTProva.service.sistema.Contexto;
import br.toledo.UTProva.service.sistema.Login;;

@RestController
@RequestMapping(value = "/api")
public class UserCTL {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private SimuladoDisciplinasRepository simuladoDisciplinasRepository;
    @Autowired
    private SimuladoCursosRepository simuladoCursosRepository;
    @Autowired
    private SimuladoTurmasRepository simuladoTurmasRepository;
    @Autowired
    private SimuladoRepository simuladoRepository;
    @Autowired
    private SimuladoStatusRepository simuladoStatusRepository;
    @Autowired
    private SimuladoJDBC simuladoJDBC;
    @Autowired
    private AlunoJDBC alunoJDBC;

    /**
     * Serviço de login do usuario consumindo o servido da toledo 
     */
    @PostMapping(value = "/login/user")
    public ResponseEntity loginUser(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            System.out.println("USUARIO " + usuarioDTO.getUsuario() + "\n SENHA " + usuarioDTO.getSenha());
            return Login.login(usuarioDTO.getUsuario(), usuarioDTO.getSenha());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Busca usauario pelo nome 
     */
    @GetMapping(value = "/getUser")
    public ResponseEntity getUser(@RequestHeader(value = "Authorization") String acessToken, @RequestParam String user) {
        try {
            System.out.println("token que estou recebendo " + acessToken);
            return ServiceGet.getUser(acessToken, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Busca o contexto do usuario no serviço da toledo 
     */
    @PostMapping(value = "/contexto")
    public ResponseEntity getContexto(@RequestHeader(value = "Authorization") String acessToken, @RequestBody UsuarioDTO usuarioDTO) {
        try {

            return Contexto.getContexto(acessToken, usuarioDTO.getId().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * metodo de logout de usuario que consome servido de logout da toledo
     */
    @PostMapping(value = "/logout")
    public ResponseEntity logout(@RequestHeader(value = "Authorization") String acessToken) {
        try {

            return ResponseEntity.ok(LogoutService.logout(acessToken));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Metodo de busca os dados iniciais do usuario conforme o contexto da pessoa
     */
    @PostMapping(value = "/getData")
    public ResponseEntity<Map> getData(@RequestHeader(value = "Authorization") String acessToken,
                                       @RequestBody ContextoDTO contextoDTO) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Gateway gateway = ServicePost.revalidToken(acessToken, contextoDTO);

            if (contextoDTO.getTipo().equals("COORDENADOR")) {
                CoordenadoresService coordenadoresService = new CoordenadoresService();

                map.put("cursos",
                        coordenadoresService.getCurso(gateway.getToken(), contextoDTO.getIdPeriodoLetivo()));
                map.put("turmas",
                        coordenadoresService.getTurmas(gateway.getToken(), contextoDTO.getIdPeriodoLetivo()));
                map.put("disciplinas", coordenadoresService.getDisciplinas(gateway.getToken(),
                        contextoDTO.getIdPeriodoLetivo()));

            } else if (contextoDTO.getTipo().equals("PROFESSOR")) {

                
                ProfessoresService professoresSevice = new ProfessoresService();

                ArrayList<String> curso = new ArrayList<>();
                ArrayList<String> turma = new ArrayList<>();
                map.put("cursos", curso);
                map.put("turmas", turma);
                map.put("disciplinas", professoresSevice.getDisciplinas(gateway.getToken()));

            }else if(contextoDTO.getTipo().equals("ALUNO")){

                AlunoService alunoService = new AlunoService();
                
                List<Long> simulados = new ArrayList<>();
                simulados.addAll(getAllSimuladoAluno(gateway.getToken(), contextoDTO.getIdUtilizador()));
                
                if(simulados.size() > 0){
                    map.put("simulados", getSimuladosAlunos(contextoDTO.getIdUtilizador(), simulados));
                    map.put("dash_aluno", alunoJDBC.getDashAluno(simulados, contextoDTO.getIdUtilizador()));
                }else{
                    map.put("simulados", simulados);
                }
                
            }
            
            return new ResponseEntity<>(map, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    /**
     * Busca todos so simulados do aluno do contexto 
     */
    public List<Long> getAllSimuladoAluno(String token, String idAluno){

        try {
            List<SimuladoDTO>               simuladoDTOs        = new ArrayList<>();
            List<SimuladoDisciplinasEntity> simuladoDisciplinas = new ArrayList<>();
            List<SimuladoTurmasEntity>      simuladoTurmas      = new ArrayList<>();
            List<SimuladoCursosEntity>      simuladoCursos      = new ArrayList<>();
            List<String> idsDisciplinas = new ArrayList<>();
            List<String> idsTurmas      = new ArrayList<>();
            List<String> idsCursos      = new ArrayList<>();
            List<Long>   idsSimulados   = new ArrayList<>();

            
            

            AlunoService alunoService = new AlunoService();
            List<DisciplinasDTO> disciplinasDTO = alunoService.getDisciplinas(token);
            CursosDTO cursosDTOs = alunoService.getCursos(token);

            //popula os id de disciplinas do aluno
            disciplinasDTO.forEach(n -> idsDisciplinas.add(n.getId()));
            simuladoDisciplinas = simuladoDisciplinasRepository.findByDisciplinasAndPeriodo(disciplinasDTO.get(0).getIdPeriodoLetivo(), idsDisciplinas);
            simuladoDisciplinas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));

            //popula os id de turmas do aluno
            disciplinasDTO.forEach(n -> idsTurmas.add(n.getIdTurma()));
            simuladoTurmas = simuladoTurmasRepository.findByTurmasAndPeriodo(disciplinasDTO.get(0).getIdPeriodoLetivo(), idsTurmas);
            simuladoTurmas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));

            //popula os id de curso do aluno
            idsCursos.add(cursosDTOs.getId());
            simuladoCursos = simuladoCursosRepository.findByCursoAndPeriodo(disciplinasDTO.get(0).getIdPeriodoLetivo(), idsCursos);
            simuladoCursos.forEach(n -> idsSimulados.add(n.getSimulado().getId()));

        

            
            
            return idsSimulados;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar os simulados do aluno " + e);
        }
        return null;
    }

    public List<SimuladoDTO> getSimuladosAlunos(String idAluno, List<Long> idsSimulados){
        
        List<SimuladoDTO>               simuladoDTOs        = new ArrayList<>();

        List<SimuladoEntity> simulado =  simuladoJDBC.getSimuladosID(idsSimulados, idAluno);
                
        for(SimuladoEntity simuladosEntity : simulado){
            SimuladoDTO simuladoDTO = new SimuladoDTO();
            simuladoDTO.setId(simuladosEntity.getId());
            simuladoDTO.setNome(simuladosEntity.getNome());
            simuladoDTO.setStatus(simuladosEntity.getStatus());
            simuladoDTO.setDataHoraInicial(simuladosEntity.getDataHoraInicial());
            simuladoDTO.setDataHoraFinal(simuladosEntity.getDataHoraFinal());
            simuladoDTO.setRascunho(simuladosEntity.isRascunho());
            
            simuladoDTOs.add(simuladoDTO);

        }

        return simuladoDTOs;
    }

    public List<SimuladoDTO> getQuestoes(List<Long> ids) {
        
        List<SimuladoDTO> simuladoDTOs = new ArrayList<>();

        for(Long s : ids){
            SimuladoEntity simuladoRetorno = simuladoRepository.findByAluno(s);
            if(simuladoRetorno != null){
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
           
        }

        return simuladoDTOs;
    }
}