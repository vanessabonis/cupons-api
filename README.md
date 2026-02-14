# API de Cupons

API REST para gerenciar cupons de desconto.

## Stack

- Java 21
- Maven 3.8+
- Spring Boot 3.3+
- H2 (testes)
- JUnit 5 + Mockito + AssertJ
- Swagger/OpenAPI 3.0
- Docker

## Setup

```bash
mvn clean install
mvn spring-boot:run
```

> **Nota:** O projeto usa repositório Maven padrão. O arquivo `settings-custom.xml` é usado apenas no CI.

## Docker

### Build da imagem

```bash
sudo docker build -t cupons-api:local .
```

### Subir com Docker Compose

```bash
sudo docker compose up --build
```

### (Opcional) Subir em background

```bash
sudo docker compose up --build -d
```

### Logs

```bash
sudo docker compose logs -f
```

### Stop

```bash
sudo docker compose down
```

### Smoke test (minimo)

```bash
sudo curl -i -X POST http://localhost:8080/coupon \
  -H "Content-Type: application/json" \
  -d '{
    "code": "ABC-123",
    "description": "Cupom de teste",
    "discountValue": 10.00,
    "expirationDate": "2026-12-31T23:59:59",
    "published": false
  }'
```

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Logs (JSON)

- Logs no formato JSON via Logback + SLF4J
- Saida padrao no console

## Actuator (metrics/health)

- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`

## Endpoints

- POST /coupon - Criar cupom
- GET /coupon/{id} - Buscar cupom
- DELETE /coupon/{id} - Deletar cupom (soft delete)

## Use Cases (intencao unica)

Seguindo a dica do desafio, os casos de uso foram separados por intencao:

- `CreateCupomUseCase`
- `GetCupomByIdUseCase`
- `DeleteCupomUseCase`

Isso evita um "Service" generico com muitos metodos e deixa cada classe
responsavel por uma unica acao do usuario.

## Estrutura

```
src/main/java/com/desafio/cupom/
├── domain/          # Entities, Value Objects, Services
├── application/     # Use Cases, DTOs, Mappers
├── infrastructure/  # JPA, Config
└── api/             # Controllers, Exception Handlers
```
