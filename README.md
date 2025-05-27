# Sistema Tutorias: A Hexagonal Architecture-based Chapter Management System

Sistema Tutorias is a Spring Boot application that provides a robust REST API for managing educational chapters using hexagonal architecture (ports and adapters pattern). It offers a clean separation of concerns between business logic, persistence, and presentation layers while providing comprehensive chapter management capabilities with built-in validation and error handling.

The system implements a domain-driven design approach with clear boundaries between the application core and external dependencies. It uses SQLite for data persistence, supports input validation, and provides internationalized error messages. The hexagonal architecture ensures that the business logic remains isolated from external concerns, making the system highly maintainable and testable.

## Repository Structure
```
.
├── mvnw/mvnw.cmd              # Maven wrapper scripts for building without local Maven installation
├── pom.xml                    # Maven project configuration and dependencies
└── src
    ├── main/java/com/pragma/sistematutorias
    │   ├── chapter                           # Chapter domain module
    │   │   ├── application                   # Application services and use cases
    │   │   ├── domain                        # Core domain model and ports
    │   │   └── infrastructure                # External adapters (REST, persistence)
    │   ├── shared                           # Shared components across domains
    │   │   ├── dto                          # Common DTOs for API responses
    │   │   ├── exception                    # Global exception handling
    │   │   └── service                      # Shared services (e.g., MessageService)
    │   └── SistematutoriasApplication.java  # Application entry point
    └── resources                            # Application properties and messages
```

## Usage Instructions
### Prerequisites
- Java 21 or higher
- SQLite database
- Maven 3.9.9 or higher (optional, wrapper included)

### Installation

#### Using Maven Wrapper
```bash
# Clone the repository
git clone <repository-url>
cd sistematutorias

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

#### Using Local Maven Installation
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Quick Start
1. Start the application using one of the installation methods above
2. The API will be available at `http://localhost:8080/api/chapter`

Basic API Usage:
```bash
# Get all chapters
curl http://localhost:8080/api/chapter/

# Create a new chapter
curl -X POST http://localhost:8080/api/chapter/ \
  -H "Content-Type: application/json" \
  -d '{"title":"Chapter 1","description":"Introduction"}'

# Get a specific chapter
curl http://localhost:8080/api/chapter/{id}
```

### More Detailed Examples
```java
// Creating a chapter with validation
POST /api/chapter/
{
  "title": "Chapter 1",
  "description": "Introduction to the course",
  "order": 1
}

// Response
{
  "message": "Chapter created successfully",
  "data": {
    "id": "1234",
    "title": "Chapter 1",
    "description": "Introduction to the course",
    "order": 1
  }
}
```

### Troubleshooting

Common Issues:
1. Database Connection Issues
   - Error: "Cannot open database file"
   - Solution: Ensure SQLite database file has proper permissions
   - Check: `src/main/resources/application.properties` for database configuration

2. Validation Errors
   - Error: 400 Bad Request with validation details
   - Solution: Check request payload against API requirements
   - Enable debug logging: Add `logging.level.com.pragma=DEBUG` to application.properties

Debug Mode:
```properties
# Add to application.properties
logging.level.com.pragma=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Data Flow
The system follows a clean hexagonal architecture pattern where requests flow through well-defined boundaries from the REST controller through the domain layer to the persistence layer.

```ascii
[Client] → [REST Controller] → [Use Cases] → [Domain Model] → [Repository]
    ↑                                                              ↓
    └──────────────────── [Database Adapter] ←────────────────────┘
```

Component Interactions:
1. REST Controller receives HTTP requests and converts them to DTOs
2. DTOs are mapped to domain objects using MapStruct
3. Use cases implement business logic and validation
4. Domain model maintains business rules and invariants
5. Repository interface defines persistence operations
6. Database adapter implements persistence using Spring Data
7. Global exception handler provides consistent error responses
8. Message service handles internationalization of responses