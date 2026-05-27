package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.Escola;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EscolaRepository extends JpaRepository<Escola, Long> {

    boolean existsByNomeAndIesId(String nome, Long iesId);
    boolean existsByNomeAndIesIdAndIdNot(String nome, Long iesId, Long id);

    Page<Escola> findAllByAtivoTrue(Pageable paginacao);
    Page<Escola> findAllByAtivoFalse(Pageable paginacao);
    Page<Escola> findByIesId(Long iesId, Pageable paginacao);
}
