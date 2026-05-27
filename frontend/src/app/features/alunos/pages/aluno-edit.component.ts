import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AlunosService } from '../services/alunos.service';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

@Component({
  selector: 'app-aluno-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive, AppShellComponent],
  templateUrl: './aluno-edit.component.html',
  styleUrl: './aluno-edit.component.css'
})
export class AlunoEditComponent implements OnInit {
  id!: number;
  matricula = '';
  nomeCompleto = '';
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alunosService: AlunosService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID do aluno não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID inválido.';
      return;
    }

    this.carregarAluno();
  }

  carregarAluno(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.alunosService.buscarPorId(this.id).subscribe({
      next: (aluno) => {
        this.matricula = aluno.matricula;
        this.nomeCompleto = aluno.nomeCompleto;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar aluno.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.alunosService.atualizar({
      id: this.id,
      matricula: this.matricula,
      nomeCompleto: this.nomeCompleto
    }).subscribe({
      next: () => {
        this.successMessage = 'Aluno atualizado com sucesso!';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar aluno.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}