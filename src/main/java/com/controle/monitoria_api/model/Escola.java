package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.escola.EscolaAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.escola.EscolaCriacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "escolas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Escola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String coordenador;

    @ManyToOne
    @JoinColumn(name = "ies_id", nullable = false)
    private IES ies;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    public Escola(EscolaCriacaoDTO dto, IES ies) {
        this.nome = dto.nome();
        this.coordenador = dto.coordenador();
        this.ies = ies;
        this.ativo = true;
    }

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }
    public void atualizarInformacoes(EscolaAtualizacaoDTO dto, IES ies) {
        if (dto.nome() != null) {
            this.nome = dto.nome();
        }
        if (dto.coordenador() != null) {
            this.coordenador = dto.coordenador();
        }
        if (dto.iesId() != null && ies != null) {
            this.ies = ies;
        }
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
}
