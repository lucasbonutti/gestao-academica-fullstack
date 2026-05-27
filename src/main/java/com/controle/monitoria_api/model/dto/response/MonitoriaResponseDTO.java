package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.Monitoria;
import com.controle.monitoria_api.model.enums.StatusMonitoria;
import com.controle.monitoria_api.model.enums.TipoMonitoria;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "MonitoriaResponseDTO", description = "DTO de resposta com os dados de uma monitoria")
public record MonitoriaResponseDTO(
        Long id,
        AlunoResponseDTO aluno,
        DisciplinaResponseDTO disciplina,
        ProfessorResponseDTO professor,
        String semestre,
        TipoMonitoria tipoMonitoria,
        String local,
        LocalDate dataInicio,
        LocalDate dataFim,
        LocalDateTime dataCadastro,
        StatusMonitoria status) {

    public MonitoriaResponseDTO(Monitoria monitoria) {
        this(
                monitoria.getId(),
                new AlunoResponseDTO(monitoria.getAluno()),
                new DisciplinaResponseDTO(monitoria.getDisciplina()),
                        new ProfessorResponseDTO(monitoria.getProfessor()),
                                monitoria.getSemestre(),
                                monitoria.getTipoMonitoria(),
                                monitoria.getLocal(),
                                monitoria.getDataInicio(),
                                monitoria.getDataFim(),
                                monitoria.getDataCadastro(),
                                monitoria.getStatus()
        );
    }
}