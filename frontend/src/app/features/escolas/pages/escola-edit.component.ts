import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { EscolasService } from '../services/escolas.service';
import { IesService } from '../../ies/services/ies.service';

import { IesResponse } from '../../../core/models/ies.model';

@Component({
  selector: 'app-escola-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './escola-edit.component.html',
  styleUrl: './escola-edit.component.css'
})
export class EscolaEditComponent implements OnInit {
  id!: number;

  nome = '';
  coordenador = '';
  iesId = '';

  iesList: IesResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private escolasService: EscolasService,
    private iesService: IesService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID da escola não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID da escola inválido.';
      return;
    }

    this.carregarIes();
    this.carregarEscola();
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

  carregarEscola(): void {
    this.loading = true;
    this.errorMessage = '';

    this.escolasService.buscarPorId(this.id).subscribe({
      next: (escola: any) => {
        this.nome = escola.nome;
        this.coordenador = escola.coordenador;
        this.iesId = String(escola.ies?.id ?? '');
        this.loading = false;
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar escola.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.escolasService.atualizar({
      id: this.id,
      nome: this.nome,
      coordenador: this.coordenador,
      iesId: Number(this.iesId)
    }).subscribe({
      next: () => {
        this.successMessage = 'Escola atualizada com sucesso.';
        this.loading = false;
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar escola.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}