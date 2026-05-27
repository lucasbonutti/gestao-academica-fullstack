import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { ProfessoresService } from '../services/professores.service';
import { EscolasService } from '../services/escolas.service';
import { EscolaResponse } from '../../../core/models/escola.model';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { ProfessorFormacoesPanelComponent } from '../../../shared/components/professor-formacoes-panel.component';

@Component({
  selector: 'app-professor-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive, AppShellComponent, ProfessorFormacoesPanelComponent],
  templateUrl: './professor-edit.component.html',
  styleUrl: './professor-edit.component.css'
})
export class ProfessorEditComponent implements OnInit {
  id!: number;
  matricula = '';
  nomeCompleto = '';
  email = '';
  telefone = '';
  escolaId = '';

  escolas: EscolaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private professoresService: ProfessoresService,
    private escolasService: EscolasService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID do professor não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID inválido.';
      return;
    }

    this.carregarEscolas();
    this.carregarProfessor();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      }
    });
  }

  carregarProfessor(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.professoresService.buscarPorId(this.id).subscribe({
      next: (professor) => {
        this.matricula = professor.matricula;
        this.nomeCompleto = professor.nomeCompleto;
        this.email = professor.email;
        this.telefone = professor.telefone;
        this.escolaId = String(professor.escola.id);
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar professor.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.professoresService.atualizar({
      id: this.id,
      matricula: this.matricula,
      nomeCompleto: this.nomeCompleto,
      email: this.email,
      telefone: this.telefone,
      escolaId: Number(this.escolaId)
    }).subscribe({
      next: () => {
        this.successMessage = 'Professor atualizado com sucesso!';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar professor.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}