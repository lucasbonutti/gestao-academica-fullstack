package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.disciplina.DisciplinaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.disciplina.DisciplinaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.DisciplinaResponseDTO;
import com.controle.monitoria_api.service.DisciplinaService;
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
@RequestMapping("/disciplinas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Disciplinas", description = "Endpoints para gerenciamento de disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova disciplina", description = """
            Cadastra uma nova disciplina no sistema.
            
            **Regras de negócio:**
            - A sigla da disciplina deve ser única no sistema
            - A disciplina deve estar vinculada a uma escola existente
            - A carga horária deve ser um valor positivo
            - A disciplina é cadastrada com status ATIVO automaticamente
            - Uma disciplina pode pertencer a várias matrizes curriculares
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Disciplina cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou sigla já existente"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<DisciplinaResponseDTO> criar(@Valid @RequestBody DisciplinaCriacaoDTO dto) {
        var disciplina = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(disciplina.id())
                .toUri();

        return ResponseEntity.created(uri).body(disciplina);
    }

    @GetMapping
    @Operation(summary = "Listar todas as disciplinas", description = """
            Retorna uma lista paginada de todas as disciplinas cadastradas.
            
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
    public ResponseEntity<Page<DisciplinaResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar disciplinas ativas", description = """
            Retorna apenas as disciplinas com status ATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<DisciplinaResponseDTO>> listarAtivos(@ParameterObject @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar disciplinas inativas", description = """
            Retorna apenas as disciplinas com status INATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<DisciplinaResponseDTO>> listarInativos(@ParameterObject @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarInativos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/escola/{escolaId}")
    @Operation(summary = "Listar disciplinas por escola", description = """
            Retorna todas as disciplinas vinculadas a uma escola específica.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<Page<DisciplinaResponseDTO>> listarPorEscola(@PathVariable Long escolaId, @ParameterObject @PageableDefault(size = 10, sort = {"sigla"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorEscola(escolaId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar disciplina por ID", description = "Retorna os detalhes completos de uma disciplina específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<DisciplinaResponseDTO> listarPorId(@PathVariable Long id) {
        var disciplina = service.listarPorId(id);
        return ResponseEntity.ok(disciplina);
    }

    @PutMapping
    @Operation(summary = "Atualizar disciplina", description = """
            Atualiza os dados de uma disciplina existente.
            
            **Regras de negócio:**
            - Sigla não pode ser alterada para uma já existente
            - É possível alterar a escola vinculada
            - Carga horária deve ser positiva
            - Campos não informados mantêm o valor atual
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou sigla duplicada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Disciplina ou Escola não encontrada")
    })
    public ResponseEntity<DisciplinaResponseDTO> atualizar(@Valid @RequestBody DisciplinaAtualizacaoDTO dto) {
        var disciplina = service.atualizar(dto);
        return ResponseEntity.ok(disciplina);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar uma disciplina", description = """
            Inativa uma disciplina no sistema.
            
            **Regras de negócio:**
            - Disciplinas inativas não podem ser vinculadas a novas matrizes
            - Disciplinas inativas não podem ser usadas em novas monitorias
            - O registro permanece no banco para histórico
            - É possível reativar a disciplina posteriormente
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disciplina inativada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Reativar uma disciplina", description = """
            Reativa uma disciplina que foi previamente inativada.
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disciplina reativada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}
