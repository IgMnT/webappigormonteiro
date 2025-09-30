
# Plataforma de Desafios de Programação

Aplicação web para gerenciar usuários e desafios de programação, construída com Spring Boot 3, Java 17 e Thymeleaf.

## Objetivo

Permitir o cadastro de usuários e a criação/listagem de desafios de programação (com dificuldade e linguagem), mantendo arquitetura em camadas e boas práticas de validação e layout.

## Funcionalidades principais

- Autenticação com Spring Security e perfis `ADMIN`, `AUTHOR` e `VISITOR`.
- Gestão de usuários com validações e hash de senha (BCrypt).
- Catálogo de desafios com título, descrição, dificuldade, categoria e linguagem.
- Cadastro e listagem de categorias de desafios.
- Controle de acesso por perfil:
	- **ADMIN**: gerencia usuários, categorias e desafios.
	- **AUTHOR**: pode criar/remover os próprios desafios e consultar categorias.
	- **VISITOR**: acesso somente leitura aos desafios.
- Tratamento centralizado de erros (páginas personalizadas 403/404, respostas JSON padronizadas).

## Tecnologias

- Java 17
- Spring Boot 3.x (Web, Thymeleaf, Data JPA, Validation)
- Spring Security 6
- H2 Database (arquivo)
- Maven

## Como executar

Pré-requisitos:
- Java 17 instalado e no PATH
- Maven (ou usar o wrapper `mvnw`)

Passos:
1. Clone o repositório
2. Na raiz do projeto, execute:
	 - Windows PowerShell:
		 - `./mvnw spring-boot:run`
	 - Ou usando Maven instalado:
		 - `mvn spring-boot:run`
3. Acesse a aplicação:
	 - Home: http://localhost:8080/principal
	 - Login: http://localhost:8080/login
	 - Usuários: http://localhost:8080/users
	 - Desafios: http://localhost:8080/desafios
	 - Categorias: http://localhost:8080/categorias
	 - Console H2: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:file:./data/exemplo`, usuário `sa`, senha `password`)

Notas:
- A base H2 é recriada a cada start (spring.jpa.hibernate.ddl-auto=create). Ajuste para `update` em produção.

### API REST

- Base URL: `http://localhost:8080/api/v1`
- Recursos expostos:
	- `GET /users` lista usuários com filtros opcionais `status` e `role`.
	- `POST /users`, `GET /users/{id}`, `PUT /users/{id}`, `PATCH /users/{id}`, `DELETE /users/{id}`.
	- `GET /categorias` com filtro `nome`, além de `POST`, `GET /{id}`, `PUT`, `PATCH`, `DELETE`.
	- `GET /desafios` com filtros `dificuldade`, `autorId`, `categoriaId`, além de `POST`, `GET /{id}`, `PUT`, `PATCH`, `DELETE`.
- As respostas seguem os códigos HTTP semânticos (201 Created, 200 OK, 204 No Content e Problem Details para erros).
- Requests `PATCH` aceitam somente os campos a serem alterados.

### Documentação interativa (Swagger)

- A documentação está disponível em: `http://localhost:8080/swagger-ui/index.html`
- O arquivo OpenAPI é servido em `http://localhost:8080/v3/api-docs`
- Cada endpoint está anotado com exemplos de respostas e códigos de status.

### Usuários de exemplo

| Perfil  | Email               | Senha         | Permissões principais |
|---------|---------------------|---------------|-----------------------|
| ADMIN   | `igor@example.com`  | `admin123`    | Gerencia tudo         |
| AUTHOR  | `bruno@example.com` | `autor123`    | Cria seus desafios    |
| VISITOR | `carla@example.com` | `visitante123`| Consulta desafios     |

> Os usuários `AUTHOR` só podem criar/remover desafios atribuídos a si mesmos. Visitantes possuem apenas leitura.

## Arquitetura e Pacotes

```
br.edu.iff.ccc.webappigormonteiro
├─ controller.view        # Controllers MVC (magros)
├─ dto                    # DTOs para entrada de dados
├─ entity                 # Entidades JPA
├─ repository             # Spring Data JPA repositories
└─ service                # Regras de negócio
```

### Tratamento de erros

- `@ControllerAdvice` global (`GlobalExceptionHandler`) centraliza respostas HTML e JSON.
- Páginas personalizadas para erros 403, 404 e falhas genéricas (`templates/error/`).
- Mensagens de validação exibidas diretamente nas telas com suporte a Bean Validation.

## Layouts e Views

- Fragmentos Thymeleaf em `templates/fragments/layout.html` (head, header, footer)
- Páginas usando `th:replace` para DRY
- Views específicas para login (`templates/auth/login.html`), desafios, categorias e usuários.

## Validação

- Validações com Jakarta Validation em DTOs e Entidades (`@NotBlank`, `@Email`, etc.)
- Regras de negócio lançam `BusinessException` com feedback amigável nas telas.

## Diagramas e Wireframes

- Coloque seus diagramas em `docs/` (ex.: `docs/diagramas/arquitetura.drawio`, `docs/wireframes/`)

## Histórias de Usuário (exemplos)

- Como administrador, quero cadastrar usuários com nome, email, status e perfil para gerenciar o acesso.
- Como autor, quero criar desafios com dificuldade e linguagem para publicar exercícios.
- Como visitante, quero listar desafios para praticar programação.

## Testes

Execute a suíte de testes (unitários e de integração) com:

```powershell
./mvnw clean test
```

Os testes cobrem autenticação de usuários e regras de negócio de categorias.

## Entrega da Avaliação

- Abra um Pull Request da branch `develop` para `main` com o título **Entrega Final P2**.
- Marque o commit final desta entrega com a tag Git `v2.0-FINAL`.
- Garanta que o pipeline de testes (`./mvnw clean test`) esteja passando antes de enviar a entrega.

## Contribuição (Commits Semânticos)

Use commits semânticos:
- feat: nova funcionalidade
- fix: correção de bug
- docs: documentação
- refactor: refatoração sem mudança de comportamento
- style/test/chore/ci: conforme necessário

