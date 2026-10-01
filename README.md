# Mercadinho Astro

API REST para gestão de um mercadinho: clientes, produtos e estoque, vendas (caixa),
fiado e usuários.

> Arquitetura, convenções e padrões do código: veja **[ARCHITECTURE.md](ARCHITECTURE.md)**.

## Stack

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation)
- PostgreSQL + Flyway
- MapStruct + Lombok
- JUnit 5 + Mockito

## Pré-requisitos

- JDK 17
- PostgreSQL rodando em `localhost:5432`

## Como rodar

1. Crie o banco:

   ```sql
   CREATE DATABASE mercadinhoastro;
   ```

2. Confira as credenciais em `src/main/resources/application.yaml`
   (padrão: usuário `postgres`, senha `1234`).

3. Suba a aplicação (o Flyway cria as tabelas automaticamente):

   ```bash
   ./mvnw spring-boot:run
   ```

   No Windows (PowerShell/cmd): `mvnw.cmd spring-boot:run`

A API sobe em `http://localhost:8080`.

## Testes

```bash
./mvnw test
```

## Endpoints

| Método | URL         | Descrição          |
|--------|-------------|--------------------|
| `POST` | `/clientes` | Cadastra cliente   |

Exemplo:

```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "Maria da Silva",
    "apelido": "Dona Maria",
    "cpf": "52998224725",
    "endereco": "Rua das Flores, 10",
    "telefone": "11987654321"
  }'
```

## Roadmap

- [x] Cadastro de cliente
- [ ] Produtos e estoque
- [ ] Vendas / caixa (PDV)
- [ ] Fiado (conta do cliente)
- [ ] Usuários e login
