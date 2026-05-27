import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
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
  selector: 'app-monitoria-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './monitoria-edit.component.html',
  styleUrl: './monitoria-edit.component.css'
})
export class MonitoriaEditComponent implements OnInit {
  id!: number;
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
    private route: ActivatedRoute,
    private router: Router,
    private monitoriasService: MonitoriasService,
    private alunosService: AlunosService,
    private professoresService: ProfessoresService,
    private disciplinasService: DisciplinasService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID da monitoria não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID da monitoria inválido.';
      return;
    }

    this.carregarListas();
    this.carregarMonitoria();
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

  carregarMonitoria(): void {
    this.loading = true;
    this.errorMessage = '';

    this.monitoriasService.buscarPorId(this.id).subscribe({
      next: (monitoria) => {
        this.alunoId = String(monitoria.aluno.id);
        this.professorId = String(monitoria.professor.id);
        this.disciplinaId = String(monitoria.disciplina.id);
        this.semestre = monitoria.semestre;
        this.tipoMonitoria = monitoria.tipoMonitoria;
        this.local = monitoria.local;
        this.dataInicio = monitoria.dataInicio;
        this.dataFim = monitoria.dataFim;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar monitoria.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.monitoriasService.atualizar({
      id: this.id,
      alunoId: Number(this.alunoId),
      disciplinaId: Number(this.disciplinaId),
      professorId: Number(this.professorId),
      semestre: this.semestre,
      tipoMonitoria: this.tipoMonitoria,
      local: this.local,
      dataInicio: this.dataInicio,
      dataFim: this.dataFim
    }).subscribe({
      next: () => {
        this.successMessage = 'Monitoria atualizada com sucesso.';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar monitoria.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}