package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Curso;
import com.controle.monitoria_api.model.MatrizCurricular;

import com.controle.monitoria_api.model.dto.request.matrizCurricular.MatrizCurricularCriacaoDTO;
import com.controle.monitoria_api.model.dto.request.matrizCurricular.MatrizCurricularAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.response.MatrizCurricularResponseDTO;
import com.controle.monitoria_api.repository.CursoRepository;
import com.controle.monitoria_api.repository.MatrizCurricularRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatrizCurricularService {

    private final MatrizCurricularRepository matrizRepository;
    private final CursoRepository cursoRepository;

    @Transactional
    public MatrizCurricularResponseDTO criar(MatrizCurricularCriacaoDTO dto) {
        var curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado!"));

        if (matrizRepository.existsByNomeAndCursoId(dto.nome(), dto.cursoId())) {
            throw new ValidacaoException("Já existe uma matriz com este nome para este curso!");
        }

        var matriz = new MatrizCurricular(dto, curso);
        var salva = matrizRepository.save(matriz);
        return new MatrizCurricularResponseDTO(salva);
    }

    public Page<MatrizCurricularResponseDTO> listarTodos(Pageable paginacao) {
        return matrizRepository.findAll(paginacao)
                .map(MatrizCurricularResponseDTO::new);
    }

    public Page<MatrizCurricularResponseDTO> listarAtivos(Pageable paginacao) {
        return matrizRepository.findAllByAtivoTrue(paginacao)
                .map(MatrizCurricularResponseDTO::new);
    }

    public Page<MatrizCurricularResponseDTO> listarInativos(Pageable paginacao) {
        return matrizRepository.findAllByAtivoFalse(paginacao)
                .map(MatrizCurricularResponseDTO::new);
    }

    public Page<MatrizCurricularResponseDTO> listarPorCurso(Long cursoId, Pageable paginacao) {
        if (!cursoRepository.existsById(cursoId)) {
            throw new RecursoNaoEncontradoException("Curso não encontrado!");
        }
        return matrizRepository.findByCursoId(cursoId, paginacao)
                .map(MatrizCurricularResponseDTO::new);
    }

    public MatrizCurricularResponseDTO listarPorId(Long id) {
        var matriz = matrizRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matriz não encontrada!"));
        return new MatrizCurricularResponseDTO(matriz);
    }

    public MatrizCurricularResponseDTO listarMatrizAtivaPorCurso(Long cursoId) {
        var matriz = matrizRepository.findByCursoIdAndAtivoTrue(cursoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma matriz ativa encontrada para este curso!"));
        return new MatrizCurricularResponseDTO(matriz);
    }

    @Transactional
    public MatrizCurricularResponseDTO atualizar(MatrizCurricularAtualizacaoDTO dto) {
        var matriz = matrizRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matriz não encontrada!"));

        Curso novoCurso = null;
        if (dto.cursoId() != null) {
            novoCurso = cursoRepository.findById(dto.cursoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado!"));
        }

        validarNomeUnicoNaAtualizacao(dto, matriz);

        matriz.atualizarInformacoes(dto, novoCurso);
        return new MatrizCurricularResponseDTO(matriz);
    }

    @Transactional
    public void ativar(Long id) {
        var matriz = matrizRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matriz não encontrada!"));

        validarNaoExisteOutraAtiva(matriz.getCurso().getId());

        matriz.ativar();
        matrizRepository.save(matriz);
    }

    @Transactional
    public void inativar(Long id) {
        var matriz = matrizRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matriz não encontrada!"));
        matriz.inativar();
        matrizRepository.save(matriz);
    }

    private void validarNomeUnicoNaAtualizacao(MatrizCurricularAtualizacaoDTO dto, MatrizCurricular matriz) {
        String nomeFinal = dto.nome() != null ? dto.nome() : matriz.getNome();
        Long cursoIdFinal = dto.cursoId() != null ? dto.cursoId() : matriz.getCurso().getId();

        if (matrizRepository.existsByNomeAndCursoIdAndIdNot(nomeFinal, cursoIdFinal, matriz.getId())) {
            throw new ValidacaoException("Já existe outra matriz com este nome para este curso!");
        }
    }

    private void validarNaoExisteOutraAtiva(Long cursoId) {
        if (matrizRepository.findByCursoIdAndAtivoTrue(cursoId).isPresent()) {
            throw new ValidacaoException("Já existe uma matriz ativa para este curso! Inative a atual antes de ativar esta.");
        }
    }
}