package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.matrizCurricular.MatrizCurricularAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.matrizCurricular.MatrizCurricularCriacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matrizes_curriculares")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class MatrizCurricular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 200)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "matrizCurricular")
    private List<MatrizDisciplina> matrizDisciplinas = new ArrayList<>();

    public MatrizCurricular(MatrizCurricularCriacaoDTO dto, Curso curso) {
        this.nome = dto.nome();
        this.descricao = dto.descricao();
        this.curso = curso;
        this.ativo = true;
    }

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public void atualizarInformacoes(MatrizCurricularAtualizacaoDTO dto, Curso curso) {
        if (dto.nome() != null) {
            this.nome = dto.nome();
        }
        if (dto.descricao() != null) {
            this.descricao = dto.descricao();
        }
        if (dto.cursoId() != null && curso != null) {
            this.curso = curso;
        }
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
}