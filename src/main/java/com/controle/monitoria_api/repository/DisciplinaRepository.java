package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.Disciplina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    Page<Disciplina> findAllByAtivoTrue(Pageable paginacao);
    Page<Disciplina> findAllByAtivoFalse(Pageable paginacao);
    Page<Disciplina> findByEscolaId(Long escolaId, Pageable paginacao);

    boolean existsBySigla(String sigla);
    boolean existsBySiglaAndIdNot(String sigla, Long id);
}
