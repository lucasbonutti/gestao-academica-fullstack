package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Aluno;
import com.controle.monitoria_api.model.dto.request.aluno.AlunoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.aluno.AlunoCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.AlunoResponseDTO;
import com.controle.monitoria_api.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository repository;

    @Transactional
    public AlunoResponseDTO criar(AlunoCriacaoDTO dto) {
        if (repository.existsByMatricula(dto.matricula())) {
            throw new ValidacaoException("Já existe um aluno com esta matrícula!");
        }

        var aluno = new Aluno(dto);
        var salvar = repository.save(aluno);
        return new AlunoResponseDTO(salvar);
    }

    public Page<AlunoResponseDTO> listarTodos(Pageable paginacao) {
        return repository.findAll(paginacao)
                .map(AlunoResponseDTO::new);
    }

    public Page<AlunoResponseDTO> listarAtivos(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao)
                .map(AlunoResponseDTO::new);
    }

    public Page<AlunoResponseDTO> listarInativos(Pageable paginacao) {
        return repository.findAllByAtivoFalse(paginacao)
                .map(AlunoResponseDTO::new);
    }

    public AlunoResponseDTO listarPorId(Long id) {
        var aluno = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado!"));
        return new AlunoResponseDTO(aluno);
    }

    @Transactional
    public AlunoResponseDTO atualizar(AlunoAtualizacaoDTO dto) {
        var aluno = repository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado!"));

        if (dto.matricula() != null && !dto.matricula().equals(aluno.getMatricula())) {
            if (repository.existsByMatricula(dto.matricula())) {
                throw new ValidacaoException("Já existe outro aluno com esta matrícula!");
            }
        }

        aluno.atualizarInformacoes(dto);
        return new AlunoResponseDTO(aluno);
    }

    @Transactional
    public void inativar(Long id) {
        var aluno = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado!"));
        aluno.inativar();
        repository.save(aluno);
    }

    @Transactional
    public void ativar(Long id) {
        var aluno = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado!"));
        aluno.ativar();
        repository.save(aluno);
    }
}