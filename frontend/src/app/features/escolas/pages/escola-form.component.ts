import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { EscolasService } from '../services/escolas.service';
import { IesService } from '../../ies/services/ies.service';

import { IesResponse } from '../../../core/models/ies.model';

@Component({
  selector: 'app-escola-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './escola-form.component.html',
  styleUrl: './escola-form.component.css'
})
export class EscolaFormComponent implements OnInit {
  nome = '';
  coordenador = '';
  iesId = '';

  iesList: IesResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private escolasService: EscolasService,
    private iesService: IesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarIes();
  }

  carregarIes(): void {
    this.iesService.listar(0, 100).subscribe({
      next: (response: any) => {
        this.iesList = response.content ?? response;
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar IES.';
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.escolasService.criar({
      nome: this.nome,
      coordenador: this.coordenador,
      iesId: Number(this.iesId)
    }).subscribe({
      next: () => {
        this.successMessage = 'Escola cadastrada com sucesso.';
        this.loading = false;

        this.nome = '';
        this.coordenador = '';
        this.iesId = '';
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar escola.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}