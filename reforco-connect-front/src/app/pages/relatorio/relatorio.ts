import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ProfessorService } from '../../services/professor.service';
import { RelatorioEngajamento } from '../../models/relatorio-engajamento.model';
import { doisDigitos, formatarHoras, mensagemDeErro } from '../../shared/formatacao';

// Tela inicial: painel com o relatório de engajamento.
// Os três cards e a linha de totais são calculados aqui, no front, a partir
// da MESMA lista devolvida por GET /professores/relatorio-engajamento.
@Component({
  selector: 'app-relatorio',
  templateUrl: './relatorio.html',
  // No Angular 22 o padrão é OnPush (só atualiza a tela com signals/inputs).
  // Eager volta ao comportamento clássico: a tela atualiza sozinha quando
  // as propriedades mudam (ex.: dentro do subscribe da requisição HTTP).
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class RelatorioPage implements OnInit {
  private professorService = inject(ProfessorService);

  linhas: RelatorioEngajamento[] = [];
  carregando = true;
  erro = '';

  totalProfessores = 0;
  totalAgendamentos = 0;
  totalHoras = 0;

  // expostas para o template
  doisDigitos = doisDigitos;
  formatarHoras = formatarHoras;

  ngOnInit(): void {
    this.professorService.relatorioEngajamento().subscribe({
      next: (resposta) => {
        this.linhas = resposta;
        this.calcularTotais();
        this.carregando = false;
      },
      error: (falha: HttpErrorResponse) => {
        this.erro = mensagemDeErro(falha);
        this.carregando = false;
      },
    });
  }

  private calcularTotais(): void {
    this.totalProfessores = this.linhas.length;
    this.totalAgendamentos = 0;
    let somaHoras = 0;
    for (const linha of this.linhas) {
      this.totalAgendamentos += linha.totalAgendamentos;
      somaHoras += linha.totalHoras;
    }
    // evita resíduo de ponto flutuante (ex.: 62.99999)
    this.totalHoras = Math.round(somaHoras * 10) / 10;
  }
}
