# Order Management API

A RESTful order management service built with **Java 21** and **Spring Boot 3**. It supports creating, retrieving, updating, and deleting orders, with request validation, consistent error responses, and unit tests.

## Tech Stack
- Java 21, Spring Boot 3 (Web, Data JPA, Validation, Actuator)
- H2 in-memory database
- JUnit 5, Mockito, MockMvc, AssertJ
- Maven, GitHub Actions CI

## Features
- CRUD endpoints for orders, with total price calculated server-side
- Order status lifecycle: `CREATED → PAID → SHIPPED → DELIVERED / CANCELLED`
- Request validation with field-level error messages
- Standardized error responses using RFC 7807 `ProblemDetail`
- Layered architecture: controller → service → repository, with DTOs separating the API from the database entity
- Health check via Spring Boot Actuator

## Running Locally
Requires Java 21.

```bash
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`.

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create an order (returns 201 + Location header) |
| GET | `/api/orders` | List all orders |
| GET | `/api/orders/{id}` | Get an order by ID (404 if missing) |
| PATCH | `/api/orders/{id}/status` | Update order status |
| DELETE | `/api/orders/{id}` | Delete an order (204) |
| GET | `/actuator/health` | Health check |

### Example
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Asha","productName":"Laptop","quantity":2,"unitPrice":899.99}'
```

Validation error response:
```json
{
  "status": 400,
  "title": "Bad Request",
  "detail": "Validation failed",
  "errors": { "customerName": "must not be blank", "quantity": "must be greater than or equal to 1" }
}
```

## Running Tests
```bash
./mvnw test
```

## Roadmap
- PostgreSQL with Docker Compose
- Pagination and filtering by status
- OpenAPI/Swagger documentation