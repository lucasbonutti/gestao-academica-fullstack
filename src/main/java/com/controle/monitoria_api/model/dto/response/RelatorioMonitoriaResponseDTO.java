package com.controle.monitoria_api.model.dto.response;

import com.controle.monitoria_api.model.RelatorioMonitoria;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RelatorioMonitoriaResponseDTO", description = "DTO de resposta com os dados do relatório de monitoria")
public record RelatorioMonitoriaResponseDTO(
        Long id,
        MonitoriaResponseDTO monitoria,
        Integer numeroAlunosAtendidos,
        String ocorrencias,
        String parecerFinal)
{

    public RelatorioMonitoriaResponseDTO(RelatorioMonitoria relatorio) {
        this(relatorio.getId(), new MonitoriaResponseDTO(relatorio.getMonitoria()), relatorio.getNumeroAlunosAtendidos(), relatorio.getOcorrencias(), relatorio.getParecerFinal());
    }
}