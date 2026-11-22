import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AgendamentoService } from '../../services/agendamento.service';
import { ProfessorService } from '../../services/professor.service';
import { Agendamento, STATUS_AGENDAMENTO } from '../../models/agendamento.model';
import { Professor } from '../../models/professor.model';
import { doisDigitos, formatarDataHora, mensagemDeErro } from '../../shared/formatacao';

interface FormularioAgendamento {
  alunoNome: string;
  professorId: number | null;
  dataHora: string; // formato do <input type="datetime-local">: "2026-11-25T14:30"
  duracaoMinutos: number | null;
  status: string;
}

@Component({
  selector: 'app-agendamentos',
  imports: [FormsModule],
  templateUrl: './agendamentos.html',
  // No Angular 22 o padrão é OnPush (só atualiza a tela com signals/inputs).
  // Eager volta ao comportamento clássico: a tela atualiza sozinha quando
  // as propriedades mudam (ex.: dentro do subscribe da requisição HTTP).
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class AgendamentosPage implements OnInit {
  private agendamentoService = inject(AgendamentoService);
  private professorService = inject(ProfessorService);

  agendamentos: Agendamento[] = [];
  professores: Professor[] = []; // opções da lista suspensa
  statusDisponiveis = STATUS_AGENDAMENTO;
  erroLista = '';
  erroFormulario = '';
  salvando = false;

  painelAberto = false;
  idEmEdicao: number | null = null;
  formulario: FormularioAgendamento = this.formularioVazio();

  doisDigitos = doisDigitos;
  formatarDataHora = formatarDataHora;

  ngOnInit(): void {
    this.carregar();
    this.professorService.listar().subscribe({
      next: (resposta) => (this.professores = resposta),
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  carregar(): void {
    this.agendamentoService.listar().subscribe({
      next: (resposta) => {
        // mais recentes primeiro
        this.agendamentos = resposta.sort((primeiro, segundo) => segundo.dataHora.localeCompare(primeiro.dataHora));
        this.erroLista = '';
      },
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  nomeDoProfessor(agendamento: Agendamento): string {
    const professor = agendamento.professor as Professor;
    return professor ? professor.nome : '—';
  }

  // "Concluído" -> "status-concluido" (classe CSS da etiqueta)
  classeDoStatus(status: string): string {
    return 'status-' + status.toLowerCase().normalize('NFD').replace(/[̀-ͯ]/g, '');
  }

  novo(): void {
    this.idEmEdicao = null;
    this.formulario = this.formularioVazio();
    this.erroFormulario = '';
    this.painelAberto = true;
  }

  editar(agendamento: Agendamento): void {
    this.agendamentoService.buscarPorId(agendamento.id!).subscribe({
      next: (agendamentoAtual) => {
        this.idEmEdicao = agendamentoAtual.id!;
        this.formulario = {
          alunoNome: agendamentoAtual.alunoNome,
          professorId: agendamentoAtual.professor ? agendamentoAtual.professor.id! : null,
          // "2026-11-25T14:30:00" -> "2026-11-25T14:30" (o input não aceita segundos)
          dataHora: agendamentoAtual.dataHora.substring(0, 16),
          duracaoMinutos: agendamentoAtual.duracaoMinutos,
          status: agendamentoAtual.status,
        };
        this.erroFormulario = '';
        this.painelAberto = true;
      },
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  fechar(): void {
    this.painelAberto = false;
    this.idEmEdicao = null;
  }

  salvar(): void {
    // O back-end espera o professor como objeto: { "professor": { "id": 1 } }
    // Os ":00" dos segundos são acrescentados no AgendamentoService.
    const agendamentoParaEnviar = {
      alunoNome: this.formulario.alunoNome,
      dataHora: this.formulario.dataHora || null,
      duracaoMinutos: this.formulario.duracaoMinutos,
      status: this.formulario.status,
      professor: this.formulario.professorId === null ? null : { id: this.formulario.professorId },
    } as unknown as Agendamento;

    this.salvando = true;
    const requisicao =
      this.idEmEdicao === null
        ? this.agendamentoService.cadastrar(agendamentoParaEnviar)
        : this.agendamentoService.atualizar(this.idEmEdicao, agendamentoParaEnviar);

    requisicao.subscribe({
      next: () => {
        this.salvando = false;
        this.fechar();
        this.carregar();
      },
      error: (falha: HttpErrorResponse) => {
        this.salvando = false;
        this.erroFormulario = mensagemDeErro(falha);
      },
    });
  }

  excluir(agendamento: Agendamento): void {
    const confirmou = confirm(`Excluir o agendamento de "${agendamento.alunoNome}"?`);
    if (!confirmou) {
      return;
    }
    this.agendamentoService.remover(agendamento.id!).subscribe({
      next: () => {
        if (this.idEmEdicao === agendamento.id) {
          this.fechar();
        }
        this.carregar();
      },
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  private formularioVazio(): FormularioAgendamento {
    return { alunoNome: '', professorId: null, dataHora: '', duracaoMinutos: 60, status: 'Agendado' };
  }
}
