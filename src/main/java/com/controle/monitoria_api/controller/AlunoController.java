package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.aluno.AlunoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.aluno.AlunoCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.AlunoResponseDTO;
import com.controle.monitoria_api.service.AlunoService;
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
@RequestMapping("/alunos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Alunos", description = "Endpoints para gerenciamento de alunos")
public class AlunoController {

    private final AlunoService service;

    @PostMapping
    @Operation(summary = "Cadastrar um novo aluno", description = """
            Cadastro de aluno no sistema.
            
            **Regras de negócio:**
            - Matrícula deve ser única no sistema
            - Aluno é cadastrado com status ATIVO automaticamente
            - Data de cadastro é preenchida automaticamente
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou matrícula já existente"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<AlunoResponseDTO> criar(@Valid @RequestBody AlunoCriacaoDTO dto) {
        var aluno = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(aluno.id())
                .toUri();

        return ResponseEntity.created(uri).body(aluno);
    }

    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista paginada de todos os alunos cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<AlunoResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"nomeCompleto"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar alunos ativos", description = "Retorna apenas os alunos com status ATIVO.")
    public ResponseEntity<Page<AlunoResponseDTO>> listarAtivos(@ParameterObject @PageableDefault(size = 10, sort = {"nomeCompleto"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar alunos inativos", description = "Retorna apenas os alunos com status INATIVO.")
    public ResponseEntity<Page<AlunoResponseDTO>> listarInativos(@ParameterObject @PageableDefault(size = 10, sort = {"nomeCompleto"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarInativos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os detalhes completos de um aluno específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<AlunoResponseDTO> listarPorId(@PathVariable Long id) {
        var aluno = service.listarPorId(id);
        return ResponseEntity.ok(aluno);
    }

    @PutMapping
    @Operation(summary = "Atualizar dados do aluno", description = """
            Atualiza as informações de um aluno existente.
            
            **Regras de negócio:**
            - Matrícula não pode ser alterada para uma já existente
            - Campos não informados mantêm o valor atual
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou matrícula duplicada"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<AlunoResponseDTO> atualizar(@Valid @RequestBody AlunoAtualizacaoDTO dto) {
        var aluno = service.atualizar(dto);
        return ResponseEntity.ok(aluno);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar um aluno", description = """
            Inativa um aluno no sistema.
            
            **Regras de negócio:**
            - Alunos inativos não podem ser vinculados a novas monitorias
            - O registro permanece no banco para histórico
            - É possível reativar o aluno posteriormente
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aluno inativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Reativar um aluno", description = """
            Reativa um aluno que foi previamente inativado.
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aluno reativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}