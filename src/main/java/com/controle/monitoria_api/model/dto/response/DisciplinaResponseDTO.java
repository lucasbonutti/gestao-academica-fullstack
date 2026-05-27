package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.Disciplina;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "DisciplinaResponseDTO", description = "DTO de resposta com os dados de uma disciplina")
public record DisciplinaResponseDTO(
        Long id,
        String sigla,
        String descricao,
        Integer cargaHoraria,
        EscolaResponseDTO escola,
        LocalDateTime dataCadastro,
        Boolean ativo) {

    public DisciplinaResponseDTO(Disciplina disciplina) {
        this(disciplina.getId(), disciplina.getSigla(), disciplina.getDescricao(), disciplina.getCargaHoraria(), new EscolaResponseDTO(disciplina.getEscola()), disciplina.getDataCadastro(), disciplina.getAtivo());
    }
}
