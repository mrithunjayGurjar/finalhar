# CRM Ticketing System - Replit Project

## Overview
A comprehensive CRM Ticketing System built with Java 21, Spring Boot 3.x, and MySQL. This system provides ticket management, user authentication with JWT, role-based access control, and a complete escalation workflow.

## Recent Changes
- **2025-11-20**: Initial project setup
  - Created complete Spring Boot application structure
  - Implemented JWT authentication with Spring Security
  - Built all entities, repositories, services, and controllers
  - Set up MySQL database with automatic schema creation
  - Configured workflow for automatic startup

## Project Architecture

### Technology Stack
- **Backend Framework**: Spring Boot 3.2.0
- **Language**: Java 21 (using records for DTOs)
- **Security**: Spring Security with JWT
- **Database**: MySQL 8.0.42
- **Build Tool**: Maven 3.9.9
- **Key Libraries**: Lombok, Spring Data JPA, JJWT

### Architecture Pattern
Controller → Service → Repository

### Key Features Implemented
1. User authentication and registration (JWT-based)
2. Role-based access (USER, AGENT, MANAGER)
3. Ticket creation and management
4. Priority-based ticket handling (LOW, MEDIUM, HIGH, CRITICAL)
5. Status tracking workflow (OPEN → IN_PROGRESS → ESCALATED → RESOLVED → CLOSED)
6. Ticket notes system (customer and team replies)
7. Automatic escalation workflow
8. Active tickets queue

## Project Structure

```
/
├── src/main/java/com/crm/
│   ├── controllers/        # REST endpoints
│   ├── services/          # Business logic
│   ├── repositories/      # Data access
│   ├── entities/          # JPA entities
│   ├── dto/               # Java 21 records
│   ├── security/          # JWT configuration
│   └── config/            # Spring config
├── src/main/resources/
│   └── application.properties
├── pom.xml                # Maven dependencies
├── start.sh              # Startup script
├── sample-data.sql       # Test data
├── postman-collection.json
└── README.md

```

## Running the Application

The workflow "Run CRM Application" is configured to automatically:
1. Initialize MySQL database
2. Start MySQL server
3. Start Spring Boot application on port 5000

The application exposes a REST API on port 5000 accessible via the webview.

## API Endpoints

### Public Endpoints
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login and get JWT token

### Protected Endpoints (require JWT token)
- `POST /api/tickets` - Create ticket
- `GET /api/tickets/{id}` - Get ticket details
- `PUT /api/tickets/{id}/status` - Update status
- `PUT /api/tickets/{id}/escalate` - Escalate ticket
- `GET /api/tickets/active` - List active tickets
- `POST /api/tickets/{id}/notes` - Add note
- `GET /api/tickets/{id}/notes` - Get notes

## Database Schema

The application uses Hibernate's auto-DDL feature to create tables:
- `users` - User accounts with roles
- `tickets` - Support tickets
- `ticket_notes` - Comments on tickets

## Testing

Use the provided `postman-collection.json` to test all endpoints. Set the `baseUrl` variable to your Replit URL.

## User Preferences

None specified yet.

## Dependencies

All dependencies are managed in `pom.xml`:
- Spring Boot starters (Web, Data JPA, Security, Validation)
- MySQL Connector
- JJWT (JWT implementation)
- Lombok (code generation)

## Notes

- MySQL runs in-memory in `/tmp/mysql-data`
- JWT secret is configured in `application.properties`
- Default JWT token expiration: 24 hours
- All passwords are BCrypt hashed
- Database schema is auto-created on startup
