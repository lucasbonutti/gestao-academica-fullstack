import { MonitoriaResponse } from './monitoria.model';

export interface RelatorioMonitoriaResponse {
  id: number;
  monitoria: MonitoriaResponse;
  numeroAlunosAtendidos: number;
  ocorrencias: string;
  parecerFinal: string;
}

export interface RelatorioMonitoriaCriacaoRequest {
  monitoriaId: number;
  numeroAlunosAtendidos: number;
  ocorrencias?: string;
  parecerFinal: string;
}