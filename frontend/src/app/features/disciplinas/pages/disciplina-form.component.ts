import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { DisciplinasService } from '../services/disciplinas.service';
import { EscolasService } from '../../escolas/services/escolas.service';
import { EscolaResponse } from '../../../core/models/escola.model';

@Component({
  selector: 'app-disciplina-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './disciplina-form.component.html',
  styleUrl: './disciplina-form.component.css'
})
export class DisciplinaFormComponent implements OnInit {
  sigla = '';
  descricao = '';
  cargaHoraria = 60;
  escolaId = '';

  escolas: EscolaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private disciplinasService: DisciplinasService,
    private escolasService: EscolasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.disciplinasService.criar({
      sigla: this.sigla,
      descricao: this.descricao,
      cargaHoraria: Number(this.cargaHoraria),
      escolaId: Number(this.escolaId)
    }).subscribe({
      next: () => {
        this.successMessage = 'Disciplina cadastrada com sucesso.';
        this.loading = false;
        this.sigla = '';
        this.descricao = '';
        this.cargaHoraria = 60;
        this.escolaId = '';
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar disciplina.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}