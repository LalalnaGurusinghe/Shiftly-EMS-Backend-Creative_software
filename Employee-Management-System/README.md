# Shiftly Employee Management System (EMS) Backend

A robust, production-ready Java Spring Boot backend for managing employees, teams, projects, timesheets, leaves, claims, referrals, and more. Built for real-world enterprise use with secure, role-based access and modular architecture.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Environment & Configuration](#environment--configuration)
- [Core Modules & Features](#core-modules--features)
- [Role-Based Access & Workflow](#role-based-access--workflow)
- [API Overview](#api-overview)
- [How to Run](#how-to-run)
- [Contributing](#contributing)

---

## Project Structure

```
Employee-Management-System/
├── pom.xml
├── mvnw / mvnw.cmd
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/EMS/Employee/Management/System/
│   │   │       ├── controller/      # REST API controllers
│   │   │       ├── service/         # Service interfaces & implementations
│   │   │       ├── repo/            # Spring Data JPA repositories
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── entity/          # JPA entities & enums
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── security/        # Security filters/config (JWT, etc.)
│   │   │       ├── JWT/             # JWT utility classes
│   │   │       └── EmployeeManagementSystemApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/...
└── target/
```

---

## Technology Stack

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA** (Hibernate)
- **Spring Security** (JWT-based)
- **MySQL** (default, configurable)
- **Lombok** (for boilerplate reduction)
- **Spring Mail** (for notifications)
- **OpenAPI/Swagger** (API docs)
- **Maven** (build tool)

---

## Environment & Configuration

Edit `src/main/resources/application.properties` for your environment:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/FinalV1?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
jwt.secret=YOUR_SECRET
jwt.expiration=3600000
spring.mail.host=smtp.gmail.com
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD
```

---

## Core Modules & Features

- **User & Authentication:** Secure JWT-based login, registration, and role management.
- **Employee Management:** CRUD for employees, skills, education, and reporting structure.
- **Teams & Departments:** Manage teams, assign employees, and map to departments.
- **Projects:** Assign projects to teams, manage project lifecycle.
- **Timesheets:** Employees submit timesheets; admins approve/reject.
- **Leave Management:** Employees apply for leave; admins approve/reject; file upload supported.
- **Claims:** Employees submit claims with file attachments; admins process.
- **Vacancies & Referrals:** Manage job vacancies and employee referrals with resume upload.
- **Events:** Company events with approval workflow.
- **Role-Based Access:** All endpoints protected by roles (USER, ADMIN, SUPER_ADMIN).

---

## Role-Based Access & Workflow

- **USER:**  
  - Can view/update own profile, submit timesheets, apply for leave, submit claims/referrals, view own team/projects/events.
- **ADMIN:**  
  - Full CRUD on employees, teams, departments, projects, claims, leaves, vacancies, events.
  - Approve/reject timesheets, leaves, claims, events.
- **SUPER_ADMIN:**  
  - All admin privileges plus user/role management.

**Workflow Example:**  
1. **Employee** registers, is assigned to a team/department.
2. **Employee** submits timesheet/leave/claim/referral.
3. **Admin** reviews and approves/rejects requests.
4. **All actions** are logged and mapped to the correct user/team/project.

---

## API Overview

Each module has a dedicated controller under `/api/v1/shiftly/ems/` (see `controller/` directory):

- `/employee` - Employee management
- `/teams`, `/team-members` - Team and membership management
- `/departments` - Department management
- `/projects` - Project management
- `/timesheets` - Timesheet submission/approval
- `/leaves` - Leave application/approval
- `/claims` - Claim submission/approval
- `/vacancies` - Job vacancies
- `/referrals` - Candidate referrals
- `/events` - Company events
- `/users` - User management

**API docs:**  
If enabled, Swagger/OpenAPI UI is available at `/swagger-ui.html`.

---

## How to Run

1. **Clone the repo**
2. **Configure your database and environment** in `application.properties`
3. **Build and run:**
   ```sh
   ./mvnw spring-boot:run
   ```
4. **Access the API** at `http://localhost:8080/api/v1/shiftly/ems/`

---

## Contributing

- Fork the repo and create a feature branch.
- Follow code style and best practices.
- Submit a pull request with a clear description.

---

**For any questions or issues, please open an issue in this repository.** 

---

## **What This Means**

- The Gemini API endpoint you are calling (`/v1beta/models/gemini-pro:generateContent`) is returning a **404 Not Found** error.
- This means the endpoint does not exist, is not available, or your API key/project does not have access to it.

---

## **How to Fix**

### 1. **Check the Gemini API Endpoint**
- The correct endpoint for Gemini 1.5 Pro (as of July 2024) is:
  ```
  https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key=YOUR_API_KEY
  ```
- Your code is using `gemini-pro`, which is not a valid model name.

### 2. **Update Your Endpoint in GeminiClient**
- Change:
  ```java
  private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
  ```
- To:
  ```java
  private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent";
  ```

### 3. **Rebuild and Restart**
- Save the change.
- Rebuild your project and restart the backend.
- Test again with Postman.

---

## **Summary of Steps**

1. **Update the model name in your GeminiClient to `gemini-1.5-pro-latest`.**
2. **Rebuild and restart your backend.**
3. **Test the endpoint again.**

Would you like me to make this code change for you? 