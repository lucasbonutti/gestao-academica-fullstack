package com.controle.monitoria_api.controller;

import com.controle.monitoria_api.model.dto.request.matrizDisciplina.MatrizDisciplinaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.matrizDisciplina.MatrizDisciplinaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.MatrizDisciplinaResponseDTO;
import com.controle.monitoria_api.service.MatrizDisciplinaService;
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
@RequestMapping("/matrizes-disciplinas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Matrizes-Disciplinas", description = "Endpoints para gerenciamento da associação entre matrizes curriculares e disciplinas, incluindo pré-requisitos")
public class MatrizDisciplinaController {

    private final MatrizDisciplinaService service;

    @PostMapping
    @Operation(summary = "Associar disciplina a uma matriz curricular", description = """
            Cria uma associação entre uma disciplina e uma matriz curricular, com opção de definir pré-requisitos.
            
            **REGRAS DE NEGÓCIO IMPORTANTES:**
            - Uma disciplina não pode ser associada mais de uma vez à mesma matriz
            - Todos os pré-requisitos informados DEVEM existir na mesma matriz curricular
            - Pré-requisitos são disciplinas que o aluno deve cursar antes da disciplina atual
            - A associação herda o status da matriz (ativa/inativa)
            
            **Pré-requisitos:**
            - A disciplina principal é a que está sendo associada
            - Os pré-requisitos são disciplinas que já existem na matriz
            - É possível criar associação sem pré-requisitos (lista vazia)
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Associação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = """
                    Possíveis erros:
                    - Disciplina já está associada a esta matriz
                    - Dados inválidos
                    """),
            @ApiResponse(responseCode = "401", description = "Não autorizado. Token JWT não fornecido ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Matriz curricular ou Disciplina não encontrada")
    })
    public ResponseEntity<MatrizDisciplinaResponseDTO> criar(@Valid @RequestBody MatrizDisciplinaCriacaoDTO dto) {
        var associacao = service.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(associacao.id())
                .toUri();

        return ResponseEntity.created(uri).body(associacao);
    }

    @GetMapping
    @Operation(summary = "Listar todas as associações", description = """
            Retorna uma lista paginada de todas as associações entre matrizes e disciplinas.
            
            **Parâmetros de paginação:**
            - page: Número da página (inicia em 0)
            - size: Quantidade de itens por página (padrão: 10)
            - sort: Campo para ordenação (ex: id)
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<MatrizDisciplinaResponseDTO>> listarTodos(@ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarTodos(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/matriz/{matrizId}")
    @Operation(summary = "Listar disciplinas de uma matriz", description = """
            Retorna todas as disciplinas associadas a uma matriz curricular específica.
            
            **Útil para:**
            - Visualizar o currículo completo de um curso
            - Verificar quais disciplinas já estão na matriz
            - Analisar pré-requisitos das disciplinas
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Matriz curricular não encontrada")
    })
    public ResponseEntity<Page<MatrizDisciplinaResponseDTO>> listarPorMatriz(@PathVariable Long matrizId, @ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorMatriz(matrizId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/disciplina/{disciplinaId}")
    @Operation(summary = "Listar matrizes que contêm uma disciplina", description = """
            Retorna todas as matrizes curriculares que possuem determinada disciplina.
            
            **Útil para:**
            - Saber em quais cursos uma disciplina é oferecida
            - Verificar como os pré-requisitos variam entre matrizes
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")
    })
    public ResponseEntity<Page<MatrizDisciplinaResponseDTO>> listarPorDisciplina(@PathVariable Long disciplinaId, @ParameterObject @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        var page = service.listarPorDisciplina(disciplinaId, paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar associação por ID", description = """
            Retorna os detalhes completos de uma associação específica entre matriz e disciplina,
            incluindo sua lista de pré-requisitos.
            
            **Perfis com acesso:** ADMIN, PROFESSOR
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Associação encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada")
    })
    public ResponseEntity<MatrizDisciplinaResponseDTO> buscarPorId(@PathVariable Long id) {
        var associacao = service.listarPorId(id);
        return ResponseEntity.ok(associacao);
    }

    @PutMapping
    @Operation(summary = "Atualizar associação", description = """
            Atualiza os dados de uma associação entre matriz e disciplina.
            
            **REGRAS DE NEGÓCIO:**
            - É possível alterar a matriz ou disciplina vinculada
            - É possível modificar a lista de pré-requisitos
            - Os novos pré-requisitos devem existir na matriz atual
            - Não é permitido criar duplicidade (mesma disciplina na mesma matriz)
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Associação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou associação duplicada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Associação, Matriz ou Disciplina não encontrada")
    })
    public ResponseEntity<MatrizDisciplinaResponseDTO> atualizar(@Valid @RequestBody MatrizDisciplinaAtualizacaoDTO dto) {
        var associacao = service.atualizar(dto);
        return ResponseEntity.ok(associacao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover associação", description = """
            Remove a associação entre uma disciplina e uma matriz curricular.
            
            **REGRAS DE NEGÓCIO:**
            - Esta ação é irreversível
            - Ao remover uma disciplina, ela deixa de fazer parte da matriz
            - Os pré-requisitos que dependem desta disciplina podem ser afetados
            - Recomenda-se verificar se a disciplina não é pré-requisito de outras antes de remover
            
            **Perfis com acesso:** APENAS ADMIN
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Associação removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado, requer perfil ADMIN"),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
