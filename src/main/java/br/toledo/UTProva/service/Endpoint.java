package br.toledo.UTProva.service;


public class Endpoint {

    public static final String SERVICE_TOLEDO    = "https://servicos.toledo.br";
    public static final String USER_AGENT        = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0";
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String SISTEMA_LOGIN = "/sistema/login";
    public static final String SISTEMA_CONTEXTO = "/sistema/rest/comum/contextos/";
    public static final String ACADEMICO_PERIODOS_LETIVOS = "/academico/rest/coordenadores/periodosLetivos/";
    public static final String ACADEMICO_COORDENADOR_CURSO = "/academico/rest/coordenadores/cursos/";
    public static final String ACADEMICO_COORDENADOR_TURMAS = "/academico/rest/coordenadores/turmas/";
    public static final String ACADEMICO_COORDENADOR_DISCIPLINAS = "/academico/rest/coordenadores/disciplinas/";
    public static final String ACADEMICO_PROFESSOR_CURSOS = "/academico/rest/professores/cursos/";
    public static final String ACADEMICO_PROFESSOR_TURMAS = "/academico/rest/professores/turmas/";
    public static final String ACADEMICO_PROFESSOR_DISCIPLINAS = "/academico/rest/professores/disciplinas/";
    public static final String ACADEMICO_ALUNO_DISCIPLINAS = "/academico/rest/alunos/disciplinasMatriculadas";
    public static final String ACADEMICO_ALUNO_CURSOS = "/academico/rest/alunos/curso";
    public static final String ACADEMICO_LOGOUT = "/academico/logout";

    
}
