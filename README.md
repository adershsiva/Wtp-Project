# Poll Management Application

Web-Technologie-Projekt (WTP)

## Description
A web application for creating, managing, and answering polls. Users can register, log in, create polls with questions, invite other users, submit answers, view aggregated results, and download responses as CSV.

## Tech Stack
- **Backend:** Java 21, Spring Boot, Spring Security, JWT, MariaDB, Maven
- **Frontend:** React, TypeScript, Vite, Bootstrap
- **Database:** MariaDB (Docker)

## How to Run

### 1. Database
```bash
cd myproject-backend
docker compose up -d
```

### 2. Backend
```bash
cd myproject-backend
./mvnw.cmd spring-boot:run
```
Runs at `http://localhost:8080`.
Swagger UI available at: `http://localhost:8080/swagger-ui.html`

### 3. Frontend
```bash
cd myproject-frontend
npm install
npm run dev
```
Runs at `http://localhost:5173`.

## AI Usage Declaration
AI was used minimally during the project strictly for syntax assistance, looking up documentation, and troubleshooting build/error messages. The project architecture, data models, business logic, endpoints, and user interface were implemented by the author.

## Author
Adersh Sivanandhan
