package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.Monitoria;
import com.controle.monitoria_api.model.enums.StatusMonitoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoriaRepository extends JpaRepository<Monitoria, Long> {

    boolean existsByAlunoIdAndDisciplinaIdAndSemestreAndStatusNot(
            Long alunoId,
            Long disciplinaId,
            String semestre,
            StatusMonitoria status
    );

    Page<Monitoria> findByProfessorId(Long professorId, Pageable pageable);

    Page<Monitoria> findByAlunoId(Long alunoId, Pageable pageable);

    Page<Monitoria> findByStatus(StatusMonitoria status, Pageable pageable);
}