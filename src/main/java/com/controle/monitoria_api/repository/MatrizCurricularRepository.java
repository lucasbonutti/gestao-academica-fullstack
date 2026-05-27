package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.MatrizCurricular;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatrizCurricularRepository extends JpaRepository<MatrizCurricular, Long> {

    Page<MatrizCurricular> findAllByAtivoTrue(Pageable paginacao);
    Page<MatrizCurricular> findAllByAtivoFalse(Pageable paginacao);
    Page<MatrizCurricular> findByCursoId(Long cursoId, Pageable paginacao);

    boolean existsByNomeAndCursoId(String nome, Long cursoId);
    boolean existsByNomeAndCursoIdAndIdNot(String nome, Long cursoId, Long id);

    Optional<MatrizCurricular> findByCursoIdAndAtivoTrue(Long cursoId);
}