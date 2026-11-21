# Reforço Connect — Back-end

API REST (Java 21 + Spring Boot 4 + PostgreSQL) da plataforma Reforço Connect.

## 1. Preparar o banco (uma vez só)

Com o PostgreSQL instalado, abra o `psql` como superusuário:

- **Windows:** menu Iniciar → "SQL Shell (psql)" → Enter em tudo e a senha do usuário `postgres` definida na instalação
- **Linux:** `sudo -u postgres psql`

E rode:

```sql
CREATE USER aluno WITH PASSWORD 'aluno';
CREATE DATABASE reforco_connect OWNER aluno;
```

As tabelas são criadas pelo Hibernate (`ddl-auto=update`) e os dados de exemplo
entram pelo `src/main/resources/data.sql` na primeira execução.

## 2. Rodar

```
./mvnw spring-boot:run        # Linux
mvnw.cmd spring-boot:run      # Windows
```

API em `http://localhost:8080`.

## 3. Arquitetura (3 camadas)

`controller` (HTTP) → `service` (regras: busca por id, 404, vínculos) → `repository` (Spring Data JPA)  
`exception/GlobalExceptionHandler` padroniza os erros: 400 (validação / JSON), 404 (id inexistente), 409 (integridade).

## 4. Endpoints

| Recurso | Rotas |
|---|---|
| `/instituicoes` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| `/professores` | idem + `GET /professores/relatorio-engajamento` |
| `/agendamentos` | idem |

Exemplos de corpo:

```json
POST /professores
{ "nome": "Rafael Mendes", "disciplina": "Matemática", "fotoUrl": null, "instituicao": { "id": 1 } }

POST /agendamentos
{ "alunoNome": "Ana Clara", "dataHora": "2026-11-25T14:30:00", "duracaoMinutos": 60,
  "status": "Agendado", "professor": { "id": 1 } }
```

`duracaoMinutos` é opcional (padrão 60).

## 5. Relatório de engajamento (native query)

`ProfessorRepository.relatorioEngajamento()`:

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

- **LEFT JOIN** em `agendamento`: professor sem agendamento continua no resultado (com INNER JOIN ele sumiria).
- **COUNT(a.id)** e não `COUNT(*)`: no LEFT JOIN sem correspondência as colunas de `a` vêm NULL; `COUNT(a.id)` dá 0, `COUNT(*)` daria 1.
- **COALESCE**: `SUM` de nenhuma linha é NULL; vira 0.
- **INNER JOIN** em `instituicao` é seguro porque `professor.instituicao_id` é NOT NULL.
- O retorno usa uma **projeção** (`dto/RelatorioEngajamento`), não a entidade.

Nos dados de exemplo, André Ribeiro não tem agendamentos e aparece com 0.

## Atenção

`cascade = ALL`: apagar uma instituição apaga os professores dela e os agendamentos deles.
