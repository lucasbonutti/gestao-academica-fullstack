package com.controle.monitoria_api.model;

import com.controle.monitoria_api.model.dto.request.monitoria.MonitoriaCriacaoDTO;
import com.controle.monitoria_api.model.dto.request.monitoria.MonitoriaAtualizacaoDTO;
import com.controle.monitoria_api.model.enums.StatusMonitoria;
import com.controle.monitoria_api.model.enums.TipoMonitoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitorias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Monitoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @Column(nullable = false, length = 20)
    private String semestre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_monitoria", nullable = false, length = 20)
    private TipoMonitoria tipoMonitoria;

    @Column(nullable = false, length = 200)
    private String local;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMonitoria status;

    @OneToOne(mappedBy = "monitoria", cascade = CascadeType.ALL)
    private RelatorioMonitoria relatorio;

    public Monitoria(MonitoriaCriacaoDTO dto, Aluno aluno, Disciplina disciplina, Professor professor) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.professor = professor;
        this.semestre = dto.semestre();
        this.tipoMonitoria = dto.tipoMonitoria();
        this.local = dto.local();
        this.dataInicio = dto.dataInicio();
        this.dataFim = dto.dataFim();
        this.status = StatusMonitoria.EM_ANDAMENTO;
    }

    @PrePersist
    public void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }

    public void atualizarInformacoes(MonitoriaAtualizacaoDTO dto, Aluno aluno, Disciplina disciplina, Professor professor) {
        if (dto.alunoId() != null && aluno != null) {
            this.aluno = aluno;
        }
        if (dto.disciplinaId() != null && disciplina != null) {
            this.disciplina = disciplina;
        }
        if (dto.professorId() != null && professor != null) {
            this.professor = professor;
        }
        if (dto.semestre() != null) {
            this.semestre = dto.semestre();
        }
        if (dto.tipoMonitoria() != null) {
            this.tipoMonitoria = dto.tipoMonitoria();
        }
        if (dto.local() != null) {
            this.local = dto.local();
        }
        if (dto.dataInicio() != null) {
            this.dataInicio = dto.dataInicio();
        }
        if (dto.dataFim() != null) {
            this.dataFim = dto.dataFim();
        }
    }

    public void finalizar() {
        this.status = StatusMonitoria.FINALIZADA;
    }

    public boolean isEmAndamento() {
        return StatusMonitoria.EM_ANDAMENTO.equals(this.status);
    }

    public boolean isFinalizada() {
        return StatusMonitoria.FINALIZADA.equals(this.status);
    }
}