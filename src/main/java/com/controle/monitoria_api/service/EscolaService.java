package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.Escola;
import com.controle.monitoria_api.model.IES;
import com.controle.monitoria_api.model.dto.request.escola.EscolaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.escola.EscolaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.EscolaResponseDTO;
import com.controle.monitoria_api.repository.EscolaRepository;
import com.controle.monitoria_api.repository.IESRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EscolaService {

    private final EscolaRepository escolaRepository;

    private final IESRepository iesRepository;

    @Transactional
    public EscolaResponseDTO criar(EscolaCriacaoDTO dto) {
        var ies = iesRepository.findById(dto.iesId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("IES não encontrada!"));

        if (escolaRepository.existsByNomeAndIesId(dto.nome(), dto.iesId())) {
            throw new ValidacaoException("Já existe uma escola com este nome nessa IES!");
        }

        var escola = new Escola(dto, ies);
        var salvar = escolaRepository.save(escola);
        return new EscolaResponseDTO(salvar);
    }

    public Page<EscolaResponseDTO> listarTodos(Pageable paginacao) {
        return escolaRepository.findAll(paginacao)
                .map(EscolaResponseDTO::new);
    }

    public Page<EscolaResponseDTO> listarAtivos(Pageable paginacao) {
        return escolaRepository.findAllByAtivoTrue(paginacao)
                .map(EscolaResponseDTO::new);
    }

    public Page<EscolaResponseDTO> listarInativos(Pageable paginacao) {
        return escolaRepository.findAllByAtivoFalse(paginacao)
                .map(EscolaResponseDTO::new);
    }

    public Page<EscolaResponseDTO> listarPorIES(Long iesId, Pageable paginacao) {
        if (!iesRepository.existsById(iesId)) {
            throw new RecursoNaoEncontradoException("IES não encontrada!");
        }

        return escolaRepository.findByIesId(iesId, paginacao)
                .map(EscolaResponseDTO::new);
    }

    public EscolaResponseDTO listarPorId(Long id) {
        var escola = escolaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));
        return new EscolaResponseDTO(escola);
    }

    @Transactional
    public EscolaResponseDTO atualizar(EscolaAtualizacaoDTO dto) {
        var escola = escolaRepository.findById(dto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));

        IES novaIes = null;
        if (dto.iesId() != null) {
            novaIes = iesRepository.findById(dto.iesId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("IES não encontrada!"));
        }

        validarNomeUnicoNaAtualizacao(dto, escola);

        escola.atualizarInformacoes(dto, novaIes);
        return new EscolaResponseDTO(escola);
    }

    @Transactional
    public void inativar(Long id) {
        var escola = escolaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));
        escola.inativar();
        escolaRepository.save(escola);
    }

    @Transactional
    public void ativar(Long id) {
        var escola = escolaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escola não encontrada!"));
        escola.ativar();
        escolaRepository.save(escola);
    }

    private void validarNomeUnicoNaAtualizacao(EscolaAtualizacaoDTO dto, Escola escola) {
        String nomeFinal = dto.nome() != null ? dto.nome() : escola.getNome();
        Long iesIdFinal = dto.iesId() != null ? dto.iesId() : escola.getIes().getId();

        if (escolaRepository.existsByNomeAndIesIdAndIdNot(nomeFinal, iesIdFinal, escola.getId())) {
            throw new ValidacaoException("Já existe outra escola com este nome nesta IES!");
        }
    }
}
