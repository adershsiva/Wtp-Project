# Poll Management Application (WTP)

A modern, full-stack web application designed for creating, administering, and analyzing interactive polls. Built with **Spring Boot 3 (Java 21)** and **React 19 (TypeScript + Vite)**.

---

## 🌟 Key Features

* **🔐 Authentication & Security**: Complete user registration and login workflows secured with **Spring Security** and stateless **JWT (JSON Web Tokens)**.
* **📝 Dynamic Poll Creation**:
  * Customizable question titles, descriptions, and expiration dates.
  * Support for multiple question formats:
    * **Rating / Scale** (e.g. 1 to 5)
    * **Choice / Option-based**
    * **Open Text Responses**
* **👥 Targeted Invitations**: Restrict poll participation to selected users and invitees.
* **⏸️ Poll Lifecycle Management**: Poll creators can pause, resume, finish (close), or delete polls with automatic deadline expiration checks.
* **📊 Comprehensive Results & Analytics**:
  * Real-time calculation of average ratings and percentage distributions.
  * Individual participant submission inspection for poll owners.
* **📥 CSV Export**: Export detailed participant responses and statistical summaries to CSV for spreadsheet analysis.
* **📖 Interactive API Documentation**: Built-in Swagger UI / OpenAPI documentation.

---

## 🛠️ Technology Stack

### Backend
* **Language & Runtime:** Java 21 LTS
* **Framework:** Spring Boot 3.4.3
* **Security:** Spring Security with JWT (`jjwt 0.12.6`)
* **Persistence:** Spring Data JPA, Hibernate ORM
* **Databases:** MariaDB (Production/Docker) & H2 (In-memory testing)
* **API Documentation:** SpringDoc OpenAPI 2.8.5 (Swagger UI)
* **Build Tool:** Maven Wrapper (`mvnw`)
* **Testing:** JUnit 5, Mockito, AssertJ

### Frontend
* **Framework:** React 19 (TypeScript)
* **Build Tool:** Vite 8
* **Routing:** React Router v7
* **HTTP Client:** Axios
* **UI & Styling:** Bootstrap 5 with responsive layout

---

## 📁 Repository Structure

```text
.
├── myproject-backend/         # Spring Boot backend application
│   ├── compose.yaml          # Docker Compose configuration for MariaDB
│   ├── src/main/java/        # Controllers, Services, Entities, DTOs, Security
│   ├── src/main/resources/   # Application properties and database configs
│   └── src/test/             # Unit and integration test suites
│
├── myproject-frontend/        # React + TypeScript frontend application
│   ├── src/pages/            # Home, Login, Register, CreatePoll, PollDetail
│   ├── src/services/         # Axios API service integrations
│   ├── src/types/            # TypeScript interfaces & DTO definitions
│   └── vite.config.ts        # Vite build & development server config
│
└── README.md                 # Project documentation
```

---

## 🚀 Getting Started

### Prerequisites
* **Java 21 LTS**
* **Node.js** (v18+ or v20+) and **npm**
* **Docker & Docker Compose** (for running MariaDB)

---

### 1. Database Setup

Navigate to the backend directory and start the MariaDB container:
```bash
cd myproject-backend
docker compose up -d
```
The database will be available on `localhost:3306` (or port mapped according to `application.properties`).

---

### 2. Backend Setup

From the `myproject-backend` directory, build and run the Spring Boot service:

```bash
# Windows (PowerShell)
./mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

* **Backend Base URL:** `http://localhost:8080`
* **Swagger UI Documentation:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI Specification:** `http://localhost:8080/api-docs`

---

### 3. Frontend Setup

From the `myproject-frontend` directory, install dependencies and start the Vite dev server:

```bash
cd myproject-frontend
npm install
npm run dev
```

* **Frontend Application:** `http://localhost:5173`

---

## 🤖 AI Usage Declaration

In accordance with academic standards and transparency guidelines:

* **Scope of AI Assistance:** Generative AI tools were used **minimally** and strictly for auxiliary tasks such as syntax clarification, reference documentation lookups, and troubleshooting build configuration errors.
* **Core Development:** All system requirements, database schema modeling, architectural structure, core business logic, validation rules, security configuration, and user flows were conceived, authored, and implemented independently by the project author.

---

## 👤 Author

* **Author:** Adersh Sivanandhan
* **Project:** Web-Technologie-Projekt (WTP)
