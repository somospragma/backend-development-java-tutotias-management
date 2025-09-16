package com.pragma.tutorings.application.service;

import com.pragma.skills.domain.model.Skill;
import com.pragma.skills.domain.port.input.FindSkillUseCase;
import com.pragma.tutorings.domain.model.Tutoring;
import com.pragma.tutorings.domain.model.enums.TutoringStatus;
import com.pragma.tutorings.domain.port.input.CreateTutoringUseCase;
import com.pragma.tutorings.domain.port.output.TutoringRepository;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.domain.port.output.TutoringRequestRepository;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.usuarios.domain.port.input.FindUserByIdUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutoringService implements CreateTutoringUseCase {

    private final TutoringRepository tutoringRepository;
    private final TutoringRequestRepository tutoringRequestRepository;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindSkillUseCase findSkillUseCase;

    @Override
    public Tutoring createTutoring(String tutoringRequestId, String tutorId, String objectives) {
        log.info("Iniciando creación de tutoría con solicitud ID: {} y tutor ID: {}", tutoringRequestId, tutorId);
        
        // Validar que el tutor existe y tiene el rol adecuado
        User tutor = validateTutor(tutorId);
        
        // Validar que la solicitud de tutoría existe y está en estado Aprobada
        TutoringRequest tutoringRequest = validateTutoringRequest(tutoringRequestId);
        
        // Validar que el tutor no ha excedido su límite de tutorías activas
        validateTutorLimit(tutorId, tutor.getActiveTutoringLimit());
        
        // Crear la tutoría con los datos de la solicitud
        Tutoring tutoring = new Tutoring();
        tutoring.setTutor(tutor);
        tutoring.setTutee(tutoringRequest.getTutee());
        tutoring.setStartDate(new Date());
        tutoring.setExpectedEndDate(calculateExpectedEndDate());
        tutoring.setStatus(TutoringStatus.Activa);
        tutoring.setObjectives(objectives);
        
        // Copiar las habilidades de la solicitud
        List<Skill> skills = new ArrayList<>(tutoringRequest.getSkills());
        tutoring.setSkills(skills);
        
        // Guardar la tutoría
        Tutoring savedTutoring = tutoringRepository.save(tutoring);
        log.info("Tutoría creada exitosamente con ID: {}", savedTutoring.getId());
        
        // Actualizar el estado de la solicitud a Asignada y asignar el ID de la tutoría
        tutoringRequest.setRequestStatus(RequestStatus.Asignada);
        tutoringRequest.setAssignedTutoringId(savedTutoring.getId());
        tutoringRequestRepository.save(tutoringRequest);
        log.info("Solicitud de tutoría actualizada a estado Asignada con tutoría ID: {}", savedTutoring.getId());
        
        return savedTutoring;
    }
    
    private User validateTutor(String tutorId) {
        Optional<User> tutorOpt = findUserByIdUseCase.findUserById(tutorId);
        
        if (tutorOpt.isEmpty()) {
            log.error("El tutor con ID: {} no existe", tutorId);
            throw new IllegalArgumentException("El tutor no existe");
        }
        
        User tutor = tutorOpt.get();
        if (tutor.getRol() != RolUsuario.Tutor && tutor.getRol() != RolUsuario.Administrador) {
            log.error("El usuario con ID: {} no tiene rol de Tutor o Administrador", tutorId);
            throw new IllegalArgumentException("Solo los usuarios con rol de Tutor o Administrador pueden crear tutorías");
        }
        
        return tutor;
    }
    
    private TutoringRequest validateTutoringRequest(String tutoringRequestId) {
        Optional<TutoringRequest> requestOpt = tutoringRequestRepository.findById(tutoringRequestId);
        
        if (requestOpt.isEmpty()) {
            log.error("La solicitud de tutoría con ID: {} no existe", tutoringRequestId);
            throw new IllegalArgumentException("La solicitud de tutoría no existe");
        }
        
        TutoringRequest request = requestOpt.get();
        if (request.getRequestStatus() != RequestStatus.Conversando) {
            log.error("La solicitud de tutoría con ID: {} no está en estado Aprobada", tutoringRequestId);
            throw new IllegalArgumentException("Solo se pueden crear tutorías a partir de solicitudes en estado Aprobada");
        }
        
        return request;
    }
    
    private void validateTutorLimit(String tutorId, int limit) {
        Long activeTutorings = tutoringRepository.countActiveTutoringByTutorId(tutorId);

        if (activeTutorings >= limit) {
            log.error("El tutor con ID: {} ha excedido su límite de tutorías activas: {}", tutorId, limit);
            throw new IllegalStateException("El tutor ha excedido su límite de tutorías activas");
        }
    }
    
    private Date calculateExpectedEndDate() {
        // Por defecto, establecer la fecha esperada de finalización a 3 meses después
        Date now = new Date();
        return new Date(now.getTime() + (90L * 24 * 60 * 60 * 1000)); // 90 días en milisegundos
    }
}