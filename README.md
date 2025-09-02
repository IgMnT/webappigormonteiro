
# Plataforma de Desafios de Programação

Aplicação web para gerenciar usuários e desafios de programação, construída com Spring Boot 3, Java 17 e Thymeleaf.

## Objetivo

Permitir o cadastro de usuários e a criação/listagem de desafios de programação (com dificuldade e linguagem), mantendo arquitetura em camadas e boas práticas de validação e layout.

## Tecnologias

- Java 17
- Spring Boot 3.x (Web, Thymeleaf, Data JPA, Validation)
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
	 - Usuários: http://localhost:8080/users
	 - Desafios: http://localhost:8080/desafios
	 - Console H2: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:file:./data/exemplo`, usuário `sa`, senha `password`)

Notas:
- A base H2 é recriada a cada start (spring.jpa.hibernate.ddl-auto=create). Ajuste para `update` em produção.

## Arquitetura e Pacotes

```
br.edu.iff.ccc.webappigormonteiro
├─ controller.view        # Controllers MVC (magros)
├─ dto                    # DTOs para entrada de dados
├─ entity                 # Entidades JPA
├─ repository             # Spring Data JPA repositories
└─ service                # Regras de negócio
```

## Layouts e Views

- Fragmentos Thymeleaf em `templates/fragments/layout.html` (head, header, footer)
- Páginas usando `th:replace` para DRY

## Validação

- Validações com Jakarta Validation em DTOs e Entidades (`@NotBlank`, `@Email`, etc.)

## Diagramas e Wireframes

- Coloque seus diagramas em `docs/` (ex.: `docs/diagramas/arquitetura.drawio`, `docs/wireframes/`)

## Histórias de Usuário (exemplos)

- Como administrador, quero cadastrar usuários com nome, email, status e perfil para gerenciar o acesso.
- Como autor, quero criar desafios com dificuldade e linguagem para publicar exercícios.
- Como visitante, quero listar desafios para praticar programação.

## Contribuição (Commits Semânticos)

Use commits semânticos:
- feat: nova funcionalidade
- fix: correção de bug
- docs: documentação
- refactor: refatoração sem mudança de comportamento
- style/test/chore/ci: conforme necessário

