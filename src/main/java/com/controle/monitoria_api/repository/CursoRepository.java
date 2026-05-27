package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Page<Curso> findAllByAtivoTrue(Pageable paginacao);
    Page<Curso> findAllByAtivoFalse(Pageable paginacao);
    Page<Curso> findByEscolaId(Long escolaId, Pageable paginacao);

    boolean existsBySigla(String sigla);
    boolean existsBySiglaAndIdNot(String sigla, Long id);
}
