import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { RelatoriosMonitoriaService } from '../services/relatorios-monitoria.service';
import { MonitoriasService } from '../../monitorias/services/monitorias.service';
import { MonitoriaResponse } from '../../../core/models/monitoria.model';

@Component({
  selector: 'app-relatorio-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './relatorio-form.component.html',
  styleUrl: './relatorio-form.component.css'
})
export class RelatorioFormComponent implements OnInit {
  monitoriaId = '';
  numeroAlunosAtendidos = 1;
  ocorrencias = '';
  parecerFinal = '';

  monitorias: MonitoriaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private relatoriosService: RelatoriosMonitoriaService,
    private monitoriasService: MonitoriasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarMonitoriasFinalizadas();
  }

  carregarMonitoriasFinalizadas(): void {
  this.monitoriasService.listarPorStatus('FINALIZADA', 0, 100).subscribe({
    next: (response) => {
      console.log('MONITORIAS FINALIZADAS:', response);
      this.monitorias = response.content;
    },
    error: (err: any) => {
      console.error('ERRO AO CARREGAR MONITORIAS FINALIZADAS:', err);
      this.errorMessage =
        err?.error?.message || 'Erro ao carregar monitorias finalizadas.';
    }
  });
}

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.relatoriosService.criar({
      monitoriaId: Number(this.monitoriaId),
      numeroAlunosAtendidos: Number(this.numeroAlunosAtendidos),
      ocorrencias: this.ocorrencias,
      parecerFinal: this.parecerFinal
    }).subscribe({
      next: () => {
        this.successMessage = 'Relatório cadastrado com sucesso.';
        this.loading = false;
        this.monitoriaId = '';
        this.numeroAlunosAtendidos = 1;
        this.ocorrencias = '';
        this.parecerFinal = '';
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar relatório.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}