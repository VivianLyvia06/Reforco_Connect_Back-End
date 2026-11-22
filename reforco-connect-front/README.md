# Reforço Connect — Front-end (Angular)

Painel de gestão: Início (relatório de engajamento), Instituições, Professores e Agendamentos.

## Pré-requisitos

- Node.js LTS (Windows: `winget install OpenJS.NodeJS.LTS` · Linux: via `nvm`)
- Back-end rodando em `http://localhost:8080` (ver README do back)

## Rodar

```
npm install
npx ng serve
```

Abra `http://localhost:4200` (porta liberada no `CorsConfig` do back).

## Estrutura

```
src/app/
├── api.config.ts          URL da API (único lugar)
├── models/                interfaces TypeScript espelhando o JSON da API
├── services/              um service por recurso: listar, buscarPorId, cadastrar, atualizar, remover
├── shared/formatacao.ts   "08", "13,5 h", datas e mensagens de erro da API
├── pages/
│   ├── relatorio/         "/"  cards + tabela de GET /professores/relatorio-engajamento
│   ├── instituicoes/      CRUD
│   ├── professores/       CRUD com lista suspensa de instituições
│   └── agendamentos/      CRUD com lista suspensa de professores e datetime
└── app.ts / app.html      shell: barra lateral + área de conteúdo
```

## Observações

- Os três cards da tela inicial são somados no front a partir da mesma lista do relatório. Não existe endpoint extra para eles.
- O formulário **não** usa `required` do HTML de propósito: quem valida é o back (`@Valid`), e o erro 400 aparece no painel.
- O `<input type="datetime-local">` devolve `2026-11-25T14:30`; o `AgendamentoService` acrescenta `:00` antes de enviar.
- Angular 22 usa `OnPush` como padrão de detecção de mudanças. Os componentes usam `ChangeDetectionStrategy.Eager` para a tela atualizar sozinha após as requisições HTTP.
