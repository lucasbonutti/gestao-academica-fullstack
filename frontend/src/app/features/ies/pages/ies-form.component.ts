import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { IesService } from '../services/ies.service';

@Component({
  selector: 'app-ies-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './ies-form.component.html',
  styleUrl: './ies-form.component.css'
})
export class IesFormComponent {
  nome = '';
  endereco = '';
  telefone = '';

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private iesService: IesService,
    private authService: AuthService,
    private router: Router
  ) {}

  salvar(): void {
    this.errorMessage = '';
    this.successMessage = '';

    const telefoneLimpo = this.apenasNumeros(this.telefone);

    if (!this.nome.trim()) {
      this.errorMessage = 'Informe o nome da IES.';
      return;
    }

    if (!this.endereco.trim()) {
      this.errorMessage = 'Informe o endereço da IES.';
      return;
    }

    if (telefoneLimpo.length < 10) {
      this.errorMessage = 'Informe um telefone válido com DDD.';
      return;
    }

    this.loading = true;

    this.iesService.criar({
      nome: this.nome.trim(),
      endereco: this.endereco.trim(),
      telefone: this.telefone
    }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'IES cadastrada com sucesso!';
        this.nome = '';
        this.endereco = '';
        this.telefone = '';
      },
      error: (err: any) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar IES.';
      }
    });
  }

  apenasNumeros(valor: string): string {
    return valor.replace(/\D/g, '');
  }

  formatarTelefone(valor: string): string {
    const numeros = this.apenasNumeros(valor).slice(0, 11);

    if (numeros.length <= 2) {
      return numeros;
    }

    if (numeros.length <= 7) {
      return `(${numeros.slice(0, 2)})${numeros.slice(2)}`;
    }

    return `(${numeros.slice(0, 2)})${numeros.slice(2, 7)}-${numeros.slice(7, 11)}`;
  }

  onTelefoneInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.telefone = this.formatarTelefone(input.value);
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}