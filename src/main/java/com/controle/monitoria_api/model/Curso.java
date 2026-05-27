package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.curso.CursoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.curso.CursoCriacaoDTO;
import com.controle.monitoria_api.model.enums.TurnoCurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String sigla;

    @Column(nullable = false, length = 200)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TurnoCurso turno;

    @Column(nullable = false, length = 100)
    private String coordenador;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    public Curso(CursoCriacaoDTO dto, Escola escola) {
        this.sigla = dto.sigla();
        this.descricao = dto.descricao();
        this.escola = escola;
        this.turno = dto.turno();
        this.coordenador = dto.coordenador();
        this.ativo = true;
    }

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public void atualizarInformacoes(CursoAtualizacaoDTO dto, Escola escola) {
        if (dto.sigla() != null) {
            this.sigla = dto.sigla();
        }
        if (dto.descricao() != null) {
            this.descricao = dto.descricao();
        }
        if (dto.escolaId() != null && escola != null) {
            this.escola = escola;
        }
        if (dto.turno() != null) {
            this.turno = dto.turno();
        }
        if (dto.coordenador() != null) {
            this.coordenador = dto.coordenador();
        }
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
}
