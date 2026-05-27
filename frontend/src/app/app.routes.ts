import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';


import { MatrizesListComponent } from './features/matrizes/pages/matrizes-list.component';
import { MatrizFormComponent } from './features/matrizes/pages/matriz-form.component';
import { MatrizEditComponent } from './features/matrizes/pages/matriz-edit.component';

import { DisciplinasListComponent } from './features/disciplinas/pages/disciplinas-list.component';
import { DisciplinaFormComponent } from './features/disciplinas/pages/disciplina-form.component';
import { DisciplinaEditComponent } from './features/disciplinas/pages/disciplina-edit.component';

import { MonitoriasListComponent } from './features/monitorias/pages/monitorias-list.component';
import { MonitoriaFormComponent } from './features/monitorias/pages/monitoria-form.component';
import { MonitoriaEditComponent } from './features/monitorias/pages/monitoria-edit.component';



import { RelatoriosListComponent } from './features/relatorios/pages/relatorios-list.component';
import { RelatorioFormComponent } from './features/relatorios/pages/relatorio-form.component';

// AUTH
import { LoginComponent } from './features/auth/pages/login.component';

// DASHBOARD
import { DashboardComponent } from './features/dashboard/pages/dashboard.component';

// ESCOLAS
import { EscolasListComponent } from './features/escolas/pages/escolas-list.component';
import { EscolaFormComponent } from './features/escolas/pages/escola-form.component';
import { EscolaEditComponent } from './features/escolas/pages/escola-edit.component';

// IES
import { IesListComponent } from './features/ies/pages/ies-list.component';
import { IesFormComponent } from './features/ies/pages/ies-form.component';
import { IesEditComponent } from './features/ies/pages/ies-edit.component';

// PROFESSORES
import { ProfessoresListComponent } from './features/professores/pages/professores-list.component';
import { ProfessorFormComponent } from './features/professores/pages/professor-form.component';
import { ProfessorEditComponent } from './features/professores/pages/professor-edit.component';

// ALUNOS
import { AlunosListComponent } from './features/alunos/pages/alunos-list.component';
import { AlunoFormComponent } from './features/alunos/pages/aluno-form.component';
import { AlunoEditComponent } from './features/alunos/pages/aluno-edit.component';
import { AlunoMatrizOfertadaComponent } from './features/alunos/pages/aluno-matriz-ofertada.component';

// CURSOS
import { CursosListComponent } from './features/cursos/pages/cursos-list.component';
import { CursoFormComponent } from './features/cursos/pages/curso-form.component';
import { CursoEditComponent } from './features/cursos/pages/curso-edit.component';




export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

  // AUTH
  { path: 'login', component: LoginComponent },

  // DASHBOARD
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },

  // ESCOLAS
  { path: 'escolas', component: EscolasListComponent, canActivate: [authGuard] },
  { path: 'escolas/nova', component: EscolaFormComponent, canActivate: [authGuard] },
  { path: 'escolas/editar/:id', component: EscolaEditComponent, canActivate: [authGuard] },

  // IES
  { path: 'ies', component: IesListComponent, canActivate: [authGuard] },
  { path: 'ies/nova', component: IesFormComponent, canActivate: [authGuard] },
  { path: 'ies/editar/:id', component: IesEditComponent, canActivate: [authGuard] },

  // PROFESSORES
  { path: 'professores', component: ProfessoresListComponent, canActivate: [authGuard] },
  { path: 'professores/novo', component: ProfessorFormComponent, canActivate: [authGuard] },
  { path: 'professores/editar/:id', component: ProfessorEditComponent, canActivate: [authGuard] },

  // ALUNOS
  { path: 'alunos', component: AlunosListComponent, canActivate: [authGuard] },
  { path: 'alunos/novo', component: AlunoFormComponent, canActivate: [authGuard] },
  { path: 'alunos/editar/:id', component: AlunoEditComponent, canActivate: [authGuard] },

  // CURSOS
  { path: 'cursos', component: CursosListComponent, canActivate: [authGuard] },
  { path: 'cursos/novo', component: CursoFormComponent, canActivate: [authGuard] },
  { path: 'cursos/editar/:id', component: CursoEditComponent, canActivate: [authGuard] },

  { path: 'relatorios', component: RelatoriosListComponent, canActivate: [authGuard] },
  { path: 'relatorios/novo', component: RelatorioFormComponent, canActivate: [authGuard] },

  { path: 'matrizes', component: MatrizesListComponent, canActivate: [authGuard] },
  { path: 'matrizes/nova', component: MatrizFormComponent, canActivate: [authGuard] },
  { path: 'matrizes/editar/:id', component: MatrizEditComponent, canActivate: [authGuard] },
  { path: 'alunos/matriz-ofertada', component: AlunoMatrizOfertadaComponent, canActivate: [authGuard] },

  { path: 'disciplinas', component: DisciplinasListComponent, canActivate: [authGuard] },
  { path: 'disciplinas/nova', component: DisciplinaFormComponent, canActivate: [authGuard] },
  { path: 'disciplinas/editar/:id', component: DisciplinaEditComponent, canActivate: [authGuard] },

  { path: 'monitorias', component: MonitoriasListComponent, canActivate: [authGuard] },
  { path: 'monitorias/nova', component: MonitoriaFormComponent, canActivate: [authGuard] },
  { path: 'monitorias/editar/:id', component: MonitoriaEditComponent, canActivate: [authGuard] },

  

  // fallback
  { path: '**', redirectTo: 'dashboard' }
];