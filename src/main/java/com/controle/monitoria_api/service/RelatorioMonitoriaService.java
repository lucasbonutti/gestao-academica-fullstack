package com.controle.monitoria_api.service;

import com.controle.monitoria_api.service.exceptions.RecursoNaoEncontradoException;
import com.controle.monitoria_api.service.exceptions.ValidacaoException;
import com.controle.monitoria_api.model.RelatorioMonitoria;
import com.controle.monitoria_api.model.dto.request.relatorioMonitoria.RelatorioMonitoriaCriacaoDTO;
import com.controle.monitoria_api.model.dto.response.RelatorioMonitoriaResponseDTO;
import com.controle.monitoria_api.repository.MonitoriaRepository;
import com.controle.monitoria_api.repository.RelatorioMonitoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelatorioMonitoriaService {

    private final RelatorioMonitoriaRepository relatorioRepository;
    private final MonitoriaRepository monitoriaRepository;

    @Transactional
    public RelatorioMonitoriaResponseDTO criar(RelatorioMonitoriaCriacaoDTO dto) {
        var monitoria = monitoriaRepository.findById(dto.monitoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Monitoria não encontrada!"));

        if (relatorioRepository.existsByMonitoriaId(dto.monitoriaId())) {
            throw new ValidacaoException("Já existe um relatório para esta monitoria!");
        }

        if (!monitoria.isFinalizada()) {
            throw new ValidacaoException("Só é possível gerar relatório para monitorias finalizadas!");
        }

        var relatorio = new RelatorioMonitoria(dto, monitoria);
        var salvar = relatorioRepository.save(relatorio);
        return new RelatorioMonitoriaResponseDTO(salvar);
    }

    public Page<RelatorioMonitoriaResponseDTO> listarTodos(Pageable paginacao) {
        return relatorioRepository.findAll(paginacao)
                .map(RelatorioMonitoriaResponseDTO::new);
    }

    public Page<RelatorioMonitoriaResponseDTO> listarPorProfessor(Long professorId, Pageable paginacao) {
        return relatorioRepository.findByMonitoriaProfessorId(professorId, paginacao)
                .map(RelatorioMonitoriaResponseDTO::new);
    }

    public Page<RelatorioMonitoriaResponseDTO> listarPorDisciplina(Long disciplinaId, Pageable paginacao) {
        return relatorioRepository.findByMonitoriaDisciplinaId(disciplinaId, paginacao)
                .map(RelatorioMonitoriaResponseDTO::new);
    }

    public Page<RelatorioMonitoriaResponseDTO> listarPorAluno(Long alunoId, Pageable paginacao) {
        return relatorioRepository.findByMonitoriaAlunoId(alunoId, paginacao)
                .map(RelatorioMonitoriaResponseDTO::new);
    }

    public Page<RelatorioMonitoriaResponseDTO> listarPorSemestre(String semestre, Pageable paginacao) {
        return relatorioRepository.findByMonitoriaSemestre(semestre, paginacao)
                .map(RelatorioMonitoriaResponseDTO::new);
    }

    public Page<RelatorioMonitoriaResponseDTO> listarPorStatusMonitoria(String status, Pageable paginacao) {
        return relatorioRepository.findByMonitoriaStatus(status, paginacao)
                .map(RelatorioMonitoriaResponseDTO::new);
    }

    public RelatorioMonitoriaResponseDTO listarPorMonitoria(Long monitoriaId) {
        RelatorioMonitoria relatorio = relatorioRepository.findByMonitoriaId(monitoriaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado para esta monitoria!"));
        return new RelatorioMonitoriaResponseDTO(relatorio);
    }

    public RelatorioMonitoriaResponseDTO listarPorId(Long id) {
        var relatorio = relatorioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado!"));
        return new RelatorioMonitoriaResponseDTO(relatorio);
    }
}