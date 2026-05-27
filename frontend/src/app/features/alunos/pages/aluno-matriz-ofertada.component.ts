import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MatrizPlannerComponent } from '../../../shared/components/matriz-planner.component';
import { DisciplinasService } from '../../disciplinas/services/disciplinas.service';
import { EscolasService } from '../../escolas/services/escolas.service';
import { EscolaResponse } from '../../../core/models/escola.model';
import { DisciplinaMatrizCard } from '../../../core/models/disciplina.model';

@Component({
  selector: 'app-aluno-matriz-ofertada',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent, MatrizPlannerComponent],
  templateUrl: './aluno-matriz-ofertada.component.html',
  styleUrl: './aluno-matriz-ofertada.component.css'
})
export class AlunoMatrizOfertadaComponent implements OnInit {
  escolas: EscolaResponse[] = [];
  escolaSelecionada = '';

  disciplinas: DisciplinaMatrizCard[] = [];
  disciplinasFiltradas: DisciplinaMatrizCard[] = [];

  loading = false;
  errorMessage = '';

  constructor(
    private escolasService: EscolasService,
    private disciplinasService: DisciplinasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarEscolas();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar escolas.';
      }
    });
  }

  carregarDisciplinasPorEscola(event: Event): void {
    const escolaId = Number((event.target as HTMLSelectElement).value);
    this.escolaSelecionada = String(escolaId);

    if (!escolaId) {
      this.disciplinas = [];
      this.disciplinasFiltradas = [];
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.disciplinasService.listarPorEscola(escolaId, 0, 100).subscribe({
      next: (response) => {
        this.disciplinas = response.content.map((item) => ({
          id: item.id,
          sigla: item.sigla,
          descricao: item.descricao
        }));

        this.disciplinasFiltradas = [...this.disciplinas];
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar disciplinas da escola.';
        this.disciplinas = [];
        this.disciplinasFiltradas = [];
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}