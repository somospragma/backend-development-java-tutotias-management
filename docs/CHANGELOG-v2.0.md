# Changelog - Sistema de Tutorías v2.0

## Nuevas Funcionalidades Implementadas

### 🔐 Mejoras en Autenticación y Seguridad

#### UserContextHelper - Utilidades de Contexto
- **Funcionalidad**: Clase utilitaria para validación de permisos y acceso a recursos
- **Métodos principales**:
  - `getCurrentUserOrThrow()`: Obtiene usuario actual o lanza excepción
  - `requireAdminRole()`: Valida que el usuario sea administrador
  - `requireResourceAccess(String resourceId)`: Valida acceso a recursos específicos
  - `canActAsTutor()`: Verifica si el usuario puede actuar como tutor
  - `isCurrentUserAdmin()`: Verifica si el usuario actual es administrador

#### Asignación Automática de Evaluadores
- **Cambio**: El campo `evaluatorId` se elimina del body de peticiones de feedback
- **Implementación**: Se asigna automáticamente desde el contexto del usuario autenticado
- **Beneficio**: Mayor seguridad y consistencia en la asignación de evaluadores

### 👥 Mejoras en Gestión de Usuarios

#### Integración con API Externa (CRECI)
- **Funcionalidad**: Validación automática de usuarios contra sistema externo
- **Endpoint**: Integración con API CRECI para verificar usuarios válidos
- **Implementación**: 
  - `PragmaUserAdapter`: Adaptador para comunicación con API externa
  - `GetExternalUserUseCase`: Caso de uso para obtener datos externos
  - Validación obligatoria en creación de usuarios

#### Filtros Avanzados en Listado de Usuarios
- **Endpoint**: `GET /api/v1/users`
- **Parámetros de filtro**:
  - `chapterId`: Filtrar por capítulo
  - `rol`: Búsqueda parcial por rol (operador LIKE)
  - `seniority`: Filtrar por nivel de seniority
  - `email`: Búsqueda parcial por email
- **Lógica**: Operadores OR entre filtros para búsqueda flexible

#### Endpoint de Perfil de Usuario
- **Endpoint**: `GET /api/v1/users/me`
- **Funcionalidad**: Obtener información del usuario autenticado
- **Respuesta**: Datos completos del perfil del usuario actual

#### Gestión de Límites de Tutoría
- **Endpoint**: `PATCH /api/v1/users/tutoring-limit`
- **Funcionalidad**: Configuración dinámica del límite de tutorías activas por usuario
- **Restricción**: Solo administradores pueden modificar límites

### 📚 Mejoras en Gestión de Tutorías

#### Vista Detallada de Tutorías
- **Endpoint**: `GET /api/v1/tutorings/{id}/details`
- **Funcionalidad**: Información completa de tutoría incluyendo:
  - Datos básicos de la tutoría
  - Lista de sesiones asociadas
  - Feedbacks recibidos
  - Información completa de tutor y tutorado
- **DTO**: `TutoringDetailDto` con estructura completa

#### Filtros por Participante
- **Endpoint**: `GET /api/v1/tutorings`
- **Parámetros**: 
  - `tutorId`: Filtrar tutorías por tutor específico
  - `tuteeId`: Filtrar tutorías por tutorado específico
- **Uso**: Permite a usuarios ver sus tutorías específicas

#### Gestión Mejorada de Cancelaciones
- **Estado nuevo**: `SOLICITUD_CANCELACION`
- **Lógica**: 
  - Usuarios regulares: Solicitan cancelación (estado intermedio)
  - Administradores: Cancelan directamente
- **Endpoint**: `PATCH /api/v1/tutorings/{id}/cancel`

#### Campo URL de Acta Final
- **Campo**: `finalActUrl` en modelo de Tutoría
- **Funcionalidad**: Almacenar enlace al documento final de la tutoría
- **Uso**: Se incluye al completar una tutoría

### 📝 Mejoras en Solicitudes de Tutoría

#### Estado "Conversando"
- **Estado nuevo**: `CONVERSANDO`
- **Propósito**: Estado intermedio durante negociación de tutoría
- **Flujo**: Enviada → Conversando → Aprobada/Rechazada

#### Eliminación Automática de Solicitudes Huérfanas
- **Funcionalidad**: Las solicitudes sin tutoría asignada se eliminan automáticamente al cancelarse
- **Beneficio**: Mantiene limpia la base de datos y evita solicitudes inconsistentes

### 🔄 Nuevos Servicios de Consulta

#### GetFeedbacksService
- **Funcionalidad**: Servicio dedicado para obtener feedbacks por tutoría
- **Método**: `getFeedbacksByTutoringId(String tutoringId)`
- **Integración**: Usado en vista detallada de tutorías

#### GetTutoringSessionsService
- **Funcionalidad**: Servicio dedicado para obtener sesiones por tutoría
- **Método**: `getSessionsByTutoringId(String tutoringId)`
- **Integración**: Usado en vista detallada de tutorías

### 🛠️ Mejoras Técnicas

#### Mappers Especializados
- **TutoringDetailDtoMapper**: Mapper específico para vista detallada
- **Funcionalidad**: Conversión compleja entre modelo de dominio y DTO detallado
- **Método**: `toDetailDto(Tutoring, List<TutoringSession>, List<Feedback>)`

#### Configuración de APIs Externas
- **Clase**: `ExternalApiProperties`
- **Funcionalidad**: Configuración centralizada para URLs de APIs externas
- **Propiedades**: URLs base y endpoints específicos

#### RestTemplate Configurado
- **Clase**: `RestTemplateConfig`
- **Funcionalidad**: Cliente HTTP configurado para integraciones
- **Características**: Timeouts, manejo de errores, headers por defecto

### 🧪 Cobertura de Pruebas

#### Nuevas Clases de Prueba
- `TutoringDetailControllerTest`: Pruebas para vista detallada
- `UserControllerGetAllUsersFilterTest`: Pruebas para filtros de usuarios
- `PragmaUserAdapterTest`: Pruebas para integración externa
- `GetFeedbacksServiceTest`: Pruebas para servicio de feedbacks
- `GetTutoringSessionsServiceTest`: Pruebas para servicio de sesiones

#### Cobertura Mejorada
- Pruebas de integración para autenticación
- Pruebas de controladores con contexto de usuario
- Pruebas de adaptadores externos con mocking
- Pruebas de servicios de consulta especializados

## Cambios de Configuración

### Nuevas Propiedades
```properties
# Configuración de API externa
external.api.creci.base-url=https://api.creci.example.com
external.api.creci.users-endpoint=/api/v1/users

# Configuración de autenticación
app.auth.header-name=Authorization
app.auth.protected-paths=/api/**
```

### Perfiles Actualizados
- **local**: SQLite con datos de prueba
- **dev**: MySQL con integración externa habilitada
- **test**: Configuración específica para pruebas con mocks

## Impacto en la Arquitectura

### Nuevas Capas
- **Adaptadores Externos**: Comunicación con APIs externas
- **Servicios de Consulta**: Servicios especializados para obtención de datos
- **Utilidades de Contexto**: Helpers para validación de permisos

### Patrones Implementados
- **Adapter Pattern**: Para integraciones externas
- **Strategy Pattern**: Para diferentes tipos de validación de usuarios
- **Facade Pattern**: Para simplificar acceso a datos complejos (vista detallada)

## Próximos Pasos Recomendados

1. **Monitoreo**: Implementar métricas para integraciones externas
2. **Cache**: Agregar cache para consultas frecuentes de usuarios externos
3. **Notificaciones**: Sistema de notificaciones para cambios de estado
4. **Audit**: Logging detallado de cambios en tutorías y solicitudes
5. **Performance**: Optimización de consultas complejas con múltiples joins

## Notas de Migración

### Base de Datos
- Nuevos campos en tabla `tutorings`: `final_act_url`
- Nuevos estados en enums: `CONVERSANDO`, `SOLICITUD_CANCELACION`
- Índices recomendados para filtros de usuarios

### API
- Cambio en endpoint de feedbacks: `evaluatorId` ya no se envía en body
- Nuevos endpoints disponibles para vista detallada
- Filtros opcionales en endpoints existentes (backward compatible)

### Autenticación
- Validación más estricta de permisos
- Asignación automática de usuarios en operaciones
- Nuevas utilidades para validación de acceso a recursos