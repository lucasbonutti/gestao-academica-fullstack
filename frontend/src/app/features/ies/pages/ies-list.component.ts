import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { IesService } from '../services/ies.service';
import { IesResponse } from '../../../core/models/ies.model';

@Component({
  selector: 'app-ies-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent],
  templateUrl: './ies-list.component.html',
  styleUrl: './ies-list.component.css'
})
export class IesListComponent implements OnInit {
  iesList: IesResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;

  constructor(
    private iesService: IesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarIes();
  }

  carregarIes(): void {
    this.loading = true;
    this.errorMessage = '';

    this.iesService.listar(this.page, 10).subscribe({
      next: (response) => {
        this.iesList = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar IES.';
        this.loading = false;
      }
    });
  }

  excluir(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.iesService.excluir(id).subscribe({
      next: () => {
        this.successMessage = 'IES excluída com sucesso.';
        this.carregarIes();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao excluir IES.';
      }
    });
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarIes();
    }
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarIes();
    }
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}