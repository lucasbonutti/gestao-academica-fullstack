package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.professor.ProfessorAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.professor.ProfessorCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.ProfessorResponseDTO;
import com.controle.monitoria_api.service.ProfessorService;
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
@RequestMapping("/professores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Professores", description = "Endpoints para gerenciamento de professores")
public class ProfessorController {

    private final ProfessorService service;

    @PostMapping
    @Operation(summary = "Cadastrar um novo professor", description = """
            Cadastra um novo professor no sistema.
            
            **Regras de negócio:**
            - Matrícula do professor deve ser única no sistema
            - Email do professor deve ser único no sistema
            - O professor deve estar vinculado a uma escola existente
            - O professor é cadastrado com status ATIVO automaticamente
            - O professor pode ter múltiplas formações acadêmicas
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Professor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, matrícula ou email já existente"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<ProfessorResponseDTO> criar(@Valid @RequestBody ProfessorCriacaoDTO dto) {
        var professor = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(professor.id())
                .toUri();

        return ResponseEntity.created(uri).body(professor);
    }

    @GetMapping
    @Operation(summary = "Listar todos os professores", description = """
            Retorna uma lista paginada de todos os professores cadastrados.
            
            **Parâmetros de paginação:**
            - page: Número da página (inicia em 0)
            - size: Quantidade de itens por página (padrão: 10)
            - sort: Campo para ordenação (ex: nomeCompleto, matricula)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<ProfessorResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"nomeCompleto"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar professores ativos", description = """
            Retorna apenas os professores com status ATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<ProfessorResponseDTO>> listarAtivos(@ParameterObject @PageableDefault(size = 10, sort = {"nomeCompleto"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar professores inativos", description = """
            Retorna apenas os professores com status INATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<ProfessorResponseDTO>> listarInativos(@ParameterObject @PageableDefault(size = 10, sort = {"nomeCompleto"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarInativos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/escola/{escolaId}")
    @Operation(summary = "Listar professores por escola", description = """
            Retorna todos os professores vinculados a uma escola específica.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<Page<ProfessorResponseDTO>> listarPorEscola(@PathVariable Long escolaId, @ParameterObject @PageableDefault(size = 10, sort = {"nomeCompleto"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorEscola(escolaId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar professor por ID", description = "Retorna os detalhes completos de um professor específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    public ResponseEntity<ProfessorResponseDTO> listarPorId(@PathVariable Long id) {
        var professor = service.listarPorId(id);
        return ResponseEntity.ok(professor);
    }

    @PutMapping
    @Operation(summary = "Atualizar professor", description = """
            Atualiza os dados de um professor existente.
            
            **Regras de negócio:**
            - Matrícula não pode ser alterada para uma já existente
            - Email não pode ser alterado para um já existente
            - É possível alterar a escola vinculada
            - Campos não informados mantêm o valor atual
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, matrícula ou email duplicado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Professor ou Escola não encontrado")
    })
    public ResponseEntity<ProfessorResponseDTO> atualizar(@RequestBody @Valid ProfessorAtualizacaoDTO dto) {
        var professor = service.atualizar(dto);
        return ResponseEntity.ok(professor);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar um professor", description = """
            Inativa um professor no sistema.
            
            **Regras de negócio:**
            - Professores inativos não podem ser orientadores de novas monitorias
            - O registro permanece no banco para histórico
            - É possível reativar o professor posteriormente
            - O usuário associado ao professor pode ser afetado
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Professor inativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Reativar um professor", description = """
            Reativa um professor que foi previamente inativado.
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Professor reativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}
