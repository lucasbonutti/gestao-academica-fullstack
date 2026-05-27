package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Disciplina;
import com.controle.monitoria_api.model.MatrizCurricular;
import com.controle.monitoria_api.model.MatrizDisciplina;
import com.controle.monitoria_api.model.dto.request.matrizDisciplina.MatrizDisciplinaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.matrizDisciplina.MatrizDisciplinaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.MatrizDisciplinaResponseDTO;
import com.controle.monitoria_api.repository.DisciplinaRepository;
import com.controle.monitoria_api.repository.MatrizCurricularRepository;
import com.controle.monitoria_api.repository.MatrizDisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatrizDisciplinaService {

    private final MatrizDisciplinaRepository matrizDisciplinaRepository;

    private final MatrizCurricularRepository matrizCurricularRepository;

    private final DisciplinaRepository disciplinaRepository;

    @Transactional
    public MatrizDisciplinaResponseDTO criar(MatrizDisciplinaCriacaoDTO dto) {
        var matrizCurricular = matrizCurricularRepository.findById(dto.matrizId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matriz não encontrada!"));

        var disciplina = disciplinaRepository.findById(dto.disciplinaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada!"));

        if (matrizDisciplinaRepository.existsByMatrizCurricularIdAndDisciplinaId(dto.matrizId(), dto.disciplinaId())) {
            throw new ValidacaoException("Esta disciplina já está associada a esta matriz!");
        }

        List<Disciplina> preRequisitos = buscarPreRequisitos(dto.preRequisitosIds());
        validarPreRequisitosNaMatriz(matrizCurricular, preRequisitos);

        var matrizDisciplina = new MatrizDisciplina(dto, matrizCurricular, disciplina, preRequisitos);

        var salvar = matrizDisciplinaRepository.save(matrizDisciplina);
        return new MatrizDisciplinaResponseDTO(salvar);
    }

    public Page<MatrizDisciplinaResponseDTO> listarTodos(Pageable paginacao) {
        return matrizDisciplinaRepository.findAll(paginacao)
                .map(MatrizDisciplinaResponseDTO::new);
    }

    public Page<MatrizDisciplinaResponseDTO> listarPorMatriz(Long matrizId, Pageable paginacao) {
        if (!matrizCurricularRepository.existsById(matrizId)) {
            throw new RecursoNaoEncontradoException("Matriz não encontrada!");
        }
        return matrizDisciplinaRepository.findByMatrizCurricularId(matrizId, paginacao)
                .map(MatrizDisciplinaResponseDTO::new);
    }

    public Page<MatrizDisciplinaResponseDTO> listarPorDisciplina(Long disciplinaId, Pageable paginacao) {
        if (!disciplinaRepository.existsById(disciplinaId)) {
            throw new RecursoNaoEncontradoException("Disciplina não encontrada!");
        }
        return matrizDisciplinaRepository.findByDisciplinaId(disciplinaId, paginacao)
                .map(MatrizDisciplinaResponseDTO::new);
    }

    public MatrizDisciplinaResponseDTO listarPorId(Long id) {
        var matrizDisciplina = matrizDisciplinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Associação não encontrada!"));
        return new MatrizDisciplinaResponseDTO(matrizDisciplina);
    }

    @Transactional
    public MatrizDisciplinaResponseDTO atualizar(MatrizDisciplinaAtualizacaoDTO dto) {
        MatrizDisciplina matrizDisciplina = matrizDisciplinaRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Associação não encontrada!"));

        MatrizCurricular novaMatriz = null;
        if (dto.matrizId() != null) {
            novaMatriz = matrizCurricularRepository.findById(dto.matrizId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Matriz não encontrada!"));
        }

        Disciplina novaDisciplina = null;
        if (dto.disciplinaId() != null) {
            novaDisciplina = disciplinaRepository.findById(dto.disciplinaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada!"));
        }

        validarDuplicidadeNaAtualizacao(dto, matrizDisciplina);

        List<Disciplina> novosPreRequisitos = processarPreRequisitos(dto, novaMatriz, matrizDisciplina);

        matrizDisciplina.atualizarInformacoes(dto, novaMatriz, novaDisciplina, novosPreRequisitos);
        return new MatrizDisciplinaResponseDTO(matrizDisciplina);
    }

    @Transactional
    public void excluir(Long id) {
        if (!matrizDisciplinaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Associação não encontrada!");
        }
        matrizDisciplinaRepository.deleteById(id);
    }

    private void validarPreRequisitosNaMatriz(MatrizCurricular matrizCurricular, List<Disciplina> preRequisitos) {
        for (Disciplina preRequisito : preRequisitos) {
            boolean existeNaMatriz = matrizDisciplinaRepository
                    .existsByMatrizCurricularIdAndDisciplinaId(matrizCurricular.getId(), preRequisito.getId());

            if (!existeNaMatriz) {
                throw new RecursoNaoEncontradoException("O pré-requisito " + preRequisito.getSigla() + " não está presente nesta matriz!");
            }
        }
    }

    private List<Disciplina> buscarPreRequisitos(List<Long> preRequisitosIds) {
        if (preRequisitosIds == null || preRequisitosIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Disciplina> preRequisitos = disciplinaRepository.findAllById(preRequisitosIds);

        if (preRequisitos.size() != preRequisitosIds.size()) {
            throw new RecursoNaoEncontradoException("Algum pré-requisito não foi encontrado!");
        }
        return preRequisitos;
    }

    private void validarDuplicidadeNaAtualizacao(MatrizDisciplinaAtualizacaoDTO dto, MatrizDisciplina matrizDisciplina) {
        Long matrizId = dto.matrizId() != null ? dto.matrizId() : matrizDisciplina.getMatrizCurricular().getId();
        Long disciplinaId = dto.disciplinaId() != null ? dto.disciplinaId() : matrizDisciplina.getDisciplina().getId();

        if (matrizDisciplinaRepository.existsByMatrizCurricularIdAndDisciplinaIdAndIdNot(matrizId, disciplinaId, matrizDisciplina.getId())) {
            throw new ValidacaoException("Já existe outra associação desta disciplina com esta matriz!");
        }
    }

    private List<Disciplina> processarPreRequisitos(MatrizDisciplinaAtualizacaoDTO dto, MatrizCurricular novaMatriz, MatrizDisciplina matrizDisciplina) {
        if (dto.preRequisitosIds() == null) {
            return null;
        }

        MatrizCurricular matrizReferencia = novaMatriz != null ? novaMatriz : matrizDisciplina.getMatrizCurricular();
        List<Disciplina> preRequisitos = buscarPreRequisitos(dto.preRequisitosIds());
        validarPreRequisitosNaMatriz(matrizReferencia, preRequisitos);

        return preRequisitos;
    }
}
