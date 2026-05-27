package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.matrizCurricular.MatrizCurricularAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.matrizCurricular.MatrizCurricularCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.MatrizCurricularResponseDTO;
import com.controle.monitoria_api.service.MatrizCurricularService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/matrizes-curriculares")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Matrizes Curriculares", description = "Endpoints para gerenciamento de matrizes curriculares dos cursos")
public class MatrizCurricularController {

    private final MatrizCurricularService service;

    @PostMapping
    @Operation(summary = "Criar nova matriz curricular", description = """
            Cadastra uma nova matriz curricular para um curso.
            
            **Regras de negócio:**
            - O nome da matriz deve ser único dentro do mesmo curso
            - A matriz deve estar vinculada a um curso existente
            - A matriz pode ser criada com status ATIVO ou INATIVO
            - A matriz pode conter várias disciplinas (via MatrizDisciplina)
            - Um curso pode ter várias matrizes, mas apenas uma ATIVA por vez
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Matriz curricular criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já existe para este curso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<MatrizCurricularResponseDTO> criar(@Valid @RequestBody MatrizCurricularCriacaoDTO dto) {
        var matriz = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(matriz.id())
                .toUri();

        return ResponseEntity.created(uri).body(matriz);
    }

    @GetMapping
    @Operation(summary = "Listar todas as matrizes curriculares", description = """
            Retorna uma lista paginada de todas as matrizes curriculares cadastradas.
            
            **Parâmetros de paginação:**
            - page: Número da página (inicia em 0)
            - size: Quantidade de itens por página (padrão: 10)
            - sort: Campo para ordenação (ex: nome, dataCadastro)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<MatrizCurricularResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar matrizes ativas", description = """
            Retorna apenas as matrizes curriculares com status ATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<MatrizCurricularResponseDTO>> listarAtivos(@ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar matrizes inativas", description = """
            Retorna apenas as matrizes curriculares com status INATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<MatrizCurricularResponseDTO>> listarInativos(@ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarInativos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(summary = "Listar matrizes por curso", description = """
            Retorna todas as matrizes curriculares (ativas e inativas) de um curso específico.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<Page<MatrizCurricularResponseDTO>> listarPorCurso(@PathVariable Long cursoId, @ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorCurso(cursoId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/curso/{cursoId}/ativa")
    @Operation(summary = "Buscar matriz ativa do curso", description = """
            Retorna a matriz curricular que está ativa para um determinado curso.
            
            **Regras de negócio:**
            - Um curso pode ter apenas UMA matriz ativa por vez
            - A matriz ativa é usada como referência para novos alunos
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matriz ativa encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Nenhuma matriz ativa encontrada para este curso")
    })
    public ResponseEntity<MatrizCurricularResponseDTO> buscarMatrizAtivaPorCurso(@PathVariable Long cursoId) {
        var matriz = service.listarMatrizAtivaPorCurso(cursoId);
        return ResponseEntity.ok(matriz);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar matriz por ID", description = "Retorna os detalhes completos de uma matriz curricular específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matriz encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Matriz não encontrada")
    })
    public ResponseEntity<MatrizCurricularResponseDTO> buscarPorId(@PathVariable Long id) {
        var matriz = service.listarPorId(id);
        return ResponseEntity.ok(matriz);
    }

    @PutMapping
    @Operation(summary = "Atualizar matriz curricular", description = """
            Atualiza os dados de uma matriz curricular existente.
            
            **Regras de negócio:**
            - Nome não pode ser alterado para um já existente no mesmo curso
            - É possível alterar o curso vinculado
            - Campos não informados mantêm o valor atual
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matriz atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já existe para este curso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Matriz ou Curso não encontrado")
    })
    public ResponseEntity<MatrizCurricularResponseDTO> atualizar(@Valid @RequestBody MatrizCurricularAtualizacaoDTO dto) {
        var matriz = service.atualizar(dto);
        return ResponseEntity.ok(matriz);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar matriz curricular", description = """
            Ativa uma matriz curricular para o curso.
            
            **REGRAS DE NEGÓCIO IMPORTANTES:**
            - Um curso pode ter APENAS UMA matriz ativa por vez
            - Ao ativar uma matriz, a matriz ativa anterior será mantida? (verificar regra de negócio)
            - Sugestão: ao ativar uma nova matriz, a anterior deve ser automaticamente inativada
            - A matriz ativa é usada como referência para novos alunos ingressantes
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Matriz ativada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Já existe uma matriz ativa para este curso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Matriz não encontrada")
    })
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar matriz curricular", description = """
            Inativa uma matriz curricular no sistema.
            
            **Regras de negócio:**
            - Matrizes inativas não podem ser usadas como referência para novos alunos
            - O registro permanece no banco para histórico
            - É possível reativar a matriz posteriormente
            - Uma matriz com disciplinas associadas pode ser inativada (mantém o histórico)
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Matriz inativada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Matriz não encontrada")
    })
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }
}