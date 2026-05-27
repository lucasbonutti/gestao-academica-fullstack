package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.ies.IESAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.ies.IESCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.IESResponseDTO;
import com.controle.monitoria_api.service.IESService;
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
@RequestMapping("/ies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "IES", description = "Endpoints para gerenciamento de Instituições de Ensino Superior")
public class IESController {

    private final IESService service;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova IES", description = """
            Cadastra uma nova Instituição de Ensino Superior.
            
            **Regras de negócio:**
            - O nome da IES deve ser único no sistema
            - Endereço e telefone são informações obrigatórias
            - A IES pode ter múltiplas escolas vinculadas
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "IES cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já existente"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN")
    })
    public ResponseEntity<IESResponseDTO> criar(@Valid @RequestBody IESCriacaoDTO dto) {
        var ies = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ies.id())
                .toUri();

        return ResponseEntity.created(uri).body(ies);
    }

    @GetMapping
    @Operation(summary = "Listar todas as IES", description = """
            Retorna uma lista paginada de todas as Instituições de Ensino Superior cadastradas.
            
            **Parâmetros de paginação:**
            - page: Número da página (inicia em 0)
            - size: Quantidade de itens por página (padrão: 10)
            - sort: Campo para ordenação (ex: nome, endereco)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<IESResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar IES por ID", description = "Retorna os detalhes completos de uma IES específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "IES encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "IES não encontrada")
    })
    public ResponseEntity<IESResponseDTO> listarPorId(@PathVariable Long id) {
        var ies = service.listarPorId(id);
        return ResponseEntity.ok(ies);
    }

    @PutMapping
    @Operation(summary = "Atualizar IES", description = """
            Atualiza os dados de uma IES existente.
            
            **Regras de negócio:**
            - Nome não pode ser alterado para um já existente
            - Endereço e telefone podem ser atualizados
            - Campos não informados mantêm o valor atual
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "IES atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "IES não encontrada")
    })
    public ResponseEntity<IESResponseDTO> atualizar(@RequestBody @Valid IESAtualizacaoDTO dto) {
        var ies = service.atualizar(dto);
        return ResponseEntity.ok(ies);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma IES", description = """
            Remove uma IES do sistema.
            
            **Regras de negócio:**
            - Não é possível excluir uma IES que possui escolas vinculadas
            - A exclusão é definitiva
            - IES sem histórico podem ser excluídas
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "IES excluída com sucesso"),
            @ApiResponse(responseCode = "400", description = "IES possui escolas vinculadas, não pode ser excluída"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "IES não encontrada")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
