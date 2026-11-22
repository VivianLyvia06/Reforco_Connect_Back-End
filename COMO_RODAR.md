# Como rodar o Reforço Connect na sua máquina

Passo a passo do zero, escrito para Windows (com as diferenças do Linux quando houver).
Faça **uma parte por vez** e só siga para a próxima quando a conferência (✅) der certo.

No fim você terá três coisas rodando ao mesmo tempo:

| O quê | Onde | Endereço |
|---|---|---|
| Banco PostgreSQL | em segundo plano | porta 5432 |
| Back-end (Spring Boot) | IntelliJ | http://localhost:8080 |
| Front-end (Angular) | terminal | http://localhost:4200 |

As Partes 1 a 7 são feitas **uma vez só**. Nos outros dias, vá direto para
"[Nos próximos dias](#nos-próximos-dias)".

---

## Parte 1 — Baixar o projeto do GitHub

Se ainda não tiver o Git: `winget install Git.Git` (feche e abra o terminal depois).

Escolha uma pasta (de preferência no disco **C:**, ver Parte 7) e rode:

```
git clone https://github.com/VivianLyvia06/Reforco_Connect_Back-End.git
```

Vai aparecer a pasta `Reforco_Connect_Back-End`, com o back-end na raiz e o
front-end dentro de `reforco-connect-front`.

✅ Existe o arquivo `Reforco_Connect_Back-End\pom.xml` e a pasta `Reforco_Connect_Back-End\reforco-connect-front`.

> **Para enviar alterações** (`git push`) você precisa ser colaborador do repositório.
> A Vivian adiciona em GitHub → repositório → **Settings → Collaborators → Add people**.
> Para só baixar e rodar, não precisa.

---

## Parte 2 — Instalar o PostgreSQL

Confira se já existe: `psql --version`. Se aparecer uma versão, pule para a Parte 3.

**Windows:** baixe o instalador em https://www.postgresql.org/download/windows/ e clique em Next em tudo, com duas atenções:
- quando pedir a **senha do usuário `postgres`**, escolha uma e **anote**;
- deixe a porta **5432**. No final, desmarque o "Stack Builder".

**Linux:** `sudo apt install postgresql`

✅ No menu Iniciar existe o programa **"SQL Shell (psql)"**.

---

## Parte 3 — Criar o usuário e o banco

O back-end se conecta como usuário `aluno`, senha `aluno`, no banco `reforco_connect`.

Abra o **SQL Shell (psql)**. Aperte **Enter** em Server, Database, Port e Username, e digite a
senha da Parte 2 (ela **não aparece** enquanto você digita — é normal).
No Linux: `sudo -u postgres psql`.

No prompt `postgres=#`, rode **um comando de cada vez** (o `;` faz parte):

```sql
CREATE USER aluno WITH PASSWORD 'aluno';
```
```sql
CREATE DATABASE reforco_connect OWNER aluno;
```

Respostas esperadas: `CREATE ROLE` e `CREATE DATABASE`. Saia com `\q`.

> **Se aparecer "o banco de dados reforco_connect já existe"**, apague e crie de novo
> (os dados de exemplo voltam sozinhos):
> ```sql
> DROP DATABASE reforco_connect;
> ```
> ```sql
> CREATE DATABASE reforco_connect OWNER aluno;
> ```

✅ `\l reforco_connect` mostra `aluno` na coluna Proprietário/Owner.

Você **não** cria tabelas: o back-end cria tudo sozinho na primeira execução.

---

## Parte 4 — Java 21

```
java -version
```

Precisa mostrar **21** ou mais. Se não: `winget install Microsoft.OpenJDK.21` (Linux: `sudo apt install openjdk-21-jdk`).
O IntelliJ também consegue baixar um JDK sozinho se pedir.

---

## Parte 5 — Rodar o back-end no IntelliJ

1. IntelliJ → **File → Open...** → selecione a pasta **`Reforco_Connect_Back-End`**
   (a que tem o `pom.xml`) → **Open as Project** → **Trust Project**.

   ⚠️ Abra exatamente essa pasta. Se abrir a pasta de cima, o IntelliJ não reconhece o
   projeto: o botão ▶ fica cinza com "Current File" e não aparece a setinha verde no `main`.
2. Espere a barra de progresso do rodapé terminar (na 1ª vez o Maven baixa as dependências).
3. Abra `src/main/java/com/vivian/reforco_connect/ReforcoConnectApplication.java` e clique no
   **▶ verde** ao lado do `main` → **Run**.
4. Espere aparecer no console: `Started ReforcoConnectApplication`.

**Deixe rodando.** Se fechar o IntelliJ, o back-end para.

✅ Abra http://localhost:8080/professores/relatorio-engajamento — deve aparecer um JSON com
8 professores (Rafael Mendes com 12 agendamentos no topo, André Ribeiro com 0 no fim).

<details>
<summary>Alternativa sem IntelliJ (terminal)</summary>

Dentro da pasta `Reforco_Connect_Back-End`:
- Windows (PowerShell): `.\mvnw.cmd spring-boot:run`
- Linux: `chmod +x mvnw` (uma vez) e `./mvnw spring-boot:run`
</details>

---

## Parte 6 — Instalar o Node.js

```
node -v
```

Se não for reconhecido: `winget install OpenJS.NodeJS.LTS` (ou o instalador LTS de https://nodejs.org).

⚠️ **Depois de instalar, feche o IntelliJ inteiro e abra de novo.** O terminal do IntelliJ só
enxerga programas que já existiam quando ele abriu. Se ainda assim `node -v` não funcionar,
reinicie o computador.

✅ `node -v` e `npm -v` mostram versões.

---

## Parte 7 — Rodar o front-end

> ⚠️ **Use o disco C:.** Numa das máquinas da equipe o `npm install` travou por 20 minutos no
> disco E: com o aviso `TAR_ENTRY_ERROR UNKNOWN: unknown error, write` — o Windows não deixava
> gravar os arquivos. Copiando a pasta `reforco-connect-front` para `C:\dev\` funcionou em
> poucos minutos. Se o seu clone estiver em outro disco, copie só a pasta do front para
> `C:\dev\reforco-connect-front` (sem a subpasta `node_modules`, se existir).

No IntelliJ, abra a aba **Terminal** (ícone `>_` no rodapé) → **+**, e rode um de cada vez:

```
cd C:\dev\reforco-connect-front
```
```
npm install
```

O `npm install` é só na primeira vez. Ele termina com `found 0 vulnerabilities`.
Os avisos amarelos `npm warn install-scripts ...` podem ser ignorados.

```
npx ng serve
```

Se perguntar sobre "pseudonymous usage data"/analytics, responda `N`.
Quando aparecer `Local: http://localhost:4200/`, abra esse endereço no navegador.

✅ Aparece o **Painel de engajamento** com os cards **08 · 42 · 63h** e a tabela dos professores.

---

## Parte 8 — Teste rápido (faça antes da apresentação)

1. **Professores → + Novo professor → Salvar** sem preencher → aparece o erro em vermelho
   (validação do back, HTTP 400).
2. Preencha nome, disciplina e instituição → **Salvar** → aparece na tabela.
3. **Agendamentos → + Novo agendamento** com esse professor, uma data e 90 min → **Salvar**.
4. **Início** → o professor novo aparece com 1 agendamento e 1,5 h; os cards mudam.
5. **Professores** → exclua o professor de teste → o agendamento dele some junto (cascata).

---

## Nos próximos dias

1. IntelliJ → ▶ no `ReforcoConnectApplication` → espere `Started ...`
2. Terminal:
   ```
   cd C:\dev\reforco-connect-front
   ```
   ```
   npx ng serve
   ```
3. Abra http://localhost:4200

Para receber atualizações que alguém da equipe enviou: `git pull` na pasta `Reforco_Connect_Back-End`.

---

## Problemas comuns

| Mensagem | Solução |
|---|---|
| `password authentication failed for user "aluno"` | Refaça a Parte 3. |
| `database "reforco_connect" does not exist` | Falta o `CREATE DATABASE` da Parte 3. |
| `Connection refused` (porta 5432) | PostgreSQL parado. Windows: "Serviços" → iniciar `postgresql-x64-…`. Linux: `sudo systemctl start postgresql`. |
| `Port 8080 was already in use` | Já tem um back-end rodando (outra janela/terminal). Feche-o. |
| ▶ cinza / "Current File" no IntelliJ | Abriu a pasta errada. Veja a Parte 5, item 1. |
| `'npm' não é reconhecido` | Node não instalado, ou IntelliJ aberto antes da instalação. Parte 6. |
| `npx.ps1 não pode ser carregado` / scripts desabilitados | No PowerShell: `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned` → responda `S`. |
| `TAR_ENTRY_ERROR ... write` ou `npm install` parado por muito tempo | Rode o front no disco C: (Parte 7). |
| Front mostra "Não foi possível conectar à API" | O back-end não está rodando. Parte 5. |

**Voltar os dados de exemplo ao original:** pare o back-end, rode no psql (como `postgres`)
`DROP DATABASE reforco_connect;` e `CREATE DATABASE reforco_connect OWNER aluno;`, e rode o back-end de novo.
