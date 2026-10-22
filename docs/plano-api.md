# Plano: API REST do Organizador de Provas (backend)

## Contexto
O banco já existe: o Flyway aplicou a `V1__criar_tabelas.sql` (`materia`, `avaliacao`, `tarefa`), mas o backend só tem o `BackendApplication`. Agora vamos criar a API REST em cima dessas tabelas. Como é um projeto de estudo, **o Diego digita o código**. Em cada passo o Claude explica o que vai ser feito e por quê, e no final do passo revisa o que foi escrito e roda a verificação.

Escopo decidido:
- CRUD de matéria, avaliação e tarefa.
- Listar as provas próximas.
- Filtrar avaliações por matéria e por status.
- O front em React fica para outro plano.

## Estrutura (pacote `br.com.provas.backend`, organizada por camada)
```
model/       Materia, Avaliacao, Tarefa (@Entity) + enums TipoAvaliacao, StatusAvaliacao
repository/  MateriaRepository, AvaliacaoRepository, TarefaRepository (JpaRepository)
dto/         records de entrada (com Bean Validation) e de saída
service/     regras de negócio + conversão entidade <-> DTO
controller/  @RestController
exception/   RecursoNaoEncontradoException + @RestControllerAdvice
```

## Passos (cada um termina com revisão do código + verificação)

**1. Entidades JPA**: `model/`
- Os campos têm que bater exatamente com a V1, senão o `ddl-auto: validate` derruba a aplicação. Isso é um bom momento pra ver esse erro e entender o que ele diz.
- Os ids usam `@GeneratedValue(strategy = IDENTITY)`, porque as colunas são `BIGSERIAL`.
- `Avaliacao.materia` é `@ManyToOne(fetch = LAZY)`, e `Tarefa.avaliacao` também.
- Os enums usam `@Enumerated(EnumType.STRING)`.
  - `TipoAvaliacao` = PROVA, CHECKPOINT, GLOBAL_SOLUTION, CHALLENGE, TRABALHO *(decisão do Claude, os nomes vêm da FIAP. Dá pra ajustar)*.
  - `StatusAvaliacao` = PENDENTE, CONCLUIDA *(decisão do Claude. O default do banco é 'PENDENTE')*.
- Verificação: `./mvnw spring-boot:run` sobe sem erro de schema validation.

**2. Repositories**: `repository/`
- `JpaRepository<Entidade, Long>` para as três entidades.
- Em `AvaliacaoRepository`, query methods derivados do nome:
  - `findByMateriaIdAndStatus(...)`, `findByMateriaId(...)`, `findByStatus(...)`
  - `findByDataBetweenOrderByDataAsc(LocalDate, LocalDate)`, usado pelas provas próximas
- Em `TarefaRepository`: `findByAvaliacaoId(Long)`.

**3. DTOs**: `dto/` (Java `record`)
- Entrada:
  - `MateriaRequest(@NotBlank @Size(max=100) nome)`
  - `AvaliacaoRequest(@NotBlank titulo, @NotNull tipo, @NotNull data, conteudo, @NotNull materiaId)`
  - `TarefaRequest(@NotBlank descricao)`
- Saída:
  - `MateriaResponse`
  - `AvaliacaoResponse`, que traz `materiaId` e `materiaNome`
  - `TarefaResponse`
- Ponto para explicar: por que não devolver a `@Entity` direto no JSON (lazy loading, `open-in-view: false` e o risco de expor campos).

**4. Exceções**: `exception/`
- `RecursoNaoEncontradoException` vira 404.
- `MethodArgumentNotValidException` vira 400, com a lista dos campos inválidos.
- `DataIntegrityViolationException` vira 409. Isso acontece ao apagar uma matéria que ainda tem avaliações, porque a FK não tem `ON DELETE CASCADE`.
- Tudo isso fica num único `@RestControllerAdvice`.

**5. Matéria (service + controller)**: primeira fatia completa, servindo de modelo para as outras.

| Método | Rota | Resposta |
|---|---|---|
| GET | `/materias` | 200 |
| GET | `/materias/{id}` | 200 / 404 |
| POST | `/materias` | 201 + `Location` |
| PUT | `/materias/{id}` | 200 |
| DELETE | `/materias/{id}` | 204 / 409 |

- Ponto para explicar: `@Transactional` no service.

**6. Avaliação**
- O CRUD em `/avaliacoes` segue o mesmo padrão do passo 5. O `materiaId` do request é resolvido com `materiaRepository.findById`, e se a matéria não existir a resposta é 404.
- `GET /avaliacoes?materiaId=&status=`: os dois parâmetros são opcionais. O service escolhe o query method conforme o que vier.
- `GET /avaliacoes/proximas?dias=7`: lista do intervalo de hoje até hoje+dias, ordenada por data. O padrão é 7 dias.
- `PATCH /avaliacoes/{id}/status`: muda entre PENDENTE e CONCLUIDA.

**7. Tarefa**
- `GET /avaliacoes/{avaliacaoId}/tarefas` e `POST /avaliacoes/{avaliacaoId}/tarefas`.
- `PUT /tarefas/{id}`, `PATCH /tarefas/{id}/concluida` (alterna entre feita e não feita) e `DELETE /tarefas/{id}`.
- Aqui o `ON DELETE CASCADE` da V1 faz com que apagar uma avaliação apague as tarefas dela também.

**8. Testes**
- `@WebMvcTest` do `MateriaController`, com o service mockado: casos de 201, 400 e 404. No Boot 4 o import é `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`.
- O `contextLoads` que já existe continua usando o Postgres do Docker.

## Arquivos
- Novos: tudo dentro de `backend/src/main/java/br/com/provas/backend/{model,repository,dto,service,controller,exception}` e `backend/src/test/java/.../controller/MateriaControllerTest.java`.
- Sem migration nova. As tabelas da V1 já cobrem o escopo.
- A cada passo concluído, um commit pequeno (o repositório git já existe, ainda sem nenhum commit).

## Verificação ponta a ponta
1. `docker compose up -d`, depois `cd backend && ./mvnw test`, que deve terminar verde.
2. `./mvnw spring-boot:run` e testar com curl:
   - criar uma matéria, depois uma avaliação com data daqui a 3 dias, depois 2 tarefas.
   - `GET /avaliacoes/proximas?dias=7` mostra a avaliação.
   - `GET /avaliacoes?status=PENDENTE&materiaId=1` filtra.
   - `POST /materias` com `nome` vazio retorna 400.
   - `GET /materias/999` retorna 404.
   - `DELETE /materias/1` enquanto ela tem avaliação retorna 409.
   - `DELETE /avaliacoes/{id}` some com as tarefas.
3. Conferir no banco: `docker exec provas-db psql -U diego -d provas -c 'select * from avaliacao'`.
