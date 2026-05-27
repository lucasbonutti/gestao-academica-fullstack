import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AlunosService } from '../services/alunos.service';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
@Component({
  selector: 'app-aluno-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive, AppShellComponent],
  templateUrl: './aluno-form.component.html',
  styleUrl: './aluno-form.component.css'
})
export class AlunoFormComponent {
  matricula = '';
  nomeCompleto = '';
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private alunosService: AlunosService,
    private authService: AuthService,
    private router: Router
  ) {}

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.alunosService.criar({
      matricula: this.matricula,
      nomeCompleto: this.nomeCompleto
    }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Aluno cadastrado com sucesso!';
        this.matricula = '';
        this.nomeCompleto = '';
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar aluno.';
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}