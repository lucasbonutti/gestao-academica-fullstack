import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { StandardError, ValidationError } from '../../../core/models/api-error.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  login = '';
  senha = '';
  loading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  entrar(): void {
    this.errorMessage = '';

    if (!this.login || !this.senha) {
      this.errorMessage = 'Preencha login e senha.';
      return;
    }

    this.loading = true;

    this.authService.login({
      login: this.login,
      senha: this.senha
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        this.loading = false;

        const apiError = err?.error as StandardError | ValidationError;
        this.errorMessage = apiError?.message || 'Erro ao realizar login.';
      }
    });
  }
}