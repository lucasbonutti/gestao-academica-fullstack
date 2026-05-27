package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.MatrizDisciplina;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(name = "MatrizDisciplinaResponseDTO", description = "DTO de resposta com os dados da associação entre matriz curricular e disciplina")
public record MatrizDisciplinaResponseDTO(
        Long id,
        MatrizCurricularResponseDTO matriz,
        DisciplinaResponseDTO disciplina,
        List<DisciplinaResponseDTO> preRequisitos) {

    public MatrizDisciplinaResponseDTO(MatrizDisciplina matrizDisciplina) {
        this(
                matrizDisciplina.getId(),
                new MatrizCurricularResponseDTO(matrizDisciplina.getMatrizCurricular()),
                new DisciplinaResponseDTO(matrizDisciplina.getDisciplina()),
                matrizDisciplina.getPreRequisitos().stream()
                        .map(DisciplinaResponseDTO::new)
                        .collect(Collectors.toList())
        );
    }
}
