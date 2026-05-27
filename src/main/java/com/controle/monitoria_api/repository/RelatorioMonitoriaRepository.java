package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.RelatorioMonitoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelatorioMonitoriaRepository extends JpaRepository<RelatorioMonitoria, Long> {

    Optional<RelatorioMonitoria> findByMonitoriaId(Long monitoriaId);

    Page<RelatorioMonitoria> findByMonitoriaStatus(String status, Pageable paginacao);
    Page<RelatorioMonitoria> findByMonitoriaSemestre(String semestre, Pageable paginacao);
    Page<RelatorioMonitoria> findByMonitoriaAlunoId(Long alunoId, Pageable paginacao);
    Page<RelatorioMonitoria> findByMonitoriaDisciplinaId(Long disciplinaId, Pageable paginacao);
    Page<RelatorioMonitoria> findByMonitoriaProfessorId(Long professorId, Pageable paginacao);

    boolean existsByMonitoriaId(Long monitoriaId);
}