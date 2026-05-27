package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.formacao.FormacaoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.formacao.FormacaoCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.FormacaoResponseDTO;
import com.controle.monitoria_api.service.FormacaoService;
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
@RequestMapping("/formacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Formações", description = "Endpoints para gerenciamento da formação acadêmica dos professores")
public class FormacaoController {

    private final FormacaoService service;

    @PostMapping
    @Operation(summary = "Adicionar formação ao professor", description = """
            Registra uma nova formação acadêmica para um professor.
            
            **Regras de negócio:**
            - Uma mesma formação (titulação + curso + instituição) não pode ser duplicada para o mesmo professor
            - Ano de conclusão deve ser entre 1900 e 2100
            - Professor deve estar ativo no sistema
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Formação cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou formação já cadastrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    public ResponseEntity<FormacaoResponseDTO> criar(@Valid @RequestBody FormacaoCriacaoDTO dto) {
        var formacao = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(formacao.id())
                .toUri();

        return ResponseEntity.created(uri).body(formacao);
    }

    @GetMapping
    @Operation(summary = "Listar todas as formações", description = """
            Retorna uma lista paginada de todas as formações cadastradas.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<FormacaoResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"titulacao"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/professor/{professorId}")
    @Operation(summary = "Listar formações por professor", description = """
            Retorna todas as formações acadêmicas de um professor específico.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    public ResponseEntity<Page<FormacaoResponseDTO>> listarPorProfessor(@ParameterObject @PathVariable Long professorId, @PageableDefault(size = 10, sort = {"titulacao"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorProfessor(professorId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar formação por ID", description = "Retorna os detalhes de uma formação específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Formação encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Formação não encontrada")
    })
    public ResponseEntity<FormacaoResponseDTO> listarPorId(@PathVariable Long id) {
        var formacao = service.listarPorId(id);
        return ResponseEntity.ok(formacao);
    }

    @PutMapping
    @Operation(summary = "Atualizar formação", description = """
            Atualiza os dados de uma formação existente.
            
            **Regras de negócio:**
            - Não é permitido duplicar formação para o mesmo professor
            - Ano de conclusão deve ser entre 1900 e 2100
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Formação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou formação já cadastrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Formação não encontrada")
    })
    public ResponseEntity<FormacaoResponseDTO> atualizar(@Valid @RequestBody FormacaoAtualizacaoDTO dto) {
        var formacao = service.atualizar(dto);
        return ResponseEntity.ok(formacao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir formação", description = """
            Remove uma formação acadêmica do professor.
            
            **Atenção:** Esta ação é irreversível!
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Formação excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Formação não encontrada")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}