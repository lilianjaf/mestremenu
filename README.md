# Mestre Menu - Ecossistema de Gestão Compartilhada

O ecossistema de gestão compartilhada para o grupo **Mestre Menu** visa centralizar as operações em uma plataforma única, permitindo que os restaurantes reduzam custos e priorizem a excelência gastronômica através de uma infraestrutura escalável e moderna utilizando Docker e PostgreSQL.

## Descrição

O Mestre Menu é uma API RESTful sob o ecossistema Spring, fundamentada nos princípios SOLID e em uma implementação de Programação Orientada a Objetos para garantir uma arquitetura extensível, segura e de fácil manutenção.

## Tecnologias Utilizadas

- **Java 21**: Linguagem principal para aproveitamento dos recursos mais recentes.
- **Spring Boot 3.4.1**: Framework base para a construção da API.
- **Spring Security & JWT (JSON Web Token)**: Implementação de segurança e autenticação stateless.
- **Spring Data JPA**: Abstração de persistência de dados.
- **Hibernate**: Implementação do JPA e gerenciamento de ORM.
- **Flyway**: Gerenciamento de migrações e versão do banco de dados.
- **PostgreSQL**: Banco de dados relacional robusto.
- **Docker & Docker Compose**: Containerização da aplicação e infraestrutura.
- **SpringDoc OpenAPI (Swagger)**: Documentação automatizada dos endpoints.
- **Gradle**: Ferramenta de automação de build.

## Arquitetura do Sistema

A aplicação segue uma arquitetura em camadas bem definida para garantir a separação de responsabilidades:

- **Controller**: Camada de entrada que gerencia as requisições HTTP e expõe os recursos via REST.
- **Service**: Camada de lógica de negócio, onde as regras do sistema são aplicadas.
- **Repository**: Camada de acesso a dados utilizando Spring Data JPA para comunicação com o PostgreSQL.
- **DTO (Data Transfer Objects)**: Utilizados para a entrada e saída de dados, desacoplando o modelo de domínio da API externa.
- **Mapper**: Responsável pela conversão entre entidades de domínio e DTOs.

## Instruções de Execução via Docker

A infraestrutura completa pode ser levantada utilizando o [docker-compose.yml](compose/docker-compose.yml), garantindo um ambiente isolado e consistente.

### Pré-requisitos
- Docker instalado.
- Docker Compose instalado.

### Passo a Passo

1.  **Clonagem do Repositório**:
    ```bash
    git clone https://github.com/lilianjaf/mestremenu.git
    cd mestremenu
    ```

2.  **Configuração de Variáveis de Ambiente**:
    O arquivo `compose/docker-compose.yml` utiliza variáveis de ambiente. Você deve definir os valores para o banco de dados. Para uma execução rápida com valores padrão:
    ```bash
    export POSTGRES_DB=mestremenu
    export POSTGRES_USER=mestremenu
    export POSTGRES_PASSWORD=mestremenu
    ```

3.  **Execução do Docker Compose**:
    Na raiz do projeto, execute o comando para subir o banco de dados e a aplicação:
    ```bash
    docker-compose -f compose/docker-compose.yml up -d
    ```

4.  **Verificação**:
    A aplicação estará disponível em `http://localhost:8080`. O banco de dados PostgreSQL estará na porta `5432`.

## Documentação da API

### Swagger UI
A documentação interativa da API (OpenAPI 3) baseada nas interfaces OpenAI pode ser acessada em:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Definições JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Postman
Uma coleção do Postman está inclusa no repositório para facilitar os testes:
- Arquivo: [Mestre Menu Testes.postman_collection.json](collections/Mestre%20Menu%20Testes.postman_collection.json)

## Funcionalidades Implementadas

O sistema atualmente conta com as seguintes capacidades:

- **Gestão de Usuários**:
    - Cadastro de novos usuários com validação de **e-mail único** e **login único**.
    - Busca de usuários por **nome** (suporta busca parcial e ignora case).
    - Atualização de dados cadastrais e endereço.
    - Exclusão de usuários.
- **Segurança**:
    - Autenticação de usuários via login e senha retornando **Token JWT**.
    - Proteção de endpoints baseada em autenticação stateless.
    - **Troca de senha** em endpoint separado ([UsuarioSenhaController](src/main/java/com/github/lilianjaf/mestremenu/api/v1/controller/UsuarioSenhaController.java)) para maior segurança.
- **Auditoria**:
    - Rastreabilidade de alterações com registro automático da **data de alteração** (`data_ultima_alteracao`) em cada modificação do usuário.
- **Resiliência e Padronização**:
    - Tratamento de exceções global com retornos padronizados (**RFC 7807 - Problem Details**).
    - Migrações automatizadas de banco de dados via Flyway.

## Testes

Para executar a suíte de testes (unitários e de integração):
```bash
./gradlew test
```
