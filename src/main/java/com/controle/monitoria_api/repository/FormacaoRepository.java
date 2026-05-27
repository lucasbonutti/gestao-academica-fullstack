package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.Formacao;
import com.controle.monitoria_api.model.enums.Titulacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormacaoRepository extends JpaRepository<Formacao, Long> {

    Page<Formacao> findByProfessorId(Long professorId, Pageable paginacao);

    boolean existsByProfessorIdAndTitulacaoAndNomeCursoAndInstituicao(Long professorId, Titulacao titulacao, String nomeCurso, String instituicao);
    boolean existsByProfessorIdAndTitulacaoAndNomeCursoAndInstituicaoAndIdNot(Long professorId, Titulacao titulacao, String nomeCurso, String instituicao, Long id);
}