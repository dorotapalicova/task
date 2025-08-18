# User and Policies Manager Application

## Overview

This is a backend REST API for managing users and policies, built with Spring Boot, PostgreSQL, and Flyway for database
migrations. The API documentation is available via Swagger UI.

---

## 1. Setup Instructions (Before First Run)

### Prerequisites

- Docker & Docker Compose installed
- Java 21+ (if building locally)
- Maven (if building locally)

### Steps

Clone the repository:

```bash
git clone https://github.com/dorotapalicova/task.git
cd task
```

### Configure environment variables (optional)

The default DB config is in `docker-compose.yaml`. Before first run you can adjust:

```
POSTGRES_DB=usermanager
POSTGRES_USER=user
POSTGRES_PASSWORD=password
```

---

Flyway initial migration script is already included in `src/main/resources/db/migration`.  
It creates necessary tables: `users`, `user_organization_unit`, `user_policy`, `policies`, and `policy_conditions`.

---

## 2. Build and start containers

```bash
docker compose up --build
```

This will:

- Start a Postgres container (usermanager-db)
- Build and run the Spring Boot app container (usermanager-app)
- Run Flyway migrations on startup

After startup, the app will be available at:

```arduino
http://localhost:8080
```

The Spring Boot app connects to the Postgres DB inside Docker network.

### Swagger UI Link

(for the app to support Swagger OpenAPI is needed, that is the reason for including it even though it currently
introduces a vulnerability)

OpenAPI docs and UI are available at:

```bash
http://localhost:8080/swagger-ui/index.html
```

Use this to explore all REST API endpoints interactively.

## 3. Application Documentation

### Entities

#### User

- **name (ID):** String
- **firstName, lastName, emailAddress:** String
- **organizationUnit:** list of strings stored in `user_organization_unit` table
- **birthDate, registeredOn:** LocalDate
- **policy:** set of policy IDs linked via `user_policy` table

#### Policy

- **id (ID):** String
- **name:** String
- **conditions:** key-value map stored in `policy_conditions` table

#### Persistence

- Uses JPA/Hibernate for ORM
- Flyway manages DB migrations
- `validate` Hibernate setting ensures DB schema matches entities

## 4. Tutorial: Configuring Users, Policies & System Behavior

### Creating Policies

#### POST `/policies`

**Example JSON:**

```json
{
  "id": "policy1",
  "name": "Admin Access",
  "conditions": {
    "accessLevel": "admin",
    "region": "global"
  }
}
```

### Creating Users

#### POST `/users`

**Example JSON:**

```json
{
  "name": "jdoe",
  "firstName": "John",
  "lastName": "Doe",
  "emailAddress": "john.doe@example.com",
  "organizationUnit": [
    "sales",
    "marketing"
  ],
  "birthDate": "1990-01-01",
  "registeredOn": "2025-08-01",
  "policy": [
    "policy1"
  ]
}
```

## System Behavior

- Supported condition types are `youngerThan`, `emailDomainIs` and `isMemberOf`
  in case of creating or modifying policy with a condition type that is not available the app will return
  `400 Bad Request` with a valid message.
- In case of creating user or policy with name that is already in use the app will return `403 Forbidden` with massage
  `name is not available`.
- Users are linked to multiple policies via **policy** field.
- When user is modified the app automatically reevaluates all the policies for the user.
- When policy is modified or created the app automatically evaluates this policy for all users.
- Policies contain conditions that define user permissions or rules.
- On startup, **Flyway** runs migrations to keep DB schema consistent.
- **Hibernate** validates schema; mismatch causes startup failure.
- API supports standard **CRUD** for users and policies.