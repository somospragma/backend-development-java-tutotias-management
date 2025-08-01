# Sistema de Tutorías

## Descripción General

El Sistema de Tutorías es una aplicación desarrollada con Spring Boot que facilita la gestión de tutorías entre tutores y tutorados dentro de una organización. La plataforma permite solicitar tutorías sobre habilidades específicas, gestionar el ciclo de vida completo de las tutorías, programar sesiones, y proporcionar retroalimentación.

El sistema está diseñado siguiendo los principios de la Arquitectura Hexagonal (Ports and Adapters), lo que permite una clara separación de responsabilidades y facilita la mantenibilidad y escalabilidad del código.

## Arquitectura del Sistema

### Arquitectura Hexagonal

El proyecto implementa la Arquitectura Hexagonal (también conocida como Ports and Adapters), que organiza el código en tres capas principales:

1. **Dominio**: Contiene las entidades de negocio, reglas y lógica de dominio.
2. **Aplicación**: Orquesta los casos de uso mediante servicios que coordinan las operaciones del dominio.
3. **Infraestructura**: Implementa los adaptadores para interactuar con el mundo exterior (API REST, base de datos, etc.).

### Estructura de Paquetes

La estructura del proyecto sigue un enfoque modular por funcionalidad:

```
com.pragma
├── chapter/                  # Módulo de capítulos/departamentos
├── feedbacks/                # Módulo de retroalimentación
├── shared/                   # Componentes compartidos
├── skills/                   # Módulo de habilidades
├── tutoring_sessions/        # Módulo de sesiones de tutoría
├── tutorings/                # Módulo de tutorías
├── tutorings_requests/       # Módulo de solicitudes de tutoría
└── usuarios/                 # Módulo de usuarios
```

Cada módulo sigue la estructura de la arquitectura hexagonal:

```
módulo/
├── application/
│   └── service/              # Servicios de aplicación
├── domain/
│   ├── model/                # Entidades de dominio
│   └── port/
│       ├── input/            # Puertos de entrada (casos de uso)
│       └── output/           # Puertos de salida (repositorios)
└── infrastructure/
    └── adapter/
        ├── input/
        │   └── rest/         # Controladores REST
        │       ├── dto/      # Objetos de transferencia de datos
        │       └── mapper/   # Mapeadores DTO <-> Entidad
        └── output/
            └── persistence/  # Implementación de repositorios
                ├── entity/   # Entidades JPA
                ├── mapper/   # Mapeadores Entidad <-> Modelo
                └── repository/ # Repositorios Spring Data
```

## Diagrama de Componentes

```mermaid
graph TD
    subgraph "Capa de Presentación"
        REST[API REST]
    end
    
    subgraph "Capa de Aplicación"
        US[Servicios de Aplicación]
    end
    
    subgraph "Capa de Dominio"
        UC[Casos de Uso]
        DM[Modelos de Dominio]
        RP[Puertos de Repositorio]
    end
    
    subgraph "Capa de Infraestructura"
        PA[Adaptadores de Persistencia]
        DB[(Base de Datos SQLite)]
    end
    
    REST --> US
    US --> UC
    UC --> DM
    UC --> RP
    RP --> PA
    PA --> DB
```

## Diagrama de Flujo de Datos

```mermaid
sequenceDiagram
    actor Usuario
    participant Controller as Controller
    participant Service as Service
    participant UseCase as UseCase
    participant Repository as Repository
    participant Database as Database
    
    Usuario->>Controller: Solicitud HTTP
    Controller->>Service: Llamada al servicio
    Service->>UseCase: Ejecuta caso de uso
    UseCase->>Repository: Solicita datos
    Repository->>Database: Consulta
    Database-->>Repository: Resultado
    Repository-->>UseCase: Datos
    UseCase-->>Service: Resultado procesado
    Service-->>Controller: Respuesta
    Controller-->>Usuario: Respuesta HTTP
```

## Diagrama de Entidades

```mermaid
classDiagram
    direction LR

    class Chapter {
        +String id
        +String name
    }
    
    class User {
        +String id
        +String firstName
        +String lastName
        +String email
        +Chapter chapter
        +RolUsuario rol
        +int activeTutoringLimit
    }

    class Skill {
        +String id
        +String name
    }

    class Tutoring {
        +String id
        +User tutor
        +User tutee
        +List~Skill~ skills
        +Date startDate
        +Date expectedEndDate
        +TutoringStatus status
        +String objectives
    }

    class TutoringSession {
        +String id
        +Tutoring tutoring
        +String datetime
        +int durationMinutes
        +String locationLink
        +String topicsCovered
        +String notes
        +TutoringsSessionStatus sessionStatus
    }

    class TutoringRequest {
        +String id
        +User tutee
        +List~Skill~ skills
        +String needsDescription
        +Date requestDate
        +RequestStatus requestStatus
        +String assignedTutoringId
    }

    class Feedback {
        +String id
        +User evaluator
        +Date evaluationDate
        +Tutoring tutoring
        +String score
        +String comments
    }

    User "1" -- "1" Chapter : pertenece a
    User "1" -- "0..N" TutoringRequest : solicita
    User "1" -- "0..N" Tutoring : es tutor en
    User "1" -- "0..N" Tutoring : es tutorado en
    User "1" -- "0..N" Feedback : proporciona
    
    Tutoring "1" -- "0..N" TutoringSession : tiene
    Tutoring "1" -- "0..N" Feedback : recibe
    Tutoring "1" -- "0..N" Skill : aborda
    
    TutoringRequest "0..1" -- "0..1" Tutoring : resulta en
    TutoringRequest "1" -- "0..N" Skill : requiere
```

## Módulos Principales

### 1. Usuarios (Users)

Gestiona la información de los usuarios del sistema, que pueden tener diferentes roles:
- **Tutor**: Puede impartir tutorías en sus áreas de experiencia.
- **Tutorado**: Puede solicitar tutorías en habilidades que desea desarrollar.
- **Administrador**: Gestiona el sistema.

### 2. Habilidades (Skills)

Representa las competencias o conocimientos que pueden ser objeto de tutoría.

### 3. Capítulos (Chapters)

Representa los departamentos o áreas de la organización a las que pertenecen los usuarios.

### 4. Solicitudes de Tutoría (Tutoring Requests)

Gestiona las peticiones de tutoría realizadas por los tutorados, con estados:
- **Enviada**: Estado inicial de una solicitud.
- **Aprobada**: La solicitud ha sido aprobada pero aún no asignada a un tutor.
- **Asignada**: Se ha asignado un tutor y se ha creado una tutoría.
- **Rechazada**: La solicitud ha sido rechazada.

### 5. Tutorías (Tutorings)

Representa las tutorías activas entre un tutor y un tutorado, con estados:
- **Activa**: La tutoría está en curso.
- **Completada**: La tutoría ha finalizado satisfactoriamente.
- **Cancelada**: La tutoría ha sido cancelada.

### 6. Sesiones de Tutoría (Tutoring Sessions)

Gestiona las sesiones individuales dentro de una tutoría, con estados:
- **Programada**: La sesión está planificada.
- **Realizada**: La sesión se ha llevado a cabo.
- **Cancelada**: La sesión ha sido cancelada.

### 7. Retroalimentación (Feedbacks)

Permite a los usuarios proporcionar evaluaciones y comentarios sobre las tutorías.

## Flujos Principales

### Flujo de Solicitud y Asignación de Tutoría

1. Un tutorado crea una solicitud de tutoría especificando las habilidades requeridas.
2. La solicitud se marca como "Enviada".
3. Un administrador revisa la solicitud y la aprueba o rechaza.
4. Si es aprobada, se asigna a un tutor disponible.
5. Se crea una tutoría vinculada a la solicitud.
6. La solicitud se marca como "Asignada".

### Flujo de Gestión de Tutorías

1. Una vez creada, la tutoría tiene estado "Activa".
2. Se pueden programar múltiples sesiones de tutoría.
3. Al finalizar, el tutor o tutorado puede marcar la tutoría como "Completada".
4. En cualquier momento, la tutoría puede ser "Cancelada".
5. Los usuarios pueden proporcionar retroalimentación sobre la tutoría.

## Requisitos de Instalación

### Prerrequisitos

- Java 21
- Maven 3.9+
- SQLite (incluido como dependencia)

### Configuración

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/sistematutorias.git
   cd sistematutorias
   ```

2. Compilar el proyecto:
   ```bash
   mvn clean install
   ```

3. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```

### Configuración con Docker

También puedes ejecutar la aplicación usando Docker:

```bash
# Construir la imagen
docker build -t sistematutorias .
```

```bash
# Ejecutar el contenedor
docker run -p 8080:8080 -v sistematutorias-data:/data sistematutorias
```

```bash
# Usando docker-compose
docker-compose up -d
```

## API REST

La API REST está documentada con OpenAPI. Puedes acceder a la documentación en:
```
http://localhost:8080/swagger-ui.html
```

### Endpoints Principales

- **Usuarios**: `/api/v1/users`
- **Habilidades**: `/api/v1/skills`
- **Capítulos**: `/api/chapter`
- **Solicitudes de Tutoría**: `/api/v1/tutoring-requests`
- **Tutorías**: `/api/v1/tutorings`
- **Sesiones de Tutoría**: `/api/v1/tutoring-sessions`
- **Retroalimentación**: `/api/v1/feedbacks`

## Pruebas

El proyecto incluye pruebas unitarias para cada componente. Para ejecutar las pruebas:

```bash
mvn test
```

## Tecnologías Utilizadas

- **Spring Boot**: Framework principal
- **Spring Data JPA**: Persistencia de datos
- **SQLite**: Base de datos
- **Lombok**: Reducción de código boilerplate
- **MapStruct**: Mapeo entre objetos
- **JUnit 5**: Pruebas unitarias
- **Docker**: Contenerización

## Notas para Desarrolladores

- El proyecto sigue los principios SOLID y Clean Architecture.
- Se utiliza el patrón de diseño Repository para el acceso a datos.
- Los DTOs (Data Transfer Objects) se utilizan para la comunicación entre capas.
- Los mappers automatizan la conversión entre entidades de dominio y DTOs/entidades de persistencia.
- La validación de datos se realiza tanto a nivel de API como de dominio.

## Contribución

Para contribuir al proyecto:

1. Crear una rama para tu funcionalidad: `git checkout -b feature/nueva-funcionalidad`
2. Realizar cambios y pruebas
3. Enviar un Pull Request

## Licencia

Este proyecto está licenciado bajo [Licencia Propietaria].