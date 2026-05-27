package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.Escola;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "EscolaResponseDTO", description = "DTO de resposta com os dados de uma escola")
public record EscolaResponseDTO(
        Long id,
        String nome,
        String coordenador,
        IESResponseDTO ies,
        LocalDateTime dataCadastro,
        Boolean ativo) {

    public EscolaResponseDTO(Escola escola) {
        this(escola.getId(), escola.getNome(), escola.getCoordenador(), new IESResponseDTO(escola.getIes()), escola.getDataCadastro(), escola.getAtivo());
    }
}
