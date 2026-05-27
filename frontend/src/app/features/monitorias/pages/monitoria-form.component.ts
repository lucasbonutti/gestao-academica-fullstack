import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MonitoriasService } from '../services/monitorias.service';
import { AlunosService } from '../../alunos/services/alunos.service';
import { ProfessoresService } from '../../professores/services/professores.service';
import { DisciplinasService } from '../../disciplinas/services/disciplinas.service';
import { AlunoResponse } from '../../../core/models/aluno.model';
import { ProfessorResponse } from '../../../core/models/professor.model';
import { DisciplinaResponse } from '../../../core/models/disciplina.model';

@Component({
  selector: 'app-monitoria-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './monitoria-form.component.html',
  styleUrl: './monitoria-form.component.css'
})
export class MonitoriaFormComponent implements OnInit {
  alunoId = '';
  disciplinaId = '';
  professorId = '';
  semestre = '';
  tipoMonitoria: 'PRESENCIAL' | 'REMOTO' = 'PRESENCIAL';
  local = '';
  dataInicio = '';
  dataFim = '';

  alunos: AlunoResponse[] = [];
  professores: ProfessorResponse[] = [];
  disciplinas: DisciplinaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private monitoriasService: MonitoriasService,
    private alunosService: AlunosService,
    private professoresService: ProfessoresService,
    private disciplinasService: DisciplinasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarListas();
  }

  carregarListas(): void {
    this.alunosService.listarAtivos(0, 100).subscribe({
      next: (response) => {
        this.alunos = response.content;
      }
    });

    this.professoresService.listarAtivos(0, 100).subscribe({
      next: (response) => {
        this.professores = response.content;
      }
    });

    this.disciplinasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.disciplinas = response.content;
      }
    });
  }

  salvar(): void {
  this.errorMessage = '';
  this.successMessage = '';

  if (!this.alunoId || !this.professorId || !this.disciplinaId) {
    this.errorMessage = 'Selecione aluno, professor e disciplina.';
    return;
  }

  if (!this.semestre || !this.local || !this.dataInicio || !this.dataFim) {
    this.errorMessage = 'Preencha todos os campos obrigatórios.';
    return;
  }

  this.loading = true;

  const payload = {
    alunoId: Number(this.alunoId),
    disciplinaId: Number(this.disciplinaId),
    professorId: Number(this.professorId),
    semestre: this.semestre,
    tipoMonitoria: this.tipoMonitoria,
    local: this.local,
    dataInicio: this.dataInicio,
    dataFim: this.dataFim
  };

  console.log('Payload monitoria:', payload);

  this.monitoriasService.criar(payload).subscribe({
    next: () => {
      this.successMessage = 'Monitoria cadastrada com sucesso.';
      this.loading = false;
      this.alunoId = '';
      this.disciplinaId = '';
      this.professorId = '';
      this.semestre = '';
      this.tipoMonitoria = 'PRESENCIAL';
      this.local = '';
      this.dataInicio = '';
      this.dataFim = '';
    },
    error: (err) => {
      console.error('Erro ao cadastrar monitoria:', err);
      console.error('Status:', err?.status);
      console.error('Body:', err?.error);

      this.errorMessage =
        err?.error?.message ||
        err?.error?.erro ||
        'Erro ao cadastrar monitoria.';
      this.loading = false;
    }
  });
}

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}