package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.IES;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "IESResponseDTO", description = "DTO de resposta com os dados de uma Instituição de Ensino Superior (IES)")
public record IESResponseDTO(
        Long id,
        String nome,
        String endereco,
        String telefone,
        LocalDateTime dataCadastro) {

    public IESResponseDTO(IES ies) {
        this(ies.getId(), ies.getNome(), ies.getEndereco(), ies.getTelefone(), ies.getDataCadastro());
    }
}
