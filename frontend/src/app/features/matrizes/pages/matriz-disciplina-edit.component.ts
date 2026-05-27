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
  selector: 'app-matriz-disciplina-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './matriz-disciplina-edit.component.html',
  styleUrl: './matriz-disciplina-edit.component.css'
})
export class MatrizDisciplinaEditComponent implements OnInit {
  id!: number;
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
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID do vínculo não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID do vínculo inválido.';
      return;
    }

    this.carregarDisciplinas();
    this.carregarVinculo();
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

  carregarVinculo(): void {
    this.loading = true;
    this.errorMessage = '';

    this.matrizDisciplinasService.buscarPorId(this.id).subscribe({
      next: (response) => {
        this.matrizId = response.matriz.id;
        this.disciplinaId = String(response.disciplina.id);
        this.preRequisitosIds = response.preRequisitos.map((item) => item.id);
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar vínculo.';
        this.loading = false;
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

    this.matrizDisciplinasService.atualizar({
      id: this.id,
      matrizId: this.matrizId,
      disciplinaId: Number(this.disciplinaId),
      preRequisitosIds: this.preRequisitosIds
    }).subscribe({
      next: () => {
        this.successMessage = 'Vínculo atualizado com sucesso.';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar vínculo.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}