package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.MatrizDisciplina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatrizDisciplinaRepository extends JpaRepository<MatrizDisciplina, Long> {

    boolean existsByMatrizCurricularIdAndDisciplinaId(Long matrizId, Long disciplinaId);
    boolean existsByMatrizCurricularIdAndDisciplinaIdAndIdNot(Long matrizId, Long disciplinaId, Long id);

    Page<MatrizDisciplina> findByMatrizCurricularId(Long matrizId, Pageable paginacao);
    Page<MatrizDisciplina> findByDisciplinaId(Long disciplinaId, Pageable paginacao);
}
