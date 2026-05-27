package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Page<Aluno> findAllByAtivoTrue(Pageable paginacao);
    Page<Aluno> findAllByAtivoFalse(Pageable paginacao);

    boolean existsByMatricula(String matricula);
}