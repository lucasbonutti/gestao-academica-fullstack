package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);
    boolean existsByMatriculaAndIdNot(String matricula, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Professor> findAllByAtivoTrue(Pageable paginacao);
    Page<Professor> findAllByAtivoFalse(Pageable paginacao);
    Page<Professor> findByEscolaId(Long escolaId, Pageable paginacao);
}
