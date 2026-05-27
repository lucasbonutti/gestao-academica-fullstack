package com.controle.monitoria_api.model.dto.request.ies;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "IESCriacaoDTO", description = "DTO para cadastro de uma Instituição de Ensino Superior")
public record IESCriacaoDTO(
        @NotBlank(message = "Nome é obrigatório!")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,
        @NotBlank(message = "Endereço é obrigatório!")
        @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
        String endereco,
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone) {
}
