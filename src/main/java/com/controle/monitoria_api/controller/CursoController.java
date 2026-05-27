package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.curso.CursoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.curso.CursoCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.CursoResponseDTO;
import com.controle.monitoria_api.service.CursoService;
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
@RequestMapping("/cursos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cursos", description = "Endpoints para gerenciamento de cursos das escolas")
public class CursoController {

    private final CursoService service;

    @PostMapping
    @Operation(summary = "Cadastrar um novo curso", description = """
            Cadastra um novo curso vinculado a uma escola.
            
            **Regras de negócio:**
            - A sigla do curso deve ser única no sistema
            - O curso deve estar vinculado a uma escola existente
            - O curso é cadastrado com status ATIVO automaticamente
            - O turno do curso deve ser informado (MATUTINO, VESPERTINO, NOTURNO, INTEGRAL)
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Curso cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou sigla já existente"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<CursoResponseDTO> criar(@Valid @RequestBody CursoCriacaoDTO dto) {
        var curso = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(curso.id())
                .toUri();

        return ResponseEntity.created(uri).body(curso);
    }

    @GetMapping
    @Operation(summary = "Listar todos os cursos", description = """
            Retorna uma lista paginada de todos os cursos cadastrados.
            
            **Parâmetros de paginação:**
            - page: Número da página (inicia em 0)
            - size: Quantidade de itens por página (padrão: 10)
            - sort: Campo para ordenação (ex: sigla, descricao)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<CursoResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar cursos ativos", description = """
            Retorna apenas os cursos com status ATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<CursoResponseDTO>> listarAtivos(
            @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar cursos inativos", description = """
            Retorna apenas os cursos com status INATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<CursoResponseDTO>> listarInativos(@ParameterObject @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarInativos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/escola/{escolaId}")
    @Operation(summary = "Listar cursos por escola", description = """
            Retorna todos os cursos vinculados a uma escola específica.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<Page<CursoResponseDTO>> listarPorEscola(@PathVariable Long escolaId, @ParameterObject @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorEscola(escolaId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por ID", description = "Retorna os detalhes completos de um curso específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<CursoResponseDTO> listarPorId(@PathVariable Long id) {
        var curso = service.listarPorId(id);
        return ResponseEntity.ok(curso);
    }

    @PutMapping
    @Operation(summary = "Atualizar curso", description = """
            Atualiza os dados de um curso existente.
            
            **Regras de negócio:**
            - Sigla não pode ser alterada para uma já existente
            - É possível alterar a escola vinculada
            - Campos não informados mantêm o valor atual
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou sigla duplicada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Curso ou Escola não encontrado")
    })
    public ResponseEntity<CursoResponseDTO> atualizar(@Valid @RequestBody CursoAtualizacaoDTO dto) {
        var curso = service.atualizar(dto);
        return ResponseEntity.ok(curso);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar um curso", description = """
            Inativa um curso no sistema.
            
            **Regras de negócio:**
            - Cursos inativos não podem ser vinculados a novas matrizes
            - O registro permanece no banco para histórico
            - É possível reativar o curso posteriormente
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Curso inativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Reativar um curso", description = """
            Reativa um curso que foi previamente inativado.
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Curso reativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}
