package com.controle.monitoria_api.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(name = "ValidationError", description = "Estrutura de erro para validação de campos")
public class ValidationError extends StandardError {

    private List<FieldMessage> erros = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public void addErro(String fieldName, String message) {
        erros.add(new FieldMessage(fieldName, message));
    }
}
