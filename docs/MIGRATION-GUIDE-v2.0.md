# Guía de Migración - Sistema de Tutorías v2.0

## Resumen de Cambios

Esta guía documenta los cambios necesarios para migrar desde la versión 1.0 a la versión 2.0 del Sistema de Tutorías. La v2.0 introduce mejoras significativas en funcionalidad, seguridad y integración externa, manteniendo la compatibilidad hacia atrás en la mayoría de casos.

## Cambios en la API

### 🔄 Cambios en Endpoints Existentes

#### 1. Endpoint de Usuarios - GET /api/v1/users

**Antes (v1.0):**
```http
GET /api/v1/users
```

**Ahora (v2.0):**
```http
GET /api/v1/users?chapterId=123&rol=TUTOR&seniority=3&email=john
```

**Cambios:**
- ✅ **Nuevos parámetros de filtro opcionales**
- ✅ **Backward compatible** - funciona sin parámetros
- ✅ **Respuesta mejorada** con conteo de tutorías activas

**Ejemplo de respuesta v2.0:**
```json
[
  {
    "id": "user-123",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "chapter": {...},
    "rol": "TUTOR",
    "activeTutoringLimit": 5,
    "seniority": 3,
    "activeTutoringCount": 2
  }
]
```

#### 2. Endpoint de Feedbacks - POST /api/v1/feedbacks

**Antes (v1.0):**
```json
{
  "evaluatorId": "user-123",
  "tutoringId": "tutoring-456",
  "score": "5",
  "comments": "Excelente tutoría"
}
```

**Ahora (v2.0):**
```json
{
  "tutoringId": "tutoring-456",
  "score": "5",
  "comments": "Excelente tutoría"
}
```

**Cambios:**
- ❌ **BREAKING CHANGE**: `evaluatorId` eliminado del body
- ✅ **Mejora de seguridad**: Se asigna automáticamente desde el contexto de usuario
- ⚠️ **Acción requerida**: Actualizar clientes para no enviar `evaluatorId`

### 🆕 Nuevos Endpoints

#### 1. Vista Detallada de Tutorías
```http
GET /api/v1/tutorings/{id}/details
```

**Respuesta:**
```json
{
  "message": "Detalle de tutoría obtenido exitosamente",
  "data": {
    "id": "tutoring-123",
    "tutor": {...},
    "tutee": {...},
    "skills": [...],
    "status": "ACTIVA",
    "objectives": "Aprender Spring Boot",
    "finalActUrl": null,
    "sessions": [
      {
        "id": "session-1",
        "datetime": "2024-01-15T10:00:00Z",
        "durationMinutes": 60,
        "sessionStatus": "REALIZADA"
      }
    ],
    "feedbacks": [
      {
        "id": "feedback-1",
        "evaluator": {...},
        "score": "5",
        "comments": "Excelente sesión"
      }
    ]
  }
}
```

#### 2. Perfil de Usuario Actual
```http
GET /api/v1/users/me
```

**Respuesta:**
```json
{
  "message": "Usuario obtenido exitosamente",
  "data": {
    "id": "user-123",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "rol": "TUTOR",
    "chapter": {...}
  }
}
```

#### 3. Filtros en Tutorías
```http
GET /api/v1/tutorings?tutorId=user-123&tuteeId=user-456
```

## Cambios en Modelos de Datos

### 🔄 Estados Actualizados

#### 1. RequestStatus (Solicitudes de Tutoría)

**Antes (v1.0):**
```java
public enum RequestStatus {
    ENVIADA,
    APROBADA,
    ASIGNADA,
    RECHAZADA
}
```

**Ahora (v2.0):**
```java
public enum RequestStatus {
    ENVIADA,
    CONVERSANDO,    // ← NUEVO
    APROBADA,
    ASIGNADA,
    RECHAZADA
}
```

#### 2. TutoringStatus (Tutorías)

**Antes (v1.0):**
```java
public enum TutoringStatus {
    ACTIVA,
    COMPLETADA,
    CANCELADA
}
```

**Ahora (v2.0):**
```java
public enum TutoringStatus {
    ACTIVA,
    COMPLETADA,
    CANCELADA,
    SOLICITUD_CANCELACION    // ← NUEVO
}
```

### 🆕 Nuevos Campos

#### 1. Modelo Tutoring
```java
public class Tutoring {
    // Campos existentes...
    
    // NUEVO en v2.0
    private String finalActUrl;
    private Date createdAt;
    private Date updatedAt;
}
```

#### 2. Modelo User (para respuestas con conteo)
```java
public class UserWithTutoringCountDto {
    // Campos existentes de UserDto...
    
    // NUEVO en v2.0
    private Integer activeTutoringCount;
}
```

## Cambios en Base de Datos

### 📊 Migraciones Requeridas

#### 1. Tabla `tutorings`
```sql
-- Agregar nuevos campos
ALTER TABLE tutorings 
ADD COLUMN final_act_url VARCHAR(500),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Actualizar registros existentes
UPDATE tutorings 
SET created_at = COALESCE(start_date, CURRENT_TIMESTAMP),
    updated_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;
```

#### 2. Actualizar Estados
```sql
-- Los nuevos estados se manejan automáticamente por JPA
-- No se requiere migración de datos existentes
```

#### 3. Índices Recomendados
```sql
-- Para filtros de usuarios
CREATE INDEX idx_users_chapter_id ON users(chapter_id);
CREATE INDEX idx_users_rol ON users(rol);
CREATE INDEX idx_users_seniority ON users(seniority);
CREATE INDEX idx_users_email ON users(email);

-- Para filtros de tutorías
CREATE INDEX idx_tutorings_tutor_id ON tutorings(tutor_id);
CREATE INDEX idx_tutorings_tutee_id ON tutorings(tutee_id);
CREATE INDEX idx_tutorings_status ON tutorings(status);

-- Para vista detallada
CREATE INDEX idx_tutoring_sessions_tutoring_id ON tutoring_sessions(tutoring_id);
CREATE INDEX idx_feedbacks_tutoring_id ON feedbacks(tutoring_id);
```

## Cambios en Configuración

### 🔧 Nuevas Propiedades

#### application.properties
```properties
# Configuración de API externa (NUEVO)
external.api.creci.base-url=https://api.creci.example.com
external.api.creci.users-endpoint=/api/v1/users

# Configuración de autenticación (NUEVO)
app.auth.header-name=Authorization
app.auth.protected-paths=/api/**

# Configuración de RestTemplate (NUEVO)
rest.template.connect-timeout=5000
rest.template.read-timeout=10000
```

#### application-dev.properties
```properties
# Habilitar integración externa en desarrollo
external.api.creci.base-url=https://dev-api.creci.example.com
external.api.creci.enabled=true
```

#### application-test.properties
```properties
# Deshabilitar integración externa en pruebas
external.api.creci.enabled=false
external.api.creci.base-url=http://localhost:8080/mock
```

## Cambios en el Código

### 🔄 Actualizaciones Requeridas

#### 1. Controladores que Usan Feedbacks

**Antes (v1.0):**
```java
@PostMapping("/feedbacks")
public ResponseEntity<FeedbackDto> createFeedback(@RequestBody CreateFeedbackDto dto) {
    // dto.getEvaluatorId() era requerido
    Feedback feedback = feedbackService.createFeedback(dto);
    return ResponseEntity.ok(feedbackMapper.toDto(feedback));
}
```

**Ahora (v2.0):**
```java
@PostMapping("/feedbacks")
public ResponseEntity<OkResponseDto<FeedbackDto>> createFeedback(@RequestBody CreateFeedbackDto dto) {
    User currentUser = UserContextHelper.getCurrentUserOrThrow();
    // dto.setEvaluatorId() se asigna automáticamente
    dto.setEvaluatorId(currentUser.getId());
    
    Feedback feedback = feedbackService.createFeedback(dto);
    return ResponseEntity.ok(OkResponseDto.of("Feedback creado exitosamente", feedbackMapper.toDto(feedback)));
}
```

#### 2. Servicios que Requieren Contexto de Usuario

**Antes (v1.0):**
```java
@Service
public class TutoringService {
    public Tutoring createTutoring(String requestId, String tutorId, String userId) {
        // userId pasado como parámetro
        // Validación manual de permisos
    }
}
```

**Ahora (v2.0):**
```java
@Service
public class TutoringService {
    public Tutoring createTutoring(String requestId, String tutorId, String objectives) {
        User currentUser = UserContextHelper.getCurrentUserOrThrow();
        
        // Validación automática de permisos
        if (!UserContextHelper.canActAsTutor()) {
            throw new ForbiddenException("No tiene permisos para crear tutorías");
        }
        
        // Usar currentUser.getId() internamente
    }
}
```

### 🆕 Nuevas Utilidades Disponibles

#### 1. UserContextHelper

```java
// Obtener usuario actual
User currentUser = UserContextHelper.getCurrentUserOrThrow();

// Validar permisos
UserContextHelper.requireAdminRole();
UserContextHelper.requireResourceAccess(resourceId);

// Verificar capacidades
boolean canTutor = UserContextHelper.canActAsTutor();
boolean isAdmin = UserContextHelper.isCurrentUserAdmin();
```

#### 2. Servicios de Consulta Especializados

```java
@Autowired
private GetFeedbacksService getFeedbacksService;

@Autowired
private GetTutoringSessionsService getTutoringSessionsService;

// Uso
List<Feedback> feedbacks = getFeedbacksService.getFeedbacksByTutoringId(tutoringId);
List<TutoringSession> sessions = getTutoringSessionsService.getSessionsByTutoringId(tutoringId);
```

## Cambios en Pruebas

### 🧪 Actualizaciones de Testing

#### 1. Pruebas con Contexto de Usuario

**Antes (v1.0):**
```java
@Test
void shouldCreateFeedback() {
    CreateFeedbackDto dto = new CreateFeedbackDto();
    dto.setEvaluatorId("user-123");
    dto.setTutoringId("tutoring-456");
    
    // Test directo
}
```

**Ahora (v2.0):**
```java
@Test
void shouldCreateFeedback() {
    // Mock del contexto de usuario
    User mockUser = createMockUser();
    when(userService.findUserByGoogleId(anyString())).thenReturn(Optional.of(mockUser));
    
    CreateFeedbackDto dto = new CreateFeedbackDto();
    // No incluir evaluatorId
    dto.setTutoringId("tutoring-456");
    
    mockMvc.perform(post("/api/v1/feedbacks")
            .header("Authorization", "google-id-123")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
}
```

#### 2. Configuración de Pruebas de Integración

```java
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public ExternalUserRepository mockExternalUserRepository() {
        return Mockito.mock(ExternalUserRepository.class);
    }
    
    @Bean
    @Primary
    public RestTemplate mockRestTemplate() {
        return Mockito.mock(RestTemplate.class);
    }
}
```

## Checklist de Migración

### ✅ Preparación

- [ ] **Backup de base de datos**
- [ ] **Revisar dependencias del cliente**
- [ ] **Preparar entorno de testing**
- [ ] **Documentar integraciones existentes**

### ✅ Base de Datos

- [ ] **Ejecutar migraciones SQL**
- [ ] **Crear índices recomendados**
- [ ] **Verificar integridad de datos**
- [ ] **Probar rollback si es necesario**

### ✅ Configuración

- [ ] **Agregar nuevas propiedades**
- [ ] **Configurar API externa**
- [ ] **Actualizar perfiles de aplicación**
- [ ] **Verificar configuración de seguridad**

### ✅ Código de Aplicación

- [ ] **Actualizar DTOs de feedback**
- [ ] **Implementar UserContextHelper**
- [ ] **Actualizar controladores**
- [ ] **Migrar servicios a nuevo contexto**

### ✅ Clientes/Frontend

- [ ] **Actualizar llamadas a API de feedback**
- [ ] **Implementar nuevos endpoints**
- [ ] **Actualizar manejo de estados**
- [ ] **Probar filtros nuevos**

### ✅ Pruebas

- [ ] **Actualizar pruebas unitarias**
- [ ] **Configurar mocks para integración externa**
- [ ] **Probar flujos de autenticación**
- [ ] **Validar nuevas funcionalidades**

### ✅ Despliegue

- [ ] **Desplegar en entorno de staging**
- [ ] **Ejecutar pruebas de integración**
- [ ] **Verificar métricas y logs**
- [ ] **Desplegar en producción**

### ✅ Post-Despliegue

- [ ] **Monitorear logs de aplicación**
- [ ] **Verificar integraciones externas**
- [ ] **Validar funcionalidades críticas**
- [ ] **Documentar issues encontrados**

## Rollback Plan

### 🔙 En Caso de Problemas

#### 1. Rollback de Aplicación
```bash
# Revertir a versión anterior
docker pull sistematutorias:v1.0
docker stop sistematutorias-v2
docker run -d --name sistematutorias-v1 sistematutorias:v1.0
```

#### 2. Rollback de Base de Datos
```sql
-- Remover campos nuevos si es necesario
ALTER TABLE tutorings 
DROP COLUMN final_act_url,
DROP COLUMN created_at,
DROP COLUMN updated_at;

-- Revertir estados a valores v1.0 si es necesario
UPDATE tutoring_requests 
SET request_status = 'ENVIADA' 
WHERE request_status = 'CONVERSANDO';
```

#### 3. Rollback de Configuración
```bash
# Restaurar configuración v1.0
cp application-v1.properties application.properties
systemctl restart sistematutorias
```

## Soporte y Contacto

### 📞 Canales de Soporte

- **Issues Técnicos**: Crear ticket en sistema de tracking
- **Preguntas de Migración**: Contactar equipo de desarrollo
- **Documentación**: Revisar `/docs` en el repositorio
- **Emergencias**: Canal de Slack #sistematutorias-support

### 📚 Recursos Adicionales

- [CHANGELOG-v2.0.md](./CHANGELOG-v2.0.md) - Lista completa de cambios
- [ARCHITECTURE-v2.0.md](./ARCHITECTURE-v2.0.md) - Documentación de arquitectura
- [UserContext-Usage-Examples.md](./UserContext-Usage-Examples.md) - Ejemplos de uso
- [openapi-complete.yaml](../openapi-complete.yaml) - Especificación completa de API

---

**Nota**: Esta guía cubre los aspectos principales de la migración. Para casos específicos o dudas técnicas, consultar la documentación detallada o contactar al equipo de desarrollo.