import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MatrizDisciplinasService } from '../services/matriz-disciplinas.service';
import { DisciplinasService } from '../../disciplinas/services/disciplinas.service';
import { DisciplinaResponse } from '../../../core/models/disciplina.model';

@Component({
  selector: 'app-matriz-disciplina-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './matriz-disciplina-form.component.html',
  styleUrl: './matriz-disciplina-form.component.css'
})
export class MatrizDisciplinaFormComponent implements OnInit {
  matrizId!: number;
  disciplinaId = '';
  preRequisitosIds: number[] = [];

  disciplinas: DisciplinaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private matrizDisciplinasService: MatrizDisciplinasService,
    private disciplinasService: DisciplinasService
  ) {}

  ngOnInit(): void {
    const matrizIdParam = this.route.snapshot.paramMap.get('id');

    if (!matrizIdParam) {
      this.errorMessage = 'ID da matriz não informado.';
      return;
    }

    this.matrizId = Number(matrizIdParam);

    if (isNaN(this.matrizId)) {
      this.errorMessage = 'ID da matriz inválido.';
      return;
    }

    this.carregarDisciplinas();
  }

  carregarDisciplinas(): void {
    this.disciplinasService.listar(0, 200).subscribe({
      next: (response) => {
        this.disciplinas = response.content;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar disciplinas.';
      }
    });
  }

  alternarPreRequisito(id: number, checked: boolean): void {
    if (checked) {
      if (!this.preRequisitosIds.includes(id)) {
        this.preRequisitosIds.push(id);
      }
      return;
    }

    this.preRequisitosIds = this.preRequisitosIds.filter((item) => item !== id);
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.matrizDisciplinasService.criar({
      matrizId: this.matrizId,
      disciplinaId: Number(this.disciplinaId),
      preRequisitosIds: this.preRequisitosIds
    }).subscribe({
      next: () => {
        this.successMessage = 'Disciplina vinculada à matriz com sucesso.';
        this.loading = false;
        this.disciplinaId = '';
        this.preRequisitosIds = [];
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao vincular disciplina à matriz.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}