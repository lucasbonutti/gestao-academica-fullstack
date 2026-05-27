package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.IES;
import com.controle.monitoria_api.model.dto.request.ies.IESAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.ies.IESCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.IESResponseDTO;
import com.controle.monitoria_api.repository.IESRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IESService {

    private final IESRepository repository;

    @Transactional
    public IESResponseDTO criar(IESCriacaoDTO dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new ValidacaoException("IES já cadastrada com este nome!");
        }

        var ies = new IES(dto);
        var salvar = repository.save(ies);
        return new IESResponseDTO(salvar);
    }

    public Page<IESResponseDTO> listarTodos(Pageable paginacao) {
        return repository.findAll(paginacao)
                .map(IESResponseDTO::new);
    }

    public IESResponseDTO listarPorId(Long id) {
        var ies = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("IES não encontrada!"));
        return new IESResponseDTO(ies);
    }

    @Transactional
    public IESResponseDTO atualizar(IESAtualizacaoDTO dto) {
        var ies = repository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("IES não encontrada!"));

        ies.atualizarInformacoes(dto);
        return new IESResponseDTO(ies);
    }

    @Transactional
    public void excluir(Long id) {
        var ies = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("IES não encontrada!"));

        if (!ies.getEscolas().isEmpty()) {
            throw new ValidacaoException("Não é possível excluir IES que possui escolas vinculadas!");
        }
        repository.deleteById(id);
    }
}
