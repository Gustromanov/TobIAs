# TobIAs

Projeto web usando **Java com Servlets/JSP**, **PostgreSQL**, **Flyway** e **PgAdmin**, rodando com **Docker Compose**.
## 👨‍💻 Equipe

- 🚀 Gustavo Batista — [@Gustromanov](https://github.com/Gustromanov)
- 🚀 Arthur Guimaraes — [@arthur-Gm](https://github.com/arthur-Gm)
- 🚀 Reinaldo Junior — [@reinaldo-a](https://github.com/reinaldo-a)
- 🚀 Pedro Luiz — [@P-Luiz](https://github.com/P-Luiz)

### Instituição
Instituto Federal Baiano (IF Baiano)
- [Trilha de Aprendizagem](docs/trilha_aprendizagem.md)

---

## Tecnologias

- Java (OpenJDK 21)
- PostgreSQL
- PgAdmin
- Docker
- Docker Compose
- Maven

---

## Estrutura do Projeto

A estrutura do projeto foi mantida simples para facilitar o entendimento. Para projetos pequenos, o importante é separar claramente:

- configuração de infraestrutura (`docker/`)
- código da aplicação (`src/main/java/`)
- views JSP e assets web (`src/main/webapp/`)
- documentação (`docs/`)

Estrutura atual:

```
tobias/
├── docker/
│   ├── docker-compose.yml
│   ├── Dockerfile
│   └── .dockerignore
├── docs/
│   ├── documento_de_requisitos/
│   ├── documento_de_visão/
│   └── padroes-dev/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/tobias/
│       │       ├── application/
│       │       ├── config/
│       │       └── controller/
│       ├── resources/
│       │   └── bd/migration/
│       └── webapp/
│           ├── WEB-INF/
│           └── assets/
├── .env
├── pom.xml
└── README.md
```

> Para projetos pequenos, usamos pacotes simples como `com.tobias`. A organizacao atual separa entrada da aplicacao (`application`), configuracoes (`config`) e servlets (`controller`).

---

## Pré-requisitos

- Docker
- Docker Compose

Verificar instalação:

```bash
docker --version
docker compose version
```

---

## Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
POSTGRES_USER=tobias_user
POSTGRES_PASSWORD=Pombadosertao
POSTGRES_DB=tobias_db
DB_HOST=db
DB_PORT=5432

PGADMIN_DEFAULT_EMAIL=admin@admin.com
PGADMIN_DEFAULT_PASSWORD=admin

GOOGLE_API_KEY=sua_chave_da_api_do_google
GEMINI_MODEL=gemini-2.5-flash
```

`GOOGLE_API_KEY` é usada nas funcionalidades de IA, como geração de questões e relatório de atividade. O projeto também aceita `GEMINI_API_KEY` por compatibilidade.

### Como criar a chave da API do Google/Gemini

1. Acesse [Google AI Studio API Keys](https://aistudio.google.com/apikey).
2. Faça login com uma conta Google.
3. Clique em **Create API key** ou **Criar chave de API**.
4. Se o Google pedir um projeto, selecione um projeto existente ou deixe o AI Studio criar um automaticamente.
5. Copie a chave gerada.
6. Cole a chave no arquivo `.env` da raiz do projeto:

```env
GOOGLE_API_KEY=cole_sua_chave_aqui
GEMINI_MODEL=gemini-2.5-flash
```

Depois de alterar o `.env`, reinicie a aplicação para a nova chave ser carregada.

> Não envie a chave da API para o GitHub. Ela deve ficar apenas no `.env`.

---

## Como Rodar o Projeto

```bash
cd docker
docker compose up --build
```

Esse comando vai:

1. Baixar as imagens
2. Criar os containers
3. Compilar o Java com Maven
4. Publicar a aplicacao no Tomcat

Depois do primeiro build, para subir novamente:

```bash
cd docker
docker compose up
```

O `docker-compose.yml` ja inclui os volumes de desenvolvimento. Arquivos JSP, CSS e JS montados por volume atualizam sem rebuild da imagem.

Quando alterar classes Java, recompile dentro do container:

```bash
docker compose exec app mvn -f /workspace/pom.xml compile
docker compose restart app
```

O comando precisa apontar para `/workspace/pom.xml` porque, dentro do container, o Tomcat roda em `/usr/local/tomcat/webapps`, mas o projeto Maven fica montado em `/workspace`.

---

## Containers

| Container    | Função            |
|-------------|-------------------|
| java_app    | aplicação Java    |
| postgres_db | banco PostgreSQL  |
| pgadmin     | interface gráfica |

---

## Conexão com o Banco (Java)

`jdbc:postgresql://db:5432/tobias_db`

---

## Importante

Dentro do Docker:

- Não use `localhost`
- Use `db` como host do banco

---

## Acessar PgAdmin

Abra no navegador:

`http://localhost:5050`

Login:

```
Email: admin@admin.com
Senha: admin
```

---

## Configurar o Banco no PgAdmin

Após logar:

Clique em **Add New Server**

### Aba General

```
Name: PostgreSQL
```

### Aba Connection

```
Host: db
Port: 5432
Database: tobias_db
Username: tobias_user
Password: Pombadosertao
```

Clique em **Save**

---

## Comandos úteis

### Subir projeto

```bash
docker compose up
```

### Rebuildar a imagem da aplicação

```bash
docker compose up -d --build --force-recreate app
```

### Parar containers

```bash
docker compose down
```

### Apagar tudo (inclui banco)

```bash
docker compose down -v
```

---

## Persistência de Dados

Os dados do PostgreSQL ficam armazenados em um volume Docker:

```
postgres_data
```

Isso garante que os dados não sejam perdidos ao reiniciar os containers.

---

## Maven

O container da aplicacao inclui Maven para facilitar o desenvolvimento.

Para compilar classes Java dentro do container:

```bash
cd docker
docker compose exec app mvn -f /workspace/pom.xml compile
```

Em seguida, reinicie o Tomcat:

```bash
docker compose restart app
```

Se quiser limpar classes antigas antes de compilar:

```bash
docker compose exec app mvn -f /workspace/pom.xml clean compile
docker compose restart app
```

Nao rode `mvn compile` dentro de `/usr/local/tomcat/webapps`, porque essa pasta nao tem `pom.xml`. Use sempre:

```bash
mvn -f /workspace/pom.xml compile
```

---

## Observações

- Projeto usa **Servlets/JSP sobre Tomcat**
- Estrutura baseada em **MVC**
- Conexão com banco via **JDBC**
- Migrações com **Flyway**
- Variáveis carregadas via **.env**
- Containers isolam todo o ambiente
