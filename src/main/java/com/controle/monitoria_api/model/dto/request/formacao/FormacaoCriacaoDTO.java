package com.controle.monitoria_api.model.dto.request.formacao;

import com.controle.monitoria_api.model.enums.Titulacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "FormacaoCriacaoDTO", description = "DTO para cadastro de uma formação acadêmica de professor")
public record FormacaoCriacaoDTO(

        @NotNull(message = "Professor é obrigatório!")
        Long professorId,

        @NotNull(message = "Titulação é obrigatória!")
        Titulacao titulacao,

        @NotBlank(message = "Instituição é obrigatória!")
        @Size(max = 150, message = "Instituição deve ter no máximo 150 caracteres!")
        String instituicao,

        @NotBlank(message = "Nome do curso é obrigatório!")
        @Size(max = 150, message = "Nome do curso deve ter no máximo 150 caracteres!")
        String nomeCurso,

        @NotNull(message = "Ano de conclusão é obrigatório!")
        @Min(value = 1900, message = "Ano de conclusão inválido!")
        @Max(value = 2100, message = "Ano de conclusão inválido!")
        Integer anoConclusao
) {}