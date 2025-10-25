# Spring-Boot-et-Swagger

A Spring Boot REST API for managing students with MySQL persistence. The project integrates Swagger (springdoc-openapi) for interactive API documentation and JaCoCo for test coverage reporting.

## Features
- Student management: create, list, delete, count, and stats by birth year
- Swagger UI for live API exploration and testing
- Spring Data JPA with Hibernate for persistence
- Unit tests with JUnit 5 and Mockito
- JaCoCo HTML coverage report

## Tech Stack
- Spring Boot 3
- Spring Web, Spring Data JPA, Validation
- MySQL (Connector/J)
- Swagger via `springdoc-openapi-starter-webmvc-ui`
- JUnit 5, Mockito
- JaCoCo

## Prerequisites
- Java 21+ (`java -version`)
- MySQL server accessible at `localhost:3306`
- Maven Wrapper included (`mvnw` / `mvnw.cmd`)

## Configuration
Application properties live in `src/main/resources/application.properties`.

Datasource and JPA settings:
```
spring.datasource.url=jdbc:mysql://localhost:3306/studentdb?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update

# Choose the right dialect for your MySQL version
# For MySQL 8+
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
# For MySQL 5.x (example)
# spring.jpa.properties.hibernate.dialect=org.hibernate.community.dialects.MySQL56Dialect

# Optional: explicitly set open-in-view strategy
# spring.jpa.open-in-view=false
```

## Running the Application
Start on port 8080:
```
./mvnw spring-boot:run
```
Override the port:
```
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```
Once started, open Swagger UI:
```
http://localhost:8080/swagger-ui.html
```
OpenAPI JSON is available at:
```
http://localhost:8080/v3/api-docs
```

## API Overview
Base path: `http://localhost:8080/api/students`

- `POST /api/students` — Create a student
- `GET /api/students` — List all students
- `DELETE /api/students/{id}` — Delete a student by id
- `GET /api/students/count` — Count total students
- `GET /api/students/byYear` — Stats by birth year (year → count)

### Example Payloads
Create a student:
```
POST /api/students
Content-Type: application/json
{
  "nom": "Mido",
  "prenom": "Ali",
  "dateNaissance": "1995-06-10"
}
```

### Curl Examples
```
# Create
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"nom":"Mido","prenom":"Ali","dateNaissance":"1995-06-10"}'

# List
curl http://localhost:8080/api/students

# Delete
curl -X DELETE http://localhost:8080/api/students/1

# Count
curl http://localhost:8080/api/students/count

# Stats by year
curl http://localhost:8080/api/students/byYear
```

## Testing
Run unit tests:
```
./mvnw -q test
```
Run full verification (tests + coverage):
```
./mvnw -q clean verify
```

### Coverage (JaCoCo)
After running `verify`, open the HTML report:
```
target/site/jacoco/index.html
```
The report shows overall coverage, per-package/class details, and annotated source highlighting for covered/uncovered lines.

## Project Structure
```
student-management/
├─ pom.xml
├─ src
│  ├─ main
│  │  ├─ java/com/example/student_management
│  │  │  ├─ controller (HomeController, StudentController)
│  │  │  ├─ entity (Student)
│  │  │  ├─ repository (StudentRepository)
│  │  │  └─ service (StudentService)
│  │  └─ resources/application.properties
│  └─ test
│     └─ java/com/example/student_management/controller/StudentControllerTest.java
└─ target (build output, JaCoCo report)
```


## Troubleshooting
- Port in use: start with `--server.port=8081` or stop the existing app
- MySQL connection errors: validate credentials and `spring.datasource.*` settings
- Hibernate dialect warnings: set the dialect matching your MySQL version
- Mockito agent warnings: using `-XX:+EnableDynamicAgentLoading` can reduce JDK agent warnings during tests

## Notes
- Base mapping is `/api/students` via `@RequestMapping("/api/students")`
- Swagger annotations (`@Operation`) are applied to document endpoints in the UI
