# Roteiro da apresentação — Reforço Connect

Duração estimada: **10 a 12 minutos** + perguntas.
O texto entre aspas é a fala sugerida. Não precisa decorar palavra por palavra: entenda a ideia
e fale do seu jeito.

Sugestão de divisão entre os três (ajustem como preferirem):

| Parte | Tempo | Quem |
|---|---|---|
| 1. Problema e solução | 1 min | Integrante 1 |
| 2. O que foi pedido × o que entregamos | 1 min | Integrante 1 |
| 3. Modelo de dados | 2 min | Integrante 2 |
| 4. Arquitetura do back-end | 2 min | Integrante 2 |
| 5. A consulta de agregação (SQL) | 2 min | Integrante 3 |
| 6. Demonstração ao vivo | 3 min | Integrante 3 |
| 7. Fechamento | 30 s | Integrante 1 |

---

## Antes de começar (checklist)

- [ ] PostgreSQL rodando
- [ ] Back-end rodando no IntelliJ (console mostra `Started ReforcoConnectApplication`)
- [ ] `npx ng serve` rodando e http://localhost:4200 aberto na tela **Início**
- [ ] Uma segunda aba com http://localhost:8080/professores/relatorio-engajamento (para mostrar o JSON)
- [ ] `ProfessorRepository.java` aberto no IntelliJ (para mostrar a consulta)
- [ ] Dados no estado original (8 professores · 42 agendamentos · 63 h). Se não estiverem, veja o fim do `COMO_RODAR.md`.

---

## 1. Problema e solução (1 min)

> "Hoje muitas ONGs e escolas comunitárias que oferecem reforço escolar gratuito organizam
> voluntários e aulas em planilhas, de forma manual. Isso dificulta saber quem está dando aula,
> para quem, e qual o alcance real do trabalho.
>
> O Reforço Connect é uma plataforma web que centraliza isso: as instituições cadastram seus
> professores voluntários, registram os agendamentos de aula com os alunos e acompanham, num
> painel, o engajamento de cada voluntário — quantas aulas deu e quantas horas dedicou."

## 2. O que foi pedido × o que entregamos (1 min)

> "O projeto pedia uma aplicação com banco relacional, CRUD completo das entidades, uma API REST
> com os endpoints especificados e um endpoint de agregação usando consulta nativa com COUNT e
> GROUP BY. Entregamos tudo isso funcionando, com back-end em Spring Boot, banco PostgreSQL e
> front-end em Angular."

Se quiser mostrar como checklist:

| Requisito | Onde está |
|---|---|
| Banco relacional com PK e FK | PostgreSQL: `instituicao`, `professor`, `agendamento` |
| CRUD das 3 entidades | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` em cada recurso |
| Endpoint de agregação (native query, COUNT, GROUP BY) | `GET /professores/relatorio-engajamento` |
| Arquitetura em camadas | `controller` → `service` → `repository` |
| Validação e tratamento de erros | `@Valid` + `GlobalExceptionHandler` (400 / 404 / 409) |
| Integração com o front Angular | CORS liberado para `localhost:4200` |

## 3. Modelo de dados (2 min)

> "O banco tem três tabelas ligadas por dois relacionamentos um-para-muitos.
>
> Uma **instituição** tem vários **professores**: por isso `professor` tem a chave estrangeira
> `instituicao_id`, que é NOT NULL — todo voluntário pertence obrigatoriamente a uma instituição.
>
> Um **professor** tem vários **agendamentos**: `agendamento` tem a chave estrangeira
> `professor_id`, também NOT NULL. Cada agendamento guarda o nome do aluno, a data e hora,
> o status e a duração em minutos, que usamos para calcular as horas de reforço."

**Sobre as diferenças em relação ao diagrama inicial** — fale antes que o professor pergunte:

> "Durante a implementação fizemos alguns ajustes em relação à modelagem inicial.
> O principal foi tirar o `instituicao_id` da tabela `agendamento`. Como todo agendamento já
> aponta para um professor, e todo professor já aponta para a sua instituição, guardar a
> instituição de novo no agendamento seria **redundância**: a mesma informação em dois lugares.
> Isso abre espaço para inconsistência — por exemplo, o agendamento dizer uma instituição e o
> professor dele pertencer a outra. Sem esse campo, a instituição é obtida pelo caminho
> agendamento → professor → instituição, e o modelo fica normalizado.
>
> Também acrescentamos `duracao_minutos` em `agendamento`, com valor padrão de 60, para conseguir
> calcular o total de horas atendidas, e simplificamos os atributos de instituição e professor
> para o que o sistema realmente usa."

## 4. Arquitetura do back-end (2 min)

> "O back-end é uma API REST em Java 21 com Spring Boot, organizada em três camadas:
>
> - os **controllers** recebem as requisições HTTP e devolvem as respostas com o status correto;
> - os **services** concentram as regras — por exemplo, ao cadastrar um professor, o service
>   busca a instituição pelo id e, se ela não existir, devolve 404;
> - os **repositories** usam Spring Data JPA para conversar com o PostgreSQL.
>
> As tabelas são mapeadas como entidades JPA, e o Hibernate cria e atualiza o esquema.
> Os dados de entrada são validados com Bean Validation — campos obrigatórios e tamanhos
> máximos iguais aos das colunas — e um tratador global de exceções devolve respostas
> padronizadas: 400 para dados inválidos, 404 para registro inexistente e 409 quando o banco
> recusa a operação por violar uma restrição de integridade. Assim o usuário nunca vê um erro
> 500 com stack trace."

## 5. A consulta de agregação (2 min) — a parte mais importante para Banco de Dados II

Mostre `ProfessorRepository.java` na tela.

```sql
SELECT p.id, p.nome, i.nome AS instituicao,
       COUNT(a.id)                                          AS "totalAgendamentos",
       COALESCE(ROUND(SUM(a.duracao_minutos) / 60.0, 1), 0) AS "totalHoras"
  FROM professor p
  JOIN instituicao i      ON i.id = p.instituicao_id
  LEFT JOIN agendamento a ON a.professor_id = p.id
 GROUP BY p.id, p.nome, i.nome
 ORDER BY "totalAgendamentos" DESC, p.nome;
```

> "Este é o endpoint de relatório de engajamento. É uma consulta nativa, escrita em SQL puro,
> no repositório.
>
> Ela parte de `professor`, junta a `instituicao` para mostrar o nome dela e junta os
> `agendamentos`. O **GROUP BY** agrupa todas essas linhas por professor, e aí aplicamos as
> funções de agregação: **COUNT** conta os agendamentos e **SUM** soma os minutos, que dividimos
> por 60 para ter horas.
>
> O ponto principal é o **LEFT JOIN** com agendamento. Com um INNER JOIN, um professor que ainda
> não tem nenhum agendamento simplesmente sumiria do relatório, porque não existe linha
> correspondente. Com o LEFT JOIN ele continua aparecendo, com zero.
>
> Por isso também usamos `COUNT(a.id)` e não `COUNT(*)`: no LEFT JOIN, quando não há
> agendamento, as colunas de `a` vêm nulas; `COUNT(a.id)` ignora nulos e dá 0, enquanto
> `COUNT(*)` contaria a linha e daria 1. E como o SUM de nenhuma linha é nulo, o **COALESCE**
> transforma em 0.
>
> Já com `instituicao` podemos usar INNER JOIN sem perder ninguém, porque `instituicao_id` é
> NOT NULL: todo professor tem instituição.
>
> O resultado não devolve a entidade Professor inteira: usamos uma **projeção**, que traz só
> as colunas do relatório."

## 6. Demonstração ao vivo (3 min)

1. **Tela Início** (já aberta)
   > "Esta é a tela inicial. Os três cards e a tabela vêm todos do mesmo endpoint de agregação.
   > Temos 8 professores, 42 agendamentos e 63 horas de reforço."

   Aponte para o **André Ribeiro** no fim da tabela:
   > "O André ainda não tem nenhum agendamento e aparece com zero, em cinza. Essa linha é a prova
   > visual do LEFT JOIN."

2. **Aba do JSON** (`localhost:8080/professores/relatorio-engajamento`)
   > "Este é o retorno cru da API: uma linha por professor, com o total de agendamentos e horas."

3. **Professores → + Novo professor → Salvar** sem preencher
   > "Se tentarmos salvar vazio, quem recusa é o back-end: a validação devolve 400 com a lista
   > dos campos obrigatórios."

   Preencha (ex.: *Carlos Pereira · Química · Centro Comunitário Sementes*) → **Salvar**.

4. **Agendamentos → + Novo agendamento** → aluno qualquer, professor *Carlos Pereira*, uma data,
   **90** minutos → **Salvar**.

5. Volte em **Início**
   > "O Carlos agora aparece com 1 agendamento e 1,5 hora, e os totais subiram para 43
   > agendamentos e 64,5 horas — a consulta foi executada de novo no banco."

6. (Opcional) **Professores → editar** um professor e trocar a instituição → mostra o `PUT`
   atualizando a chave estrangeira.

7. **Professores → excluir** o Carlos → confirmar.
   > "Ao excluir o professor, os agendamentos dele são removidos em cascata."

> ⚠️ **Não exclua uma instituição** durante a demonstração: a cascata apaga todos os professores
> dela e os agendamentos deles.

## 7. Fechamento (30 s)

> "Resumindo: o Reforço Connect tem um banco relacional normalizado com chaves estrangeiras e
> restrições de integridade, uma API REST completa em camadas, um relatório analítico com SQL
> agregado e um front-end que consome tudo isso. O próximo passo natural seria incluir o lado
> do aluno, com cadastro e autenticação. Obrigado!"

---

## Perguntas prováveis do professor

**Por que LEFT JOIN e não INNER JOIN?**
Para não perder professores sem agendamento. O INNER JOIN só devolve linhas com correspondência
nas duas tabelas; o LEFT JOIN mantém todas as linhas da tabela da esquerda (`professor`).

**Por que COUNT(a.id) e não COUNT(\*)?**
`COUNT(*)` conta linhas, inclusive a linha "vazia" que o LEFT JOIN cria; daria 1 para quem não
tem agendamento. `COUNT(coluna)` ignora NULL e dá 0.

**Para que serve o COALESCE?**
`SUM` sobre nenhuma linha devolve NULL, não zero. O `COALESCE(valor, 0)` troca o NULL por 0.

**Por que precisa do GROUP BY com p.id, p.nome e i.nome?**
No SQL padrão, toda coluna do SELECT que não está dentro de uma função de agregação precisa
estar no GROUP BY.

**Por que não tem instituicao_id em agendamento, se o diagrama inicial tinha?**
Seria redundante: a instituição já é alcançada por agendamento → professor → instituição.
Duplicar permitiria dados inconsistentes. Removemos para manter o modelo normalizado.

**O que acontece se apagar uma instituição?**
O relacionamento está com `cascade = ALL` no JPA: apaga os professores dela e os agendamentos
desses professores. A interface pede confirmação e avisa disso.

**O que acontece se cadastrar um professor com uma instituição que não existe?**
O service busca a instituição pelo id antes de salvar; se não existir, a API responde 404 com
mensagem. Se mesmo assim o banco recusar algo (restrição de integridade), responde 409.

**Por que native query e não JPQL / método do Spring Data?**
Era requisito do projeto, e deixa explícito o SQL que roda no PostgreSQL — JOIN, agregação e
agrupamento ficam visíveis e controlados por nós.

**O que é a projeção?**
Uma interface (`RelatorioEngajamento`) com um getter para cada coluna da consulta. O Spring
preenche e devolve só esses campos, em vez da entidade completa.

**Como as horas são calculadas?**
Cada agendamento tem `duracao_minutos` (padrão 60). A consulta soma os minutos do professor,
divide por 60.0 (o `.0` evita divisão inteira) e arredonda para uma casa decimal.

**Como as tabelas foram criadas?**
Pelo Hibernate, a partir das entidades JPA (`ddl-auto=update`). As chaves estrangeiras e os
NOT NULL vêm das anotações `@ManyToOne`, `@JoinColumn(nullable = false)` e
`@Column(nullable = false)`. Os dados de exemplo entram pelo `data.sql`.

**Por que o front e o back rodam em portas diferentes? Isso não dá problema?**
Por padrão o navegador bloquearia (política de mesma origem). Liberamos a origem do Angular,
`http://localhost:4200`, no `CorsConfig` do back.
