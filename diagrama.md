```mermaid
classDiagram
    direction LR

    class Chapter {
        +String idCharper
        +String nombre
    }
    
    class Usuario {
        +String idUsuario
        +String nombre
        +String apellido
        +String email
        +String departamento
        +String cargo
        +String rol // Tutor, Tutorado, Administrador, Gerente
        +Int limiteTutoriasActivas // Nuevo: Límite de tutorías activas para un tutor
    }

    class Habilidad {
        +String idHabilidad
        +String nombreHabilidad
        +String categoria
        +String nivelExpertise
    }

    class Tutoria {
        +String idTutoria
        +Date fechaInicio
        +Date fechaFinEsperada
        +String estado // Activa, Completada, Cancelada, etc.
        +String objetivos
    }

    class SesionTutoria {
        +String idSesion
        +Date fechaHora
        +Int duracionMinutos
        +String modalidad // Virtual, Presencial
        +String lugarEnlace
        +String temasCubiertos
        +String notas
        +String estadoSesion // Programada, Realizada, Cancelada
    }

    class DisponibilidadTutor {
        +String idDisponibilidad
        +String diaSemana
        +Time horaInicio
        +Time horaFin
    }

    class SolicitudTutoria {
        +String idSolicitud
        +String descripcionNecesidad
        +Date fechaSolicitud
        +String estadoSolicitud // Enviada, Aprobada, Asignada, Rechazada
    }

    class Evaluacion {
        +String idEvaluacion
        +Int puntuacion
        +String comentarios
        +Date fechaEvaluacion
    }

    class Notificacion {
        +String idNotificacion
        +String tipoNotificacion
        +String contenidoMensaje
        +Date fechaEnvio
        +String estadoNotificacion // Enviada, Leída
    }

    Usuario "1" -- "0..N" Habilidad : tiene_habilidad/interesa
    Usuario "1" -- "0..N" DisponibilidadTutor : define
    Usuario "1" -- "0..N" SolicitudTutoria : envia
    Usuario "1" -- "0..N" Evaluacion : da_feedback

    Tutoria "1" -- "1" Usuario : (Tutor) es_dada_por
    Tutoria "1" -- "1" Usuario : (Tutorado) es_recibida_por
    Tutoria "1" -- "1" Habilidad : se_enfoca_en
    Tutoria "1" -- "0..N" SesionTutoria : incluye
    Tutoria "1" -- "0..N" Evaluacion : es_objeto_de

    SolicitudTutoria "1" -- "1" Habilidad : solicita_habilidad
    SolicitudTutoria "0..1" -- "1" Tutoria : resulta_en

    Usuario "1" -- "0..N" Notificacion : recibe

    note for Usuario "Un Usuario con rol 'Tutor' puede ser 'Tutorado' en diferentes tutorías.
    Además, un Tutor no puede tener más de 'limiteTutoriasActivas' tutorías activas."
