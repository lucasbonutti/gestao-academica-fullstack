package com.controle.monitoria_api.model.dto.request.escola;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "EscolaCriacaoDTO", description = "DTO para cadastro de uma nova escola")
public record EscolaCriacaoDTO(
        @NotBlank(message = "Nome da escola é obrigatório!")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,
        @NotBlank(message = "Coordenador é obrigatório!")
        @Size(max = 100, message = "Coordenador deve ter no máximo 100 caracteres")
        String coordenador,
        @NotNull(message = "IES é obrigatória!")
        Long iesId) {
}
