import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ProfessorService } from '../../services/professor.service';
import { InstituicaoService } from '../../services/instituicao.service';
import { Professor } from '../../models/professor.model';
import { Instituicao } from '../../models/instituicao.model';
import { doisDigitos, mensagemDeErro } from '../../shared/formatacao';

// Campos do formulário (a instituição é escolhida pelo id na lista suspensa)
interface FormularioProfessor {
  nome: string;
  disciplina: string;
  fotoUrl: string;
  instituicaoId: number | null;
}

@Component({
  selector: 'app-professores',
  imports: [FormsModule],
  templateUrl: './professores.html',
  // No Angular 22 o padrão é OnPush (só atualiza a tela com signals/inputs).
  // Eager volta ao comportamento clássico: a tela atualiza sozinha quando
  // as propriedades mudam (ex.: dentro do subscribe da requisição HTTP).
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class ProfessoresPage implements OnInit {
  private professorService = inject(ProfessorService);
  private instituicaoService = inject(InstituicaoService);

  professores: Professor[] = [];
  instituicoes: Instituicao[] = []; // opções da lista suspensa
  erroLista = '';
  erroFormulario = '';
  salvando = false;

  painelAberto = false;
  idEmEdicao: number | null = null;
  formulario: FormularioProfessor = this.formularioVazio();

  doisDigitos = doisDigitos;

  ngOnInit(): void {
    this.carregar();
    this.instituicaoService.listar().subscribe({
      next: (resposta) => (this.instituicoes = resposta),
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  carregar(): void {
    this.professorService.listar().subscribe({
      next: (resposta) => {
        this.professores = resposta;
        this.erroLista = '';
      },
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  nomeDaInstituicao(professor: Professor): string {
    const instituicao = professor.instituicao as Instituicao;
    return instituicao ? instituicao.nome : '—';
  }

  novo(): void {
    this.idEmEdicao = null;
    this.formulario = this.formularioVazio();
    this.erroFormulario = '';
    this.painelAberto = true;
  }

  editar(professor: Professor): void {
    this.professorService.buscarPorId(professor.id!).subscribe({
      next: (professorAtual) => {
        this.idEmEdicao = professorAtual.id!;
        this.formulario = {
          nome: professorAtual.nome,
          disciplina: professorAtual.disciplina,
          fotoUrl: professorAtual.fotoUrl ?? '',
          instituicaoId: professorAtual.instituicao ? professorAtual.instituicao.id! : null,
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
    // O back-end espera a instituição como objeto: { "instituicao": { "id": 1 } }
    const professorParaEnviar = {
      nome: this.formulario.nome,
      disciplina: this.formulario.disciplina,
      fotoUrl: this.formulario.fotoUrl || null,
      instituicao: this.formulario.instituicaoId === null ? null : { id: this.formulario.instituicaoId },
    } as Professor;

    this.salvando = true;
    const requisicao =
      this.idEmEdicao === null
        ? this.professorService.cadastrar(professorParaEnviar)
        : this.professorService.atualizar(this.idEmEdicao, professorParaEnviar);

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

  excluir(professor: Professor): void {
    const confirmou = confirm(
      `Excluir "${professor.nome}"?\n\nOs agendamentos deste professor também serão excluídos.`,
    );
    if (!confirmou) {
      return;
    }
    this.professorService.remover(professor.id!).subscribe({
      next: () => {
        if (this.idEmEdicao === professor.id) {
          this.fechar();
        }
        this.carregar();
      },
      error: (falha: HttpErrorResponse) => (this.erroLista = mensagemDeErro(falha)),
    });
  }

  private formularioVazio(): FormularioProfessor {
    return { nome: '', disciplina: '', fotoUrl: '', instituicaoId: null };
  }
}
