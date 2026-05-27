package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.aluno.AlunoAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.aluno.AlunoCriacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alunos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String matricula;

    @Column(name = "nome_completo", nullable = false, length = 100)
    private String nomeCompleto;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Monitoria> monitorias = new ArrayList<>();

    public Aluno(AlunoCriacaoDTO dto) {
        this.matricula = dto.matricula();
        this.nomeCompleto = dto.nomeCompleto();
        this.ativo = true;
    }

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public void atualizarInformacoes(AlunoAtualizacaoDTO dto) {
        if (dto.matricula() != null) {
            this.matricula = dto.matricula();
        }
        if (dto.nomeCompleto() != null) {
            this.nomeCompleto = dto.nomeCompleto();
        }
    }

    public void inativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
}