import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { IesService } from '../services/ies.service';

@Component({
  selector: 'app-ies-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './ies-edit.component.html',
  styleUrl: './ies-edit.component.css'
})
export class IesEditComponent implements OnInit {
  id!: number;
  nome = '';
  endereco = '';
  telefone = '';

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private iesService: IesService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID da IES não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID inválido.';
      return;
    }

    this.carregarIes();
  }

  carregarIes(): void {
    this.loading = true;
    this.errorMessage = '';

    this.iesService.buscarPorId(this.id).subscribe({
      next: (ies) => {
        this.nome = ies.nome;
        this.endereco = ies.endereco;
        this.telefone = ies.telefone;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar IES.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.iesService.atualizar({
      id: this.id,
      nome: this.nome,
      endereco: this.endereco,
      telefone: this.telefone
    }).subscribe({
      next: () => {
        this.successMessage = 'IES atualizada com sucesso.';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar IES.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}