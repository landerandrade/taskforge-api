# TaskForge API

API REST de gestão de tarefas — projeto construído ao vivo no curso **"Do Código ao Contrato"** da Forgile.

## Sobre o projeto

TaskForge é o backend de um sistema de gestão de tarefas. Ao longo de 9 semanas, os alunos constroem essa API do zero — do setup até o deploy — simulando o fluxo real de um time de
desenvolvimento: branches, Pull Requests, code review e Sprint Ágil.

## Stack

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA + Hibernate
- Spring Security + JWT
- PostgreSQL 16
- Docker

## Como rodar localmente

Pré-requisitos: Java 21 e Docker instalados.

Passo 1 — sobe o banco:
  ```
  docker compose up -d
  ```

Passo 2 — roda a aplicação:
  ```
  ./mvnw spring-boot:run
  ```

A API sobe em http://localhost:8080

## Estrutura do projeto

  ```
  src/main/java/com/forgile/taskforge/
  ├── controller/    → endpoints REST
  ├── service/       → regras de negócio
  ├── repository/    → acesso ao banco
  ├── model/         → entidades JPA
  ├── dto/           → objetos de entrada e saída
  └── config/        → configurações de segurança
  ```

## Curso

"Do Código ao Contrato" — Java + Spring Boot, 9 semanas, ao vivo.
Turma Fundadora abrindo em breve → forgile.com
