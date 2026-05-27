package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.relatorioMonitoria.RelatorioMonitoriaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.RelatorioMonitoriaResponseDTO;
import com.controle.monitoria_api.service.RelatorioMonitoriaService;
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
@RequestMapping("/relatorios-monitoria")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Relatórios", description = "Endpoints para gerenciamento de relatórios de monitoria")
public class RelatorioMonitoriaController {

    private final RelatorioMonitoriaService service;

    @PostMapping
    @Operation(summary = "Criar relatório de monitoria", description = """
            Gera um relatório final para uma monitoria concluída.

            **Regras de negócio:**
            - Só é possível criar relatório para monitorias com status FINALIZADA
            - Cada monitoria pode ter apenas UM relatório
            - O parecer final é obrigatório
            - O número de alunos atendidos deve ser positivo

            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Relatório criado com sucesso"),
            @ApiResponse(responseCode = "400", description = """
                    Possíveis erros:
                    - Monitoria não está finalizada
                    - Já existe relatório para esta monitoria
                    """),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Monitoria não encontrada")
    })
    public ResponseEntity<RelatorioMonitoriaResponseDTO> criar(@Valid @RequestBody RelatorioMonitoriaCriacaoDTO dto) {
        var relatorio = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(relatorio.id())
                .toUri();

        return ResponseEntity.created(uri).body(relatorio);
    }

    @GetMapping
    @Operation(summary = "Listar todos os relatórios", description = """
            Retorna uma lista paginada de todos os relatórios cadastrados.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<RelatorioMonitoriaResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/professor/{professorId}")
    @Operation(summary = "Listar relatórios por professor", description = """
            Retorna todos os relatórios das monitorias orientadas por um professor específico.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    public ResponseEntity<Page<RelatorioMonitoriaResponseDTO>> listarPorProfessor(@PathVariable Long professorId, @ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorProfessor(professorId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/disciplina/{disciplinaId}")
    @Operation(summary = "Listar relatórios por disciplina", description = """
            Retorna todos os relatórios de monitorias de uma disciplina específica.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Page<RelatorioMonitoriaResponseDTO>> listarPorDisciplina(@PathVariable Long disciplinaId, @ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorDisciplina(disciplinaId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/aluno/{alunoId}")
    @Operation(summary = "Listar relatórios por aluno", description = """
            Retorna todos os relatórios das monitorias realizadas por um aluno específico.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<Page<RelatorioMonitoriaResponseDTO>> listarPorAluno(@PathVariable Long alunoId, @ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorAluno(alunoId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/semestre/{semestre}")
    @Operation(summary = "Listar relatórios por semestre", description = """
            Retorna todos os relatórios de monitorias de um determinado semestre.
            
            **Formato do semestre:** YYYY.S (ex: 2024.1, 2024.2)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Formato de semestre inválido"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<RelatorioMonitoriaResponseDTO>> listarPorSemestre(@PathVariable String semestre, @ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorSemestre(semestre, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/status-monitoria/{status}")
    @Operation(summary = "Listar relatórios por status da monitoria", description = """
            Retorna os relatórios filtrando pelo status da monitoria associada.
            
            **Status possíveis:**
            - EM_ANDAMENTO: Monitoria em curso
            - FINALIZADA: Monitoria concluída 
            - CANCELADA: Monitoria cancelada
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<RelatorioMonitoriaResponseDTO>> listarPorStatusMonitoria(@PathVariable String status, @ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorStatusMonitoria(status, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/monitoria/{monitoriaId}")
    @Operation(summary = "Buscar relatório por monitoria", description = """
            Retorna o relatório associado a uma monitoria específica.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Relatório ou monitoria não encontrado")
    })
    public ResponseEntity<RelatorioMonitoriaResponseDTO> listarPorMonitoria(@PathVariable Long monitoriaId) {
        var relatorio = service.listarPorMonitoria(monitoriaId);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar relatório por ID", description = "Retorna um relatório específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Relatório não encontrado")
    })
    public ResponseEntity<RelatorioMonitoriaResponseDTO> listarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPorId(id));
    }
}