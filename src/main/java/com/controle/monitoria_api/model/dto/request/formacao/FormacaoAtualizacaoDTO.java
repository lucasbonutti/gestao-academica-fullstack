package com.controle.monitoria_api.model.dto.request.formacao;

import com.controle.monitoria_api.model.enums.Titulacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "FormacaoAtualizacaoDTO", description = "DTO para atualização de uma formação acadêmica")
public record FormacaoAtualizacaoDTO(

        @NotNull(message = "ID é obrigatório!")
        Long id,

        Titulacao titulacao,

        @Size(max = 150, message = "Instituição deve ter no máximo 150 caracteres!")
        String instituicao,

        @Size(max = 150, message = "Nome do curso deve ter no máximo 150 caracteres!")
        String nomeCurso,

        @Min(value = 1900, message = "Ano de conclusão inválido!")
        @Max(value = 2100, message = "Ano de conclusão inválido!")
        Integer anoConclusao
) {}