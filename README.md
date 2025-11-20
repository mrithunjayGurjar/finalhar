# CRM Ticketing System

A simple CRM Ticketing System built with Java 21, Spring Boot 3.x, Spring Security with JWT, and MySQL.

## Features

- **User Authentication**: Register and login with JWT token-based authentication
- **Role-based Access**: Support for USER, AGENT, and MANAGER roles
- **Ticket Management**: Create, view, and manage customer support tickets
- **Priority Levels**: LOW, MEDIUM, HIGH, CRITICAL
- **Status Tracking**: OPEN → IN_PROGRESS → ESCALATED → RESOLVED → CLOSED
- **Ticket Notes**: Add customer replies and team replies to tickets
- **Escalation Workflow**: Automatic routing from USER → AGENT → MANAGER
- **Active Tickets Queue**: View all non-closed tickets

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.2.0
- **Spring Security**: JWT-based authentication
- **Spring Data JPA**: Database persistence
- **MySQL**: 8.0.42
- **Maven**: 3.9.9
- **Lombok**: Boilerplate code reduction

## Project Structure

```
src/main/java/com/crm/
├── controllers/        # REST API endpoints
│   ├── AuthController.java
│   └── TicketController.java
├── services/          # Business logic
│   ├── AuthService.java
│   └── TicketService.java
├── repositories/      # JPA repositories
│   ├── UserRepository.java
│   ├── TicketRepository.java
│   └── TicketNoteRepository.java
├── entities/          # JPA entities
│   ├── User.java
│   ├── Ticket.java
│   ├── TicketNote.java
│   ├── Role.java
│   ├── Priority.java
│   ├── TicketStatus.java
│   └── NoteType.java
├── dto/               # Java 21 Records for DTOs
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   ├── CreateTicketRequest.java
│   ├── UpdateStatusRequest.java
│   ├── AddNoteRequest.java
│   ├── TicketResponse.java
│   ├── NoteResponse.java
│   └── ActiveTicketResponse.java
├── security/          # JWT security configuration
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
└── config/            # Spring configuration
    └── SecurityConfig.java
```

## API Endpoints

### Authentication

- **POST** `/auth/register` - Register a new user
- **POST** `/auth/login` - Login and get JWT token

### Tickets

- **POST** `/api/tickets` - Create a new ticket
- **GET** `/api/tickets/{id}` - Get ticket details
- **PUT** `/api/tickets/{id}/status` - Update ticket status
- **PUT** `/api/tickets/{id}/escalate` - Escalate ticket
- **GET** `/api/tickets/active` - Get all active tickets

### Ticket Notes

- **POST** `/api/tickets/{id}/notes` - Add a note to a ticket
- **GET** `/api/tickets/{id}/notes` - Get all notes for a ticket

## Running the Application

The application is configured to run automatically on Replit. The workflow starts MySQL and then runs the Spring Boot application.

### Manual Run

```bash
# Make the start script executable
chmod +x start.sh

# Run the application
./start.sh
```

The application will:
1. Initialize MySQL database (if needed)
2. Start MySQL server
3. Wait for MySQL to be ready
4. Start Spring Boot application on port 5000

### Build Only

```bash
mvn clean package -DskipTests
```

## Database Schema

### Users Table
- `id` (BIGINT, Primary Key)
- `name` (VARCHAR)
- `email` (VARCHAR, Unique)
- `password` (VARCHAR, BCrypt hashed)
- `role` (ENUM: USER, AGENT, MANAGER)

### Tickets Table
- `id` (BIGINT, Primary Key)
- `title` (VARCHAR)
- `description` (TEXT)
- `created_by` (BIGINT, Foreign Key → users)
- `assigned_to` (BIGINT, Foreign Key → users)
- `priority` (ENUM: LOW, MEDIUM, HIGH, CRITICAL)
- `status` (ENUM: OPEN, IN_PROGRESS, ESCALATED, RESOLVED, CLOSED)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Ticket Notes Table
- `id` (BIGINT, Primary Key)
- `ticket_id` (BIGINT, Foreign Key → tickets)
- `author_id` (BIGINT, Foreign Key → users)
- `message` (TEXT)
- `type` (ENUM: CUSTOMER_REPLY, TEAM_REPLY)
- `created_at` (TIMESTAMP)

## Testing

Import the provided `postman-collection.json` into Postman to test all API endpoints.

### Sample Test Flow

1. **Register Users**:
   - Register as USER, AGENT, and MANAGER
   
2. **Login**:
   - Login with each user to get JWT tokens
   
3. **Create Ticket**:
   - Use USER token to create a ticket
   
4. **Add Notes**:
   - Add customer and team replies
   
5. **Update Status**:
   - Change ticket status through workflow
   
6. **Escalate**:
   - Test escalation from USER → AGENT → MANAGER
   
7. **View Active Tickets**:
   - Get list of all non-closed tickets

## Configuration

Key settings in `application.properties`:

```properties
server.port=5000
spring.datasource.url=jdbc:mysql://localhost:3306/crm_ticketing
spring.jpa.hibernate.ddl-auto=update
jwt.expiration=86400000
```

## Security

- Passwords are hashed using BCrypt
- JWT tokens expire after 24 hours
- All endpoints except `/auth/**` require authentication
- Tokens must be sent in `Authorization: Bearer <token>` header

## Sample Data

Use `sample-data.sql` to populate the database with test data:
- 4 users (1 USER, 2 AGENTS, 1 MANAGER)
- 4 sample tickets with various statuses
- 9 sample ticket notes

## Status Transitions

Valid status transitions (strict linear workflow):
- OPEN → IN_PROGRESS
- IN_PROGRESS → ESCALATED
- ESCALATED → RESOLVED
- RESOLVED → CLOSED
- CLOSED → (no transitions allowed)

Note: All tickets must follow the complete linear workflow. Every ticket must be escalated before resolution.

## Escalation Rules

- If assigned to USER → escalate to AGENT
- If assigned to AGENT → escalate to MANAGER  
- If assigned to MANAGER → status becomes ESCALATED

## License

MIT License
