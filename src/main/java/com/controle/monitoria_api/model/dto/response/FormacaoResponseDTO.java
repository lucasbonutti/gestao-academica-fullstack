package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.Formacao;
import com.controle.monitoria_api.model.enums.Titulacao;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FormacaoResponseDTO", description = "DTO de resposta com os dados de uma formação acadêmica")
public record FormacaoResponseDTO(
        Long id,
        Long professorId,
        String professorNome,
        Titulacao titulacao,
        String instituicao,
        String nomeCurso,
        Integer anoConclusao
) {

    public FormacaoResponseDTO(Formacao formacao) {
        this(formacao.getId(), formacao.getProfessor().getId(), formacao.getProfessor().getNomeCompleto(), formacao.getTitulacao(), formacao.getInstituicao(), formacao.getNomeCurso(), formacao.getAnoConclusao());
    }
}