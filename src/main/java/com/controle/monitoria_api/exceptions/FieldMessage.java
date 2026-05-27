package com.controle.monitoria_api.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FieldMessage", description = "Mensagem de erro para um campo específico")
public record FieldMessage(
        String fieldName,
        String message) {
}
