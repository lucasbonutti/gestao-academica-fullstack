package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.professor.ProfessorAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.professor.ProfessorCriacaoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "professor")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String matricula;

    @Column(name = "nome_completo", nullable = false, length = 100)
    private String nomeCompleto;

    @Column(nullable = false, length = 100 ,unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "escola_id", nullable = false)
    private Escola escola;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(nullable = false)
    private Boolean ativo;

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Professor(ProfessorCriacaoDTO dto, Escola escola) {
        this.matricula = dto.matricula();
        this.nomeCompleto = dto.nomeCompleto();
        this.email = dto.email();
        this.telefone = dto.telefone();
        this.escola = escola;
        this.ativo = true;
    }

    public void atualizarInformacoes(ProfessorAtualizacaoDTO dto, Escola escola) {
        if (dto.matricula() != null) {
            this.matricula = dto.matricula();
        }
        if (dto.nomeCompleto() != null) {
            this.nomeCompleto = dto.nomeCompleto();
        }
        if (dto.email() != null) {
            this.email = dto.email();
        }
        if (dto.telefone() != null) {
            this.telefone = dto.telefone();
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
