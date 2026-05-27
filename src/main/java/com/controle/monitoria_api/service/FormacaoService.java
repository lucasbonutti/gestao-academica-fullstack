package com.controle.monitoria_api.service;

import com.controle.monitoria_api.model.enums.Titulacao;
import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Formacao;
import com.controle.monitoria_api.model.dto.request.formacao.FormacaoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.formacao.FormacaoCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.FormacaoResponseDTO;
import com.controle.monitoria_api.repository.FormacaoRepository;
import com.controle.monitoria_api.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormacaoService {

    private final FormacaoRepository formacaoRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public FormacaoResponseDTO criar(FormacaoCriacaoDTO dto) {
        var professor = professorRepository.findById(dto.professorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado!"));

        if (formacaoRepository.existsByProfessorIdAndTitulacaoAndNomeCursoAndInstituicao(
                dto.professorId(), dto.titulacao(), dto.nomeCurso(), dto.instituicao())) {
            throw new ValidacaoException("Esta formação já está cadastrada para este professor!");
        }

        var formacao = new Formacao(dto, professor);
        var salvo = formacaoRepository.save(formacao);
        return new FormacaoResponseDTO(salvo);
    }

    public Page<FormacaoResponseDTO> listarTodos(Pageable paginacao) {
        return formacaoRepository.findAll(paginacao)
                .map(FormacaoResponseDTO::new);
    }

    public Page<FormacaoResponseDTO> listarPorProfessor(Long professorId, Pageable paginacao) {
        if (!professorRepository.existsById(professorId)) {
            throw new RecursoNaoEncontradoException("Professor não encontrado!");
        }

        return formacaoRepository.findByProfessorId(professorId, paginacao)
                .map(FormacaoResponseDTO::new);
    }

    public FormacaoResponseDTO listarPorId(Long id) {
        var formacao = formacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Formação não encontrada!"));
        return new FormacaoResponseDTO(formacao);
    }

    @Transactional
    public FormacaoResponseDTO atualizar(FormacaoAtualizacaoDTO dto) {
        var formacao = formacaoRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Formação não encontrada!"));

        validarDuplicidadeNaAtualizacao(dto, formacao);

        formacao.atualizarInformacoes(dto);
        return new FormacaoResponseDTO(formacao);
    }

    @Transactional
    public void excluir(Long id) {
        if (!formacaoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Formação não encontrada!");
        }
        formacaoRepository.deleteById(id);
    }

    private void validarDuplicidadeNaAtualizacao(FormacaoAtualizacaoDTO dto, Formacao formacao) {
        Titulacao titulacaoFinal = dto.titulacao() != null ? dto.titulacao() : formacao.getTitulacao();
        String nomeCursoFinal = dto.nomeCurso() != null ? dto.nomeCurso() : formacao.getNomeCurso();
        String instituicaoFinal = dto.instituicao() != null ? dto.instituicao() : formacao.getInstituicao();

        if (formacaoRepository.existsByProfessorIdAndTitulacaoAndNomeCursoAndInstituicaoAndIdNot(
                formacao.getProfessor().getId(), titulacaoFinal, nomeCursoFinal, instituicaoFinal, formacao.getId())) {
            throw new ValidacaoException("Esta formação já está cadastrada para este professor!");
        }
    }
}