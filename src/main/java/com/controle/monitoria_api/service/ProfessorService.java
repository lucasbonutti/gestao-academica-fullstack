package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Escola;
import com.controle.monitoria_api.model.Professor;
import com.controle.monitoria_api.model.dto.request.professor.ProfessorAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.professor.ProfessorCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.ProfessorResponseDTO;
import com.controle.monitoria_api.repository.EscolaRepository;
import com.controle.monitoria_api.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    private final EscolaRepository escolaRepository;

    @Transactional
    public ProfessorResponseDTO criar(ProfessorCriacaoDTO dto) {
        var escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));

        if (professorRepository.existsByMatricula(dto.matricula())) {
            throw new ValidacaoException("Já existe um professor com esta matrícula!");
        }
        if (professorRepository.existsByEmail(dto.email())) {
            throw new ValidacaoException("Já existe um professor com este email!");
        }

        var professor = new Professor(dto, escola);
        var salvar = professorRepository.save(professor);
        return new ProfessorResponseDTO(salvar);
    }

    public Page<ProfessorResponseDTO> listarTodos(Pageable paginacao) {
        return professorRepository.findAll(paginacao)
                .map(ProfessorResponseDTO::new);
    }

    public Page<ProfessorResponseDTO> listarAtivos(Pageable paginacao) {
        return professorRepository.findAllByAtivoTrue(paginacao)
                .map(ProfessorResponseDTO::new);
    }

    public Page<ProfessorResponseDTO> listarInativos(Pageable paginacao) {
        return professorRepository.findAllByAtivoFalse(paginacao)
                .map(ProfessorResponseDTO::new);
    }

    public Page<ProfessorResponseDTO> listarPorEscola(Long escolaId, Pageable paginacao) {
        if (!escolaRepository.existsById(escolaId)) {
            throw new RecursoNaoEncontradoException("Escola não encontrada!");
        }

        return professorRepository.findByEscolaId(escolaId, paginacao)
                .map(ProfessorResponseDTO::new);
    }

    public ProfessorResponseDTO listarPorId(Long id) {
        var professor = professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado(a)!"));
        return new ProfessorResponseDTO(professor);
    }

    @Transactional
    public ProfessorResponseDTO atualizar(ProfessorAtualizacaoDTO dto) {
        var professor = professorRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado(a)!"));

        Escola novaEscola = null;
        if (dto.escolaId() != null) {
            novaEscola = escolaRepository.findById(dto.escolaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));
        }

        validarMatriculaUnicaNaAtualizacao(dto, professor);
        validarEmailUnicoNaAtualizacao(dto, professor);

        professor.atualizarInformacoes(dto, novaEscola);
        return new ProfessorResponseDTO(professor);
    }

    @Transactional
    public void inativar(Long id) {
        var professor = professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado(a)!"));
        professor.inativar();
        professorRepository.save(professor);
    }

    @Transactional
    public void ativar(Long id) {
        var professor = professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado(a)!"));
        professor.ativar();
        professorRepository.save(professor);
    }

    private void validarMatriculaUnicaNaAtualizacao(ProfessorAtualizacaoDTO dto, Professor professor) {
        String matriculaFinal = dto.matricula() != null ? dto.matricula() : professor.getMatricula();

        if (professorRepository.existsByMatriculaAndIdNot(matriculaFinal, professor.getId())) {
            throw new ValidacaoException("Já existe outro professor com esta matrícula!");
        }
    }

    private void validarEmailUnicoNaAtualizacao(ProfessorAtualizacaoDTO dto, Professor professor) {
        String emailFinal = dto.email() != null ? dto.email() : professor.getEmail();

        if (professorRepository.existsByEmailAndIdNot(emailFinal, professor.getId())) {
            throw new ValidacaoException("Já existe outro professor com este email!");
        }
    }
}
