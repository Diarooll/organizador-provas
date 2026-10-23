# Organizador de provas

App local pra organizar provas da faculdade (FIAP). Projeto de estudo: o usuário quer entender cada passo, não só receber código pronto.

## Stack
- Backend: Spring Boot 4.1, Java 21, Maven, em `backend/`
- Banco: Postgres 16 no Docker (`docker-compose.yml` na raiz), porta 5433
- Migrations: Flyway, em `backend/src/main/resources/db/migration`
- Front (a fazer): React em `frontend/`

## Modelo de dados
materia 1:N avaliacao 1:N tarefa

## Convenções
- `ddl-auto: validate`: quem cria tabela é o Flyway
- Nunca editar migration já aplicada: criar uma nova (V2__...)
- Explicar o que cada coisa faz antes de pedir pra executar

## Plano atual
API REST do backend: `docs/plano-api.md`. O usuário digita o código; o Claude explica cada passo e revisa.
Status: Passo 1 (entidades JPA) concluído. Passo 2 (repositories) em andamento.
