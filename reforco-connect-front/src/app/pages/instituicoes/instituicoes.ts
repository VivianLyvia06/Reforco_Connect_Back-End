import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { InstituicaoService } from '../../services/instituicao.service';
import { Instituicao } from '../../models/instituicao.model';
import { doisDigitos, mensagemDeErro } from '../../shared/formatacao';

@Component({
  selector: 'app-instituicoes',
  imports: [FormsModule],
  templateUrl: './instituicoes.html',
  // No Angular 22 o padrão é OnPush (só atualiza a tela com signals/inputs).
  // Eager volta ao comportamento clássico: a tela atualiza sozinha quando
  // as propriedades mudam (ex.: dentro do subscribe da requisição HTTP).
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class InstituicoesPage implements OnInit {
  private instituicaoService = inject(InstituicaoService);

  instituicoes: Instituicao[] = [];
  erroLista = '';
  erroFormulario = '';
  salvando = false;

  // Estado do painel lateral
  painelAberto = false;
  idEmEdicao: number | null = null; // null = cadastrando uma nova
  formulario: Instituicao = this.formularioVazio();

  doisDigitos = doisDigitos;

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.instituicaoService.listar().subscribe({
      next: (resposta) => {
        this.instituicoes = resposta;
        this.erroLista = '';
      },
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  novo(): void {
    this.idEmEdicao = null;
    this.formulario = this.formularioVazio();
    this.erroFormulario = '';
    this.painelAberto = true;
  }

  editar(instituicao: Instituicao): void {
    // Busca pelo id (GET /instituicoes/{id}) para editar o dado mais recente
    this.instituicaoService.buscarPorId(instituicao.id!).subscribe({
      next: (instituicaoAtual) => {
        this.idEmEdicao = instituicaoAtual.id!;
        this.formulario = {
          nome: instituicaoAtual.nome,
          telefone: instituicaoAtual.telefone,
          endereco: instituicaoAtual.endereco,
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
    this.salvando = true;
    const requisicao =
      this.idEmEdicao === null
        ? this.instituicaoService.cadastrar(this.formulario)
        : this.instituicaoService.atualizar(this.idEmEdicao, this.formulario);

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

  excluir(instituicao: Instituicao): void {
    const confirmou = confirm(
      `Excluir "${instituicao.nome}"?\n\n` +
        'Atenção: os professores desta instituição e os agendamentos deles também serão excluídos.',
    );
    if (!confirmou) {
      return;
    }
    this.instituicaoService.remover(instituicao.id!).subscribe({
      next: () => {
        if (this.idEmEdicao === instituicao.id) {
          this.fechar();
        }
        this.carregar();
      },
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  private formularioVazio(): Instituicao {
    return { nome: '', telefone: '', endereco: '' };
  }
}
