package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.matrizDisciplina.MatrizDisciplinaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.matrizDisciplina.MatrizDisciplinaCriacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matrizes_disciplinas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"matriz_curricular_id", "disciplina_id"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class MatrizDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matriz_curricular_id", nullable = false)
    private MatrizCurricular matrizCurricular;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToMany
    @JoinTable(
            name = "matriz_disciplina_pre_requisitos",
            joinColumns = @JoinColumn(name = "matriz_disciplina_id"),
            inverseJoinColumns = @JoinColumn(name = "pre_requisitos_id")
    )
    private List<Disciplina> preRequisitos = new ArrayList<>();

    public MatrizDisciplina(MatrizDisciplinaCriacaoDTO dto, MatrizCurricular matrizCurricular, Disciplina disciplina, List<Disciplina> preRequisitos) {
        this.matrizCurricular = matrizCurricular;
        this.disciplina = disciplina;
        if (preRequisitos != null) {
            this.preRequisitos = preRequisitos;
        }
    }

    public MatrizDisciplina(MatrizDisciplinaAtualizacaoDTO dto, MatrizCurricular matrizCurricular, Disciplina disciplina, List<Disciplina> preRequisitos) {
        this.matrizCurricular = matrizCurricular;
        this.disciplina = disciplina;
        if (preRequisitos != null) {
            this.preRequisitos = preRequisitos;
        }
    }

    public void atualizarInformacoes(MatrizDisciplinaAtualizacaoDTO dto, MatrizCurricular matriz, Disciplina disciplina, List<Disciplina> preRequisitos) {
        if (dto.matrizId() != null && matriz != null) {
            this.matrizCurricular = matriz;
        }
        if (dto.disciplinaId() != null && disciplina != null) {
            this.disciplina = disciplina;
        }
        if (preRequisitos != null) {
            this.preRequisitos.clear();
            this.preRequisitos.addAll(preRequisitos);
        }
    }
}