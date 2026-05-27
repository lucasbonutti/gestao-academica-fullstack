package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.relatorioMonitoria.RelatorioMonitoriaCriacaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "relatorios_monitoria")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class RelatorioMonitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "monitoria_id", nullable = false, unique = true)
    private Monitoria monitoria;

    @Column(name = "numero_alunos_atendidos", nullable = false)
    private Integer numeroAlunosAtendidos;

    @Column(length = 500)
    private String ocorrencias;

    @Column(name = "parecer_final", nullable = false, length = 500)
    private String parecerFinal;

    public RelatorioMonitoria(RelatorioMonitoriaCriacaoDTO dto, Monitoria monitoria) {
        this.monitoria = monitoria;
        this.numeroAlunosAtendidos = dto.numeroAlunosAtendidos();
        this.ocorrencias = dto.ocorrencias();
        this.parecerFinal = dto.parecerFinal();
    }
}