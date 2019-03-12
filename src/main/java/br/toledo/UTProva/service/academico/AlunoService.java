package br.toledo.UTProva.service.academico;

import br.toledo.UTProva.model.dao.entity.SimuladoCursosEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoDisciplinasEntity;
import br.toledo.UTProva.model.dao.entity.SimuladoTurmasEntity;
import br.toledo.UTProva.model.dao.repository.SimuladoCursosRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoDisciplinasRepository;
import br.toledo.UTProva.model.dao.repository.SimuladoTurmasRepository;
import br.toledo.UTProva.model.dto.CursosDTO;
import br.toledo.UTProva.model.dto.DisciplinasDTO;
import br.toledo.UTProva.model.dto.SimuladoDTO;
import br.toledo.UTProva.model.dto.TurmasDTO;
import br.toledo.UTProva.service.Endpoint;
import br.toledo.UTProva.service.Gateway;
import br.toledo.UTProva.service.simulado.SimuladoService;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/api")
public class AlunoService {

    @Autowired
    private SimuladoCursosRepository simuladoCursosRepository;
    @Autowired
    private SimuladoTurmasRepository simuladoTurmasRepository;
    @Autowired
    private SimuladoDisciplinasRepository simuladoDisciplinasRepository;

    private static final String USER_AGENT        = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0";
    private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";


    public List<DisciplinasDTO> getDisciplinas(String token){
        List<DisciplinasDTO> retorno = new ArrayList<>();
        System.out.println("RECUPERANDO AS DISCIPLICAS DE ALUNO");
        try {

            HttpHeaders headers = Gateway.createHeaders(token);
           

            HttpEntity<DisciplinasDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<List<DisciplinasDTO>> response = restTemplate.exchange(
                Endpoint.SERVICE_TOLEDO + Endpoint.ACADEMICO_ALUNO_DISCIPLINAS, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<DisciplinasDTO>>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA AS DISCIPLICAS DO ALUNO " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public CursosDTO getCursos(String token){
        CursosDTO retorno = new CursosDTO();
        System.out.println("RECUPERANDO OS CURSOS DE ALUNO");
        try {

            HttpHeaders headers = Gateway.createHeaders(token);

            HttpEntity<CursosDTO> requestEntity = new HttpEntity<>(null, headers);
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
           
            ResponseEntity<CursosDTO> response = restTemplate.exchange(
                Endpoint.SERVICE_TOLEDO + Endpoint.ACADEMICO_ALUNO_CURSOS, 
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<CursosDTO>(){});

            int responseCode = response.getStatusCodeValue();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                retorno = response.getBody();    
            } else {
                System.out.println("ERROR NA HORA DE PEGA OS CURSOS DO ALUNO " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public List<SimuladoDTO> getAllSimuladoAluno(String token){

        try {
            List<SimuladoDisciplinasEntity> simuladoDisciplinas = new ArrayList<>();
            List<SimuladoTurmasEntity>      simuladoTurmas      = new ArrayList<>();
            List<SimuladoCursosEntity>      simuladoCursos      = new ArrayList<>();
            List<String> idsDisciplinas = new ArrayList<>();
            List<String> idsTurmas      = new ArrayList<>();
            List<String> idsCursos      = new ArrayList<>();
            List<Long>   idsSimulados   = new ArrayList<>();
            List<Long>   distinctIds    = new ArrayList<>();


            List<DisciplinasDTO> disciplinasDTO = getDisciplinas(token);
            
            disciplinasDTO.forEach(n -> idsDisciplinas.add(n.getId()));
            simuladoDisciplinas = simuladoDisciplinasRepository.findByDisciplinasAndPeriodo(disciplinasDTO.get(0).getIdPeriodoLetivo(), idsDisciplinas);
            
            // simuladoDisciplinas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));

            // simuladoDisciplinas.forEach(n -> idsTurmas.add(n.getIdTurma()));
            // simuladoTurmas = simuladoTurmasRepository.findByTurmasAndPeriodo(disciplinasDTO.get(0).getIdPeriodoLetivo(), idsTurmas);
            // simuladoTurmas.forEach(n -> idsSimulados.add(n.getSimulado().getId()));

            // simuladoTurmas.forEach(n -> idsCursos.add(n.getIdCurso()));
            // simuladoCursos = simuladoCursosRepository.findByCursoAndPeriodo(disciplinasDTO.get(0).getIdPeriodoLetivo(), idsCursos);
            // simuladoCursos.forEach(n -> idsSimulados.add(n.getSimulado().getId()));


            // distinctIds = idsSimulados.stream().distinct().collect(Collectors.toList());

            // SimuladoService simuladoService = new SimuladoService();
            // List<SimuladoDTO> simuladoDTOs = simuladoService.getQuestoes(distinctIds);
            
            return null; //simuladoDTOs;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar os simulados do aluno " + e);
        }
        return null;
    }
}