package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.Aluno;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "AlunoResponseDTO", description = "DTO de resposta com os dados de um aluno")
public record AlunoResponseDTO(
        Long id,
        String matricula,
        String nomeCompleto,
        LocalDateTime dataCadastro,
        Boolean ativo
) {
    public AlunoResponseDTO(Aluno aluno) {
        this(aluno.getId(), aluno.getMatricula(), aluno.getNomeCompleto(), aluno.getDataCadastro(), aluno.getAtivo());
    }
}