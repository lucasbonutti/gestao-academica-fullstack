package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.formacao.FormacaoCriacaoDTO;
import com.controle.monitoria_api.model.dto.request.formacao.FormacaoAtualizacaoDTO;
import com.controle.monitoria_api.model.enums.Titulacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "formacoes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Formacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Titulacao titulacao;

    @Column(nullable = false, length = 150)
    private String instituicao;

    @Column(name = "nome_curso", nullable = false, length = 150)
    private String nomeCurso;

    @Column(name = "ano_conclusao", nullable = false)
    private Integer anoConclusao;

    public Formacao(FormacaoCriacaoDTO dto, Professor professor) {
        this.professor = professor;
        this.titulacao = dto.titulacao();
        this.instituicao = dto.instituicao();
        this.nomeCurso = dto.nomeCurso();
        this.anoConclusao = dto.anoConclusao();
    }

    public void atualizarInformacoes(FormacaoAtualizacaoDTO dto) {
        if (dto.titulacao() != null) {
            this.titulacao = dto.titulacao();
        }
        if (dto.instituicao() != null) {
            this.instituicao = dto.instituicao();
        }
        if (dto.nomeCurso() != null) {
            this.nomeCurso = dto.nomeCurso();
        }
        if (dto.anoConclusao() != null) {
            this.anoConclusao = dto.anoConclusao();
        }
    }
}