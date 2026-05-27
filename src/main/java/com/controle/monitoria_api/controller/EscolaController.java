package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.escola.EscolaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.escola.EscolaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.EscolaResponseDTO;
import com.controle.monitoria_api.service.EscolaService;
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
@RequestMapping("/escolas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Escolas", description = "Endpoints para gerenciamento de escolas")
public class EscolaController {

    private final EscolaService service;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova escola", description = """
            Cadastra uma nova escola vinculada a uma IES.
            
            **Regras de negócio:**
            - O nome da escola deve ser único dentro da mesma IES
            - A escola deve estar vinculada a uma IES existente
            - Cada escola possui um único coordenador
            - A escola é cadastrada com status ATIVO automaticamente
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Escola cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já existe nesta IES"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "IES não encontrada")
    })
    public ResponseEntity<EscolaResponseDTO> criar(@Valid @RequestBody EscolaCriacaoDTO dto) {
        var escola = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(escola.id())
                .toUri();

        return ResponseEntity.created(uri).body(escola);
    }

    @GetMapping
    @Operation(summary = "Listar todas as escolas", description = """
            Retorna uma lista paginada de todas as escolas cadastradas.
            
            **Parâmetros de paginação:**
            - page: Número da página (inicia em 0)
            - size: Quantidade de itens por página (padrão: 10)
            - sort: Campo para ordenação (ex: nome, coordenador)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<EscolaResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar escolas ativas", description = """
            Retorna apenas as escolas com status ATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<EscolaResponseDTO>> listarAtivos(@ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarAtivos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar escolas inativas", description = """
            Retorna apenas as escolas com status INATIVO.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<EscolaResponseDTO>> listarInativos(@ParameterObject @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarInativos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/ies/{iesId}")
    @Operation(summary = "Listar escolas por IES", description = """
            Retorna todas as escolas vinculadas a uma Instituição de Ensino Superior específica.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "IES não encontrada")
    })
    public ResponseEntity<Page<EscolaResponseDTO>> listarPorIES(@ParameterObject @PathVariable Long iesId, @PageableDefault(size = 10, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorIES(iesId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar escola por ID", description = "Retorna os detalhes completos de uma escola específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Escola encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<EscolaResponseDTO> listarPorId(@PathVariable Long id) {
        var escola = service.listarPorId(id);
        return ResponseEntity.ok(escola);
    }

    @PutMapping
    @Operation(summary = "Atualizar escola", description = """
            Atualiza os dados de uma escola existente.
            
            **Regras de negócio:**
            - Nome não pode ser alterado para um já existente na mesma IES
            - É possível alterar a IES vinculada
            - É possível alterar o coordenador
            - Campos não informados mantêm o valor atual
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Escola atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já existe nesta IES"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Escola ou IES não encontrada")
    })
    public ResponseEntity<EscolaResponseDTO> atualizar(@RequestBody @Valid EscolaAtualizacaoDTO dto) {
        var escola = service.atualizar(dto);
        return ResponseEntity.ok(escola);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar uma escola", description = """
            Inativa uma escola no sistema.
            
            **Regras de negócio:**
            - Escolas inativas não podem receber novos cursos
            - Escolas inativas não podem receber novos professores
            - O registro permanece no banco para histórico
            - É possível reativar a escola posteriormente
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Escola inativada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Reativar uma escola", description = """
            Reativa uma escola que foi previamente inativada.
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Escola reativada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada")
    })
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }
}
