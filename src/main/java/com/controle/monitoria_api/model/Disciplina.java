package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.disciplina.DisciplinaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.disciplina.DisciplinaCriacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "disciplinas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String sigla;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    public Disciplina(DisciplinaCriacaoDTO dto, Escola escola) {
        this.sigla = dto.sigla();
        this.descricao = dto.descricao();
        this.cargaHoraria = dto.cargaHoraria();
        this.escola = escola;
        this.ativo = true;
    }

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public void atualizarInformacoes(DisciplinaAtualizacaoDTO dto, Escola escola) {
        if (dto.sigla() != null) {
            this.sigla = dto.sigla();
        }
        if (dto.descricao() != null) {
            this.descricao = dto.descricao();
        }
        if (dto.cargaHoraria() != null) {
            this.cargaHoraria = dto.cargaHoraria();
        }
        if (dto.escolaId() != null && escola != null) {
            this.escola = escola;
        }
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
}
