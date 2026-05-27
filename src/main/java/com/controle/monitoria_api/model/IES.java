package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.ies.IESAtualizacaoDTO;
import com.controle.monitoria_api.model.dto.request.ies.IESCriacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class IES {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(length = 20)
    private String telefone;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "ies")
    private List<Escola> escolas = new ArrayList<>();

    public IES(IESCriacaoDTO dto) {
        this.nome = dto.nome();
        this.endereco = dto.endereco();
        this.telefone = dto.telefone();
    }

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public void atualizarInformacoes(IESAtualizacaoDTO dto) {
        if (dto.nome() != null) {
            this.nome = dto.nome();
        }
        if (dto.endereco() != null) {
            this.endereco = dto.endereco();
        }
        if (dto.telefone() != null) {
            this.telefone = dto.telefone();

        }
    }
}
