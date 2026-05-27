package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.monitoria.MonitoriaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.monitoria.MonitoriaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.MonitoriaResponseDTO;
import com.controle.monitoria_api.service.MonitoriaService;
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
@RequestMapping("/monitorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Monitorias", description = "Endpoints para gerenciamento de monitorias acadêmicas")
public class MonitoriaController {

    private final MonitoriaService service;

    @PostMapping
    @Operation(summary = "Criar uma nova monitoria", description = """
            Cadastro de monitoria para um aluno em uma disciplina específica.
            
            **Regras de negócio:**
            - O aluno não pode ser monitor na mesma disciplina no mesmo semestre
            - A data de início deve ser anterior à data de fim
            - O aluno, disciplina e professor devem estar ativos no sistema
            - A monitoria é criada com status EM_ANDAMENTO automaticamente
            - O semestre deve seguir o formato: YYYY.S (ex: 2024.1, 2024.2)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Monitoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou violação de regra de negócio"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Aluno, Disciplina ou Professor não encontrado")
    })
    public ResponseEntity<MonitoriaResponseDTO> criar(@Valid @RequestBody MonitoriaCriacaoDTO dto) {
        var monitoria = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(monitoria.id())
                .toUri();

        return ResponseEntity.created(uri).body(monitoria);
    }

    @GetMapping
    @Operation(summary = "Listar todas as monitorias", description = """
            Retorna uma lista paginada de todas as monitorias cadastradas.
            
            **Parâmetros de paginação:**
            - page: Número da página (inicia em 0)
            - size: Quantidade de itens por página (padrão: 10)
            - sort: Campo para ordenação (ex: semestre, dataCadastro)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<Page<MonitoriaResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"semestre"}, direction = Sort.Direction.DESC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/professor/{professorId}")
    @Operation(summary = "Listar monitorias por professor", description = """
            Retorna todas as monitorias orientadas por um professor específico.
            Útil para o professor visualizar seus monitorandos.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado")
    })
    public ResponseEntity<Page<MonitoriaResponseDTO>> listarPorProfessor(@PathVariable Long professorId, @ParameterObject @PageableDefault(size = 10, sort = {"semestre"}, direction = Sort.Direction.DESC) Pageable paginacao) {
        var page = service.listarPorProfessor(professorId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/aluno/{alunoId}")
    @Operation(summary = "Listar monitorias por aluno", description = """
            Retorna o histórico de monitorias de um aluno específico.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<Page<MonitoriaResponseDTO>> listarPorAluno(@PathVariable Long alunoId, @ParameterObject @PageableDefault(size = 10, sort = {"semestre"}, direction = Sort.Direction.DESC) Pageable paginacao) {
        var page = service.listarPorAluno(alunoId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar monitorias por status", description = """
            Retorna monitorias filtradas pelo status atual.
            
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
    public ResponseEntity<Page<MonitoriaResponseDTO>> listarPorStatus(@PathVariable String status, @ParameterObject @PageableDefault(size = 10, sort = {"dataCadastro"}, direction = Sort.Direction.DESC) Pageable paginacao) {
        var page = service.listarPorStatus(status, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar monitoria por ID", description = "Retorna os detalhes completos de uma monitoria específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monitoria encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Monitoria não encontrada")
    })
    public ResponseEntity<MonitoriaResponseDTO> listarPorId(@PathVariable Long id) {
        var monitoria = service.listarPorId(id);
        return ResponseEntity.ok(monitoria);
    }

    @PutMapping
    @Operation(summary = "Atualizar uma monitoria", description = """
            Atualiza os dados de uma monitoria existente.
            
            **Restrições:**
            - Apenas ADMIN pode atualizar monitorias
            - Monitorias finalizadas não podem ser atualizadas
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monitoria atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Monitoria não encontrada")
    })
    public ResponseEntity<MonitoriaResponseDTO> atualizar(@Valid @RequestBody MonitoriaAtualizacaoDTO dto) {
        var monitoria = service.atualizar(dto);
        return ResponseEntity.ok(monitoria);
    }

    @PatchMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar uma monitoria", description = """
            Marca uma monitoria como FINALIZADA.
            
            **Regras de negócio:**
            - A monitoria deve estar com status EM_ANDAMENTO
            - Após finalizada, não é possível editar a monitoria
            - Uma monitoria finalizada pode ter um relatório associado
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Monitoria finalizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Monitoria não encontrada")
    })
    public ResponseEntity<Void> finalizar(@PathVariable Long id) {
        service.finalizar(id);
        return ResponseEntity.noContent().build();
    }
}