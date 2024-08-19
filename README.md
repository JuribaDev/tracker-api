# Expense Tracker API

## Overview

This project is an Expense Tracker API built with Spring Boot. It provides a robust backend for managing expenses, categories, and user authentication. The application follows a clean architecture pattern and incorporates domain-driven design principles.

## Features

- User Authentication and Authorization
- Expense Management
- Category Management
- Role-based Access Control
- Audit Logging Events based on Spring Application Events
- JWT Token-based Authentication
- OpenAPI (Swagger) Documentation
- Pagination and Sorting for List Operations

## Technology Stack

- Java 17
- Spring Boot 3.3.2
- Spring Security with JWT
- Spring Data JPA
- H2 Database (for development)
- Maven
- GitHub Actions (CI/CD)
- Lombok
- SpringDoc OpenAPI (Swagger)
- Rate Limiting with Bucket4j

## Project Structure

The project follows a clean architecture with the following main packages:

- `com.juriba.tracker.auth`: Authentication and authorization
- `com.juriba.tracker.user`: User management
- `com.juriba.tracker.expense`: Expense and category management
- `com.juriba.tracker.audit`: Audit logging
- `com.juriba.tracker.common`: Common utilities and base classes

Each feature area is further divided into:

- `application`: Use cases and application services
- `domain`: Domain models and events
- `infrastructure`: Repositories and external services
- `presentation`: Controllers and DTOs

## Getting Started

### Prerequisites

- Java 17 or later
- Maven
- openssl

### Setup

1. Clone the repository:
   ```
   git clone [repository-url]
   ```

2. Navigate to the project directory:
   ```
   cd tracker-api
   ```

3. Run the setup script to generate RSA keys for JWT:
   ```
   ./setup_project.sh
   ```

4. Build the project:
   ```
   mvn clean install
   ```

5. Run the application:
   ```
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
   
6. For testing, you can use the generated following credentials:
   ```
   admin User Email: admin@tracker.com
   admin User Password: adminPassword
   
   user User Email: user@tracker.com
   user User Password: userPassword  
   ```

The application will start on `http://localhost:8080`.

## API Documentation

Once the application is running, you can access the Swagger UI for API documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

## Configuration

The main configuration files are:

- `src/main/resources/application.properties`: Main application properties
- `src/main/resources/application-dev.properties`: Development-specific properties

## Testing

Run the tests using:

```
mvn test
```

## Security

- The application uses JWT for authentication.
- Passwords are encrypted using BCrypt.
- Role-based access control is implemented for different endpoints.

