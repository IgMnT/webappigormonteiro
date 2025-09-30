
# Plataforma de Desafios de Programação

Aplicação web e API REST construídas com Spring Boot 3, Java 17 e Thymeleaf para gerenciar desafios de programação e usuários com diferentes níveis de acesso.

## Sumário

- [Visão geral](#visão-geral)
- [Principais recursos](#principais-recursos)
- [Stack](#stack)
- [Preparando o ambiente](#preparando-o-ambiente)
- [Executando a aplicação](#executando-a-aplicação)
- [Autenticação e perfis](#autenticação-e-perfis)
- [Dados iniciais](#dados-iniciais)
- [Interface web](#interface-web)
- [API REST](#api-rest)
- [Documentação interativa](#documentação-interativa)
- [Tratamento de erros](#tratamento-de-erros)
- [Arquitetura e pacotes](#arquitetura-e-pacotes)
- [Validação e regras de negócio](#validação-e-regras-de-negócio)
- [Testes](#testes)
- [Entrega da avaliação](#entrega-da-avaliação)
- [Contribuição (commits semânticos)](#contribuição-commits-semânticos)
- [Referências adicionais](#referências-adicionais)

## Visão geral

O projeto fornece uma plataforma completa para cadastro e gerenciamento de desafios de programação. A aplicação combina páginas web (Thymeleaf) e uma API REST autenticada, com camadas bem definidas para serviços, repositórios, validações e tratamento centralizado de erros.

## Principais recursos

- Autenticação com Spring Security 6 (`SecurityFilterChain` customizada) e perfis `ADMIN`, `AUTHOR` e `VISITOR`.
- Hash de senha com `BCryptPasswordEncoder` e enforcement de senha mínima de 8 caracteres nas atualizações.
- Regra de negócio que impede a remoção ou inativação do único administrador ativo.
- Gestão de usuários, categorias e desafios com validações de unicidade (e-mail, título de desafio, nome de categoria) e regras específicas por perfil.
- Suporte a criação, atualização completa (`PUT`) e parcial (`PATCH`) em todos os recursos expostos pela API.
- Pesquisa de desafios com filtros combinados (dificuldade, autor e categoria) via consulta JPQL customizada.
- Documentação OpenAPI/Swagger totalmente anotada, gerada automaticamente em `/swagger-ui/index.html`.
- Tratamento global de exceções com respostas ProblemDetails e páginas HTML personalizadas para 403/404.

## Stack

- Java 17
- Spring Boot 3.x (Web, Thymeleaf, Data JPA, Validation, Springdoc OpenAPI)
- Spring Security 6
- Banco de dados H2 em arquivo (`./data/exemplo.mv.db`)
- Maven (ou wrapper `mvnw`)

## Preparando o ambiente

1. Instale o Java 17 e garanta que `java -version` aponta para a versão correta.
2. Opcionalmente instale o Maven; o wrapper `./mvnw` já está configurado.
3. Clone o repositório e navegue até a pasta `webappigormonteiro`.
4. Verifique se a porta `8080` está disponível.

> A base H2 persiste em disco (`./data/exemplo`), permitindo reinicializações com dados mantidos. Em ambiente de desenvolvimento utilizamos `spring.jpa.hibernate.ddl-auto=create`; ajuste para `update` ou para outro banco em produção.

## Executando a aplicação

```powershell
./mvnw spring-boot:run
```

Após o start:

- Home/ dashboard: <http://localhost:8080/principal>
- Login: <http://localhost:8080/login>
- Gestão de usuários: <http://localhost:8080/users>
- Catálogo de desafios: <http://localhost:8080/desafios>
- Categorias de desafios: <http://localhost:8080/categorias>
- Console H2: <http://localhost:8080/h2-console> (JDBC URL `jdbc:h2:file:./data/exemplo`, usuário `sa`, senha `password`)

Para encerrar, finalize o processo no terminal (`Ctrl+C`).

## Autenticação e perfis

- Login via formulário customizado (`/login`) com redirecionamento para `/principal` após sucesso.
- Logout disponível em `/logout`, limpando sessão e credenciais, com redirecionamento para `/login?logout`.
- Regras de autorização (resumo):
  - Recursos públicos: `/`, `/principal`, `/home`, páginas de erro e assets estáticos.
  - `/desafios/**` `GET`: acessível a `ADMIN`, `AUTHOR`, `VISITOR`.
  - `/desafios/**`, `/categorias/**`: somente `ADMIN` e `AUTHOR` para ações de escrita.
  - `/users/**`: exclusivo de `ADMIN`.
  - API REST (`/api/v1/**`): exige autenticação (mesma sessão gerada pelo login).

## Dados iniciais

O seed automático cria contas e categorias, permitindo testar rapidamente o fluxo completo:

| Perfil  | Email               | Senha         | Observações |
|---------|---------------------|---------------|-------------|
| ADMIN   | `igor@example.com`  | `admin123`    | Pode gerenciar todos os recursos e é o usuário protegido pela regra do “único admin”. |
| AUTHOR  | `bruno@example.com` | `autor123`    | Pode cadastrar desafios para si, listar categorias e desafios. |
| VISITOR | `carla@example.com` | `visitante123`| Acesso somente leitura aos desafios. |

Categorias iniciais: **Algoritmos**, **Desenvolvimento Web** e **Banco de Dados**.

## Interface web

- Views construídas com Thymeleaf e fragmentos em `templates/fragments/layout.html`.
- Listagens utilizam diretivas `th:each` com feedback de validação direto nos formulários (`th:errors`).
- Regras de segurança no controller evitam que autores excluam ou criem desafios para outros usuários.
- Páginas de erro personalizadas em `templates/error/` (403, 404 e fallback geral).

## API REST

- **Base URL**: `http://localhost:8080/api/v1`
- **Autenticação**: utilize as credenciais acima para autenticar via formulário e reaproveite o cookie de sessão. Em integrações automatizadas, configure um client que suporte login programático (form ou session).

| Método | Rota                     | Descrição                                               | Perfis |
|--------|--------------------------|---------------------------------------------------------|--------|
| GET    | `/users`                 | Lista usuários com filtros `status` e `role`.           | ADMIN  |
| POST   | `/users`                 | Cria usuário.                                           | ADMIN  |
| PUT    | `/users/{id}`            | Atualiza totalmente um usuário.                         | ADMIN  |
| PATCH  | `/users/{id}`            | Atualização parcial; aceita apenas campos enviados.     | ADMIN  |
| DELETE | `/users/{id}`            | Remove usuário (proteção para o último admin).          | ADMIN  |
| GET    | `/categorias`            | Lista categorias com filtro `nome`.                     | ADMIN, AUTHOR |
| POST   | `/categorias`            | Cria categoria garantindo unicidade.                    | ADMIN, AUTHOR |
| GET    | `/categorias/{id}`       | Detalha categoria.                                      | ADMIN, AUTHOR |
| PUT    | `/categorias/{id}`       | Atualiza totalmente.                                    | ADMIN, AUTHOR |
| PATCH  | `/categorias/{id}`       | Atualização parcial.                                    | ADMIN, AUTHOR |
| DELETE | `/categorias/{id}`       | Remove categoria (bloqueia se houver desafios ligados). | ADMIN, AUTHOR |
| GET    | `/desafios`              | Lista desafios com filtros `dificuldade`, `autorId`, `categoriaId`. | ADMIN, AUTHOR, VISITOR |
| POST   | `/desafios`              | Cria desafio validando título único e relacionamentos.  | ADMIN, AUTHOR |
| GET    | `/desafios/{id}`         | Detalha desafio.                                        | ADMIN, AUTHOR, VISITOR |
| PUT    | `/desafios/{id}`         | Atualiza desafio (autor e categoria devem existir).     | ADMIN, AUTHOR |
| PATCH  | `/desafios/{id}`         | Atualiza parcialmente (mantém integridade referencial). | ADMIN, AUTHOR |
| DELETE | `/desafios/{id}`         | Remove desafio.                                         | ADMIN, AUTHOR |

### Exemplos de uso

Criar uma categoria (após autenticação):

```http
POST /api/v1/categorias HTTP/1.1
Content-Type: application/json

{
  "nome": "Machine Learning",
  "descricao": "Modelagem e algoritmos supervisionados"
}
```

Resposta (201 Created) com cabeçalho `Location` apontando para o recurso recém-criado.

Atualizar parcialmente um desafio:

```http
PATCH /api/v1/desafios/10 HTTP/1.1
Content-Type: application/json

{
  "dificuldade": "Avançado",
  "categoriaId": 2
}
```

Em caso de violação de regra de negócio ou validação, a API retorna **ProblemDetails** com `title`, `detail`, `type`, `instance` e, quando aplicável, um array `errors` detalhando os campos.

## Documentação interativa

- UI Swagger: <http://localhost:8080/swagger-ui/index.html>
- Documento OpenAPI (JSON): <http://localhost:8080/v3/api-docs>
- As controllers utilizam anotações `@Operation`, `@ApiResponse`, `@Schema` e `@ArraySchema`, mantendo o contrato sincronizado com os DTOs e entidades expostas.

## Tratamento de erros

- `GlobalExceptionHandler` identifica se a requisição é API (via header `Accept`) e devolve a resposta adequada (HTML ou JSON).
- Exceções específicas:
  - `ResourceNotFoundException` → HTTP 404, view `error/404` ou ProblemDetail.
  - `BusinessException` → HTTP 400 com mensagem clara para regras de negócio.
  - `AccessDeniedException` → HTTP 403 com redirecionamento para `error/403` na interface web.
  - `MethodArgumentNotValidException` → HTTP 400 agregando todas as mensagens de validação.
  - Fallback genérico loga o stack trace e retorna HTTP 500.

## Arquitetura e pacotes

```
br.edu.iff.ccc.webappigormonteiro
├─ config              # Segurança e OpenAPI
├─ controller.api      # REST controllers (/api/v1)
├─ controller.view     # Controllers MVC (Thymeleaf)
├─ dto                 # DTOs e records de request/response
├─ entity              # Entidades JPA com validações
├─ exception           # Exceções de domínio + handler global
├─ repository          # Spring Data JPA repositories
└─ service             # Regras de negócio transacionais
```

Cada camada evita acoplamento indevido: controllers chamam serviços, serviços interagem com repositórios e tratam regras; entidades permanecem focadas em persistência.

## Validação e regras de negócio

- Validações via Jakarta Validation em entidades e DTOs (`@NotBlank`, `@Email`, `@Size`, etc.).
- Regras adicionais implementadas em serviços levantam `BusinessException` (ex.: proteção do último admin, unicidade de título de desafio, bloqueio de exclusão de categoria com desafios vinculados).
- Serviços utilizam transações (`@Transactional`) e aparecem prontos para extensão.

## Testes

```powershell
./mvnw clean test
```

O teste de contexto (`WebappigormonteiroApplicationTests`) garante que a aplicação sobe com todas as configurações. Amplie a suíte adicionando testes unitários para as regras de serviço conforme necessário.

## Entrega da avaliação

- Abra um Pull Request da branch `develop` para `main` com o título **Entrega Final P2**.
- Marque o commit final desta entrega com a tag Git `v2.0-FINAL`.
- Execute `./mvnw clean test` antes de enviar para assegurar a integridade da build.

## Contribuição (commits semânticos)

Adote a convenção:

- `feat`: nova funcionalidade
- `fix`: correção de bug
- `docs`: documentação
- `refactor`: refatoração sem mudança funcional
- `style`, `test`, `chore`, `ci`: conforme necessário

## Referências adicionais

- Histórias de usuário e tarefas: `docs/USER_STORIES.md`, `docs/TASKS.md`
- Diagramas e apresentações: pasta `docs/`
- Dados persistidos localmente: `data/exemplo.mv.db`

