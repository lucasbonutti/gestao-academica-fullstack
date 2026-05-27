import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { ProfessoresService } from '../services/professores.service';
import { EscolasService } from '../../escolas/services/escolas.service';
import { FormacoesService } from '../services/formacoes.service';
import { EscolaResponse } from '../../../core/models/escola.model';
import { Titulacao } from '../../../core/models/formacao.model';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

@Component({
  selector: 'app-professor-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive, AppShellComponent],
  templateUrl: './professor-form.component.html',
  styleUrl: './professor-form.component.css'
})
export class ProfessorFormComponent implements OnInit {
  matricula = '';
  nomeCompleto = '';
  email = '';
  telefone = '';
  escolaId = '';

  titulacao: Titulacao | '' = '';
  instituicao = '';
  nomeCurso = '';
  anoConclusao: number | null = null;

  escolas: EscolaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private professoresService: ProfessoresService,
    private escolasService: EscolasService,
    private formacoesService: FormacoesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarEscolas();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => this.escolas = response.content,
      error: (err: any) => this.errorMessage = err?.error?.message || 'Erro ao carregar escolas.'
    });
  }

  salvar(): void {
    this.errorMessage = '';
    this.successMessage = '';

    const matriculaLimpa = this.apenasNumeros(this.matricula);
    const telefoneLimpo = this.apenasNumeros(this.telefone);

    if (matriculaLimpa.length < 6) {
      this.errorMessage = 'A matrícula deve ter pelo menos 6 números.';
      return;
    }

    if (!this.nomeCompleto.trim()) {
      this.errorMessage = 'Informe o nome completo do professor.';
      return;
    }

    if (!this.email.trim()) {
      this.errorMessage = 'Informe o email do professor.';
      return;
    }

    if (telefoneLimpo.length < 10) {
      this.errorMessage = 'Informe um telefone válido com DDD.';
      return;
    }

    if (!this.escolaId) {
      this.errorMessage = 'Selecione uma escola.';
      return;
    }

    if (!this.titulacao || !this.instituicao.trim() || !this.nomeCurso.trim() || !this.anoConclusao) {
      this.errorMessage = 'Preencha os dados da formação acadêmica.';
      return;
    }

    this.loading = true;

    this.professoresService.criar({
      matricula: matriculaLimpa,
      nomeCompleto: this.nomeCompleto.trim(),
      email: this.email.trim(),
      telefone: this.telefone,
      escolaId: Number(this.escolaId)
    }).subscribe({
      next: (professor) => {
        this.formacoesService.criar({
          professorId: professor.id,
          titulacao: this.titulacao as Titulacao,
          instituicao: this.instituicao.trim(),
          nomeCurso: this.nomeCurso.trim(),
          anoConclusao: Number(this.anoConclusao)
        }).subscribe({
          next: () => {
            this.loading = false;
            this.successMessage = 'Professor e formação cadastrados com sucesso!';
            this.limparFormulario();
          },
          error: (err: any) => {
            this.loading = false;
            this.errorMessage = err?.error?.message || 'Professor criado, mas houve erro ao cadastrar a formação.';
          }
        });
      },
      error: (err: any) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar professor.';
      }
    });
  }

  limparFormulario(): void {
    this.matricula = '';
    this.nomeCompleto = '';
    this.email = '';
    this.telefone = '';
    this.escolaId = '';
    this.titulacao = '';
    this.instituicao = '';
    this.nomeCurso = '';
    this.anoConclusao = null;
  }

  apenasNumeros(valor: string): string {
    return valor.replace(/\D/g, '');
  }

  formatarTelefone(valor: string): string {
    const numeros = this.apenasNumeros(valor).slice(0, 11);

    if (numeros.length <= 2) return numeros;
    if (numeros.length <= 7) return `(${numeros.slice(0, 2)})${numeros.slice(2)}`;

    return `(${numeros.slice(0, 2)})${numeros.slice(2, 7)}-${numeros.slice(7, 11)}`;
  }

  onMatriculaInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.matricula = this.apenasNumeros(input.value).slice(0, 20);
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