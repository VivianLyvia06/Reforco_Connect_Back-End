import { Routes } from '@angular/router';
import { RelatorioPage } from './pages/relatorio/relatorio';
import { InstituicoesPage } from './pages/instituicoes/instituicoes';
import { ProfessoresPage } from './pages/professores/professores';
import { AgendamentosPage } from './pages/agendamentos/agendamentos';

export const routes: Routes = [
  { path: '', component: RelatorioPage, title: 'Painel de engajamento · Reforço Connect' },
  { path: 'instituicoes', component: InstituicoesPage, title: 'Instituições · Reforço Connect' },
  { path: 'professores', component: ProfessoresPage, title: 'Professores · Reforço Connect' },
  { path: 'agendamentos', component: AgendamentosPage, title: 'Agendamentos · Reforço Connect' },
  { path: '**', redirectTo: '' },
];
