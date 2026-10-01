# Arquitetura — Mercadinho Astro

Este documento define **como o código deve ser organizado** e as **convenções** que todo código novo segue.
Se algo aqui mudar, atualize este arquivo no mesmo commit da mudança.

---

## 1. Visão geral

API REST para gestão de um mercadinho de bairro.

| Item           | Escolha                              |
|----------------|--------------------------------------|
| Linguagem      | Java 17                              |
| Framework      | Spring Boot 3.5 (Web, Data JPA, Validation) |
| Banco de dados | PostgreSQL                           |
| Migrations     | Flyway                               |
| Mapeamento     | MapStruct (DTO ⇄ entidade)           |
| Boilerplate    | Lombok                               |
| Testes         | JUnit 5 + Mockito                    |
| Build          | Maven (wrapper `mvnw`)               |

### Módulos (escopo)

| Módulo                   | Responsabilidade                                                    | Status          |
|--------------------------|---------------------------------------------------------------------|-----------------|
| **Cliente**              | Cadastro de clientes (nome, CPF, telefone, endereço)                | Em andamento    |
| **Produto e estoque**    | Produtos, categorias, quantidade em estoque, entradas/saídas        | Planejado       |
| **Venda / caixa (PDV)**  | Registro de venda com itens, forma de pagamento, fechamento de caixa | Planejado       |
| **Fiado**                | Conta do cliente: compras a prazo, débitos e pagamentos             | Planejado       |
| **Usuário e login**      | Funcionários, perfis (ex.: ADMIN, CAIXA) e autenticação             | Planejado       |

Dependências esperadas entre módulos: **Venda** usa **Produto** (baixa de estoque) e **Cliente**;
**Fiado** usa **Cliente** e **Venda**; **Usuário** é transversal (quem registrou a venda, permissões).

---

## 2. Arquitetura em camadas

O projeto segue **arquitetura em camadas**. Cada camada só conhece a camada imediatamente abaixo.

```
 Requisição HTTP
       │
       ▼
┌──────────────┐   recebe/retorna DTO, valida entrada (@Valid), define status HTTP
│  Controller  │
└──────┬───────┘
       ▼
┌──────────────┐   regras de negócio, transações, lança exceções de domínio
│   Service    │ ──► Mapper (DTO ⇄ Model)
└──────┬───────┘
       ▼
┌──────────────┐   acesso a dados (Spring Data JPA)
│  Repository  │
└──────┬───────┘
       ▼
   PostgreSQL
```

### Responsabilidades e regras de cada camada

| Camada         | Pode                                                                 | Não pode                                                       |
|----------------|----------------------------------------------------------------------|----------------------------------------------------------------|
| **controller** | Receber DTO, validar com `@Valid`, chamar o service, montar `ResponseEntity` | Ter regra de negócio; acessar repository; receber/retornar entidade |
| **service**    | Regras de negócio, `@Transactional`, chamar repositories e mappers, lançar exceções | Conhecer HTTP (`ResponseEntity`, `HttpStatus`, `HttpServletRequest`) |
| **repository** | Interfaces `JpaRepository` e consultas (`@Query`, query methods)     | Ter lógica de negócio                                          |
| **model**      | Entidades JPA que espelham as tabelas                                | Ser exposta pela API                                           |
| **dto**        | Objetos de entrada/saída da API, com anotações de validação          | Ter anotações JPA                                              |
| **mapper**     | Converter DTO ⇄ Model (MapStruct)                                    | Ter regra de negócio ou acessar banco                          |
| **exception**  | Exceções de domínio e o handler global                               | —                                                              |

**Regra principal:** entidade (`*Model`) nunca sai do service. O controller só enxerga DTOs.

---

## 3. Organização de pacotes

Organização **por camada**. Pacote base: `com.dev.mercadinhoastro.web`.

```
com.dev.mercadinhoastro.web
├── WebApplication.java
├── config/          # configurações do Spring (segurança, CORS, beans)
├── controller/      # ClienteController, ProdutoController, ...
├── service/         # ClienteService, ProdutoService, ...
├── repository/      # ClienteRepository, ...
├── model/           # ClienteModel, ProdutoModel, ... (entidades JPA)
├── dto/             # ClienteDTO, ...
├── mapper/          # ClienteMapper, ... (interfaces MapStruct)
└── exception/       # exceções de domínio + GlobalExceptionHandler
```

```
src/main/resources
├── application.yaml
└── db/migration/    # scripts Flyway (V1__..., V2__...)
```

---

## 4. Convenções de nomenclatura

### Código

| Elemento        | Padrão                         | Exemplo                       |
|-----------------|--------------------------------|-------------------------------|
| Entidade        | `<Nome>Model`                  | `ClienteModel`                |
| DTO             | `<Nome>DTO`                    | `ClienteDTO`                  |
| Repository      | `<Nome>Repository`             | `ClienteRepository`           |
| Service         | `<Nome>Service`                | `ClienteService`              |
| Controller      | `<Nome>Controller`             | `ClienteController`           |
| Mapper          | `<Nome>Mapper`                 | `ClienteMapper`               |
| Exceção         | `<Motivo>Exception`            | `RecursoNaoEncontradoException` |
| Métodos service | verbo no infinitivo, em português | `cadastrar`, `buscarPorId`, `listar`, `atualizar`, `remover` |

O código (classes, métodos, variáveis) é escrito **em português**, no domínio do negócio.

Se um recurso precisar de formatos diferentes de entrada e saída (ex.: senha só na entrada),
divida em `<Nome>RequestDTO` e `<Nome>ResponseDTO`.

### Banco de dados

| Elemento | Padrão                         | Exemplo                 |
|----------|--------------------------------|-------------------------|
| Tabela   | `tb_<nome>` no singular, snake_case | `tb_cliente`, `tb_item_venda` |
| Coluna   | snake_case                     | `nome_completo`         |
| Chave primária | `id BIGSERIAL`           | —                       |
| Chave estrangeira | `<tabela>_id`         | `cliente_id`            |

---

## 5. API REST

### URLs

Recursos **no plural**, sem verbo na URL. O verbo HTTP define a ação.

| Ação              | Método   | URL                 | Sucesso |
|-------------------|----------|---------------------|---------|
| Cadastrar         | `POST`   | `/clientes`         | `201 Created` + corpo criado |
| Listar            | `GET`    | `/clientes`         | `200 OK` |
| Buscar por id     | `GET`    | `/clientes/{id}`    | `200 OK` |
| Atualizar         | `PUT`    | `/clientes/{id}`    | `200 OK` |
| Remover           | `DELETE` | `/clientes/{id}`    | `204 No Content` |

Ações que não são CRUD viram sub-recursos: `POST /vendas/{id}/cancelamento`,
`POST /clientes/{id}/fiado/pagamentos`.

Recursos com nome composto usam kebab-case: `/itens-venda`.

### Formato de erro

Todos os erros passam pelo `GlobalExceptionHandler` (`@RestControllerAdvice`) e respondem no mesmo formato:

```json
{
  "timestamp": "2026-10-01T14:30:00",
  "status": 400,
  "erro": "Requisição inválida",
  "mensagem": "Um ou mais campos estão inválidos",
  "caminho": "/clientes",
  "campos": [
    { "campo": "cpf", "mensagem": "número do registro de contribuinte individual brasileiro (CPF) inválido" }
  ]
}
```

`campos` só aparece em erros de validação.

| Exceção                          | Status HTTP | Quando                                         |
|----------------------------------|-------------|------------------------------------------------|
| `MethodArgumentNotValidException`| `400`       | Falha no `@Valid` do DTO                        |
| `RecursoNaoEncontradoException`  | `404`       | Busca por id que não existe                     |
| `RegraNegocioException`          | `422`       | Regra violada (ex.: CPF já cadastrado, estoque insuficiente) |
| `Exception` (genérica)           | `500`       | Erro inesperado — logar e responder mensagem genérica |

Regras de negócio são verificadas no **service**, que lança a exceção; o controller não usa `try/catch`.

---

## 6. Mapeamento DTO ⇄ Model (MapStruct)

Mappers são interfaces anotadas com `@Mapper(componentModel = "spring")` e são injetadas no service.

```java
@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteModel toModel(ClienteDTO dto);
    ClienteDTO toDTO(ClienteModel model);
}
```

O MapStruct precisa estar no `annotationProcessorPaths` do `maven-compiler-plugin`
**depois** do Lombok, junto com `lombok-mapstruct-binding`.

---

## 7. Persistência e migrations

- O schema é controlado **apenas pelo Flyway**. O Hibernate não cria nem altera tabelas
  (`spring.jpa.hibernate.ddl-auto=validate`).
- Uma migration nova por mudança: `V<n>__<descricao_em_snake_case>.sql`
  (ex.: `V2__create_tb_produto.sql`).
- **Nunca editar uma migration já aplicada.** Para corrigir, crie uma nova.
- Valores monetários: `NUMERIC(10,2)` no banco e `BigDecimal` no Java. Nunca `double`/`float`.
- Datas: `TIMESTAMP` no banco e `LocalDateTime` no Java.
- Métodos de service que escrevem no banco usam `@Transactional`.
  Leituras usam `@Transactional(readOnly = true)`.

---

## 8. Validação

- Validação de **formato** (obrigatório, tamanho, regex, CPF) fica no **DTO**, com Bean Validation.
- Validação de **regra de negócio** (CPF duplicado, estoque suficiente, limite de fiado) fica no **service**.
- Mensagens de validação em português.

---

## 9. Testes

- **Testes unitários nos services**, com JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`),
  mockando repositories e mappers.
- Toda regra de negócio nova precisa de teste cobrindo o caminho feliz e cada caso de erro.
- Local: `src/test/java/...` espelhando o pacote da classe testada (ex.: `service/ClienteServiceTest`).
- Nome dos métodos de teste: `deve<Resultado>Quando<Condicao>`
  (ex.: `deveLancarExcecaoQuandoCpfJaCadastrado`).

---

## 10. Commits

Seguir [Conventional Commits](https://www.conventionalcommits.org/), com descrição em português:

```
feat: cadastra produto
fix: corrige validação de CPF duplicado
refactor: migra ClienteMapper para MapStruct
docs: atualiza ARCHITECTURE.md
test: adiciona testes do ClienteService
chore: atualiza dependências
```

---

## 11. Situação atual vs. padrão definido

O que o código já existente ainda precisa ajustar para seguir este documento:

- [ ] `ClienteController`: trocar `@RequestMapping("/cliente")` + `/cadastrar` por `POST /clientes`.
- [ ] `ClienteMapper`: migrar do mapper manual para MapStruct (e adicionar a dependência no `pom.xml`).
- [ ] Criar `exception/` com `GlobalExceptionHandler`, `RecursoNaoEncontradoException` e `RegraNegocioException`.
- [ ] `ClienteService`: verificar CPF duplicado antes de salvar (hoje estoura erro de constraint do banco → 500) e adicionar `@Transactional`.
- [ ] `application.yaml`: adicionar `spring.jpa.hibernate.ddl-auto: validate`.
- [ ] Criar `ClienteServiceTest`.
