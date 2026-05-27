package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Curso;
import com.controle.monitoria_api.model.Escola;
import com.controle.monitoria_api.model.dto.request.curso.CursoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.curso.CursoCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.CursoResponseDTO;
import com.controle.monitoria_api.repository.CursoRepository;
import com.controle.monitoria_api.repository.EscolaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    private final EscolaRepository escolaRepository;

    @Transactional
    public CursoResponseDTO criar(CursoCriacaoDTO dto) {
        var escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));

        if (cursoRepository.existsBySigla(dto.sigla())) {
            throw new ValidacaoException("Já existe um curso com esta sigla!");
        }

        var curso = new Curso(dto, escola);
        var salvar = cursoRepository.save(curso);
        return new CursoResponseDTO(salvar);
    }

    public Page<CursoResponseDTO> listarTodos(Pageable paginacao) {
        return cursoRepository.findAll(paginacao)
                .map(CursoResponseDTO::new);
    }

    public Page<CursoResponseDTO> listarAtivos(Pageable paginacao) {
        return cursoRepository.findAllByAtivoTrue(paginacao)
                .map(CursoResponseDTO::new);
    }

    public Page<CursoResponseDTO> listarInativos(Pageable paginacao) {
        return cursoRepository.findAllByAtivoFalse(paginacao)
                .map(CursoResponseDTO::new);
    }

    public Page<CursoResponseDTO> listarPorEscola(Long escolaId, Pageable paginacao) {
        if (!escolaRepository.existsById(escolaId)) {
            throw new RecursoNaoEncontradoException("Escola não encontrada!");
        }

        return cursoRepository.findByEscolaId(escolaId, paginacao)
                .map(CursoResponseDTO::new);
    }

    public CursoResponseDTO listarPorId(Long id) {
        var curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado!"));
        return new CursoResponseDTO(curso);
    }

    @Transactional
    public CursoResponseDTO atualizar(CursoAtualizacaoDTO dto) {
        var curso = cursoRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado!"));

        Escola novaEscola = null;
        if (dto.escolaId() != null) {
            novaEscola = escolaRepository.findById(dto.escolaId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));
        }

        validarSiglaUnicaNaAtualizacao(dto, curso);

        curso.atualizarInformacoes(dto, novaEscola);
        return new CursoResponseDTO(curso);
    }

    @Transactional
    public void inativar(Long id) {
        var curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado!"));
        curso.inativar();
        cursoRepository.save(curso);
    }

    @Transactional
    public void ativar(Long id) {
        var curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado!"));
        curso.ativar();
        cursoRepository.save(curso);
    }

    private void validarSiglaUnicaNaAtualizacao(CursoAtualizacaoDTO dto, Curso curso) {
        String siglaFinal = dto.sigla() != null ? dto.sigla() : curso.getSigla();

        if (cursoRepository.existsBySiglaAndIdNot(siglaFinal, curso.getId())) {
            throw new ValidacaoException("Já existe outro curso com esta sigla!");
        }
    }
}
