package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Disciplina;
import com.controle.monitoria_api.model.Escola;
import com.controle.monitoria_api.model.dto.request.disciplina.DisciplinaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.disciplina.DisciplinaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.DisciplinaResponseDTO;
import com.controle.monitoria_api.repository.DisciplinaRepository;
import com.controle.monitoria_api.repository.EscolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;

    private final EscolaRepository escolaRepository;

    @Transactional
    public DisciplinaResponseDTO criar(DisciplinaCriacaoDTO dto) {
        var escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));

        if (disciplinaRepository.existsBySigla(dto.sigla())) {
            throw new ValidacaoException("Já existe uma disciplina com esta sigla!");
        }

        var disciplina = new Disciplina(dto, escola);
        var salvar = disciplinaRepository.save(disciplina);
        return new DisciplinaResponseDTO(salvar);
    }

    public Page<DisciplinaResponseDTO> listarTodos(Pageable paginacao) {
        return disciplinaRepository.findAll(paginacao)
                .map(DisciplinaResponseDTO::new);
    }

    public Page<DisciplinaResponseDTO> listarAtivos(Pageable paginacao) {
        return disciplinaRepository.findAllByAtivoTrue(paginacao)
                .map(DisciplinaResponseDTO::new);
    }

    public Page<DisciplinaResponseDTO> listarInativos(Pageable paginacao) {
        return disciplinaRepository.findAllByAtivoFalse(paginacao)
                .map(DisciplinaResponseDTO::new);
    }

    public Page<DisciplinaResponseDTO> listarPorEscola(Long escolaId, Pageable paginacao) {
        if (!escolaRepository.existsById(escolaId)) {
            throw new RecursoNaoEncontradoException("Escola não encontrada!");
        }
        return disciplinaRepository.findByEscolaId(escolaId, paginacao)
                .map(DisciplinaResponseDTO::new);
    }

    public DisciplinaResponseDTO listarPorId(Long id) {
        var disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada!"));
        return new DisciplinaResponseDTO(disciplina);
    }

    @Transactional
    public DisciplinaResponseDTO atualizar(DisciplinaAtualizacaoDTO dto) {
        var disciplina = disciplinaRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada!"));

        Escola novaEscola = null;
        if (dto.escolaId() != null) {
            novaEscola = escolaRepository.findById(dto.escolaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));
        }

        validarSiglaUnicaNaAtualizacao(dto, disciplina);

        disciplina.atualizarInformacoes(dto, novaEscola);
        return new DisciplinaResponseDTO(disciplina);
    }

    @Transactional
    public void inativar(Long id) {
        var disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada!"));
        disciplina.inativar();
        disciplinaRepository.save(disciplina);
    }

    @Transactional
    public void ativar(Long id) {
        var disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina não encontrada!"));
        disciplina.ativar();
        disciplinaRepository.save(disciplina);
    }

    private void validarSiglaUnicaNaAtualizacao(DisciplinaAtualizacaoDTO dto, Disciplina disciplina) {
        String siglaFinal = dto.sigla() != null ? dto.sigla() : disciplina.getSigla();

        if (disciplinaRepository.existsBySiglaAndIdNot(siglaFinal, disciplina.getId())) {
            throw new ValidacaoException("Já existe outra disciplina com esta sigla!");
        }
    }
}
