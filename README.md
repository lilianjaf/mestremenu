# Mestre Menu

Sistema de gerenciamento para restaurantes.

## Funcionalidades

- Cadastro de Usuários (Clientes e Donos de Restaurante)
- Autenticação via JWT
- Gerenciamento de Perfil (Atualização de dados e Troca de Senha)
- Busca de Usuários por Nome
- Exclusão de Conta
- Auditoria de Alterações

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.1
- Spring Security & JWT
- Spring Data JPA
- PostgreSQL
- Flyway (Migração de Banco de Dados)
- SpringDoc OpenAPI (Swagger)
- Docker & Docker Compose

## Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados.

### Passos
1. Clone o repositório.
2. Na raiz do projeto, execute:
   ```bash
   docker-compose -f compose/docker-compose.yml up -d
   ```
3. A aplicação estará disponível em `http://localhost:8080`.

## Documentação da API

Após iniciar a aplicação, acesse:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/v3/api-docs`

## Testes

Para executar os testes unitários e de integração:
```bash
./gradlew test
```
