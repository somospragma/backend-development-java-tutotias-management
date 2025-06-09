package com.pragma.tutorings_requests.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.tutorings_requests.domain.model.TutoringRequest;
import com.pragma.tutorings_requests.domain.model.enums.RequestStatus;
import com.pragma.tutorings_requests.domain.port.input.CreateTutoringRequestUseCase;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.CreateTutoringRequestDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.dto.TutoringRequestDto;
import com.pragma.tutorings_requests.infrastructure.adapter.input.rest.mapper.TutoringRequestDtoMapper;
import com.pragma.usuarios.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TutoringRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateTutoringRequestUseCase createTutoringRequestUseCase;

    @Mock
    private TutoringRequestDtoMapper tutoringRequestDtoMapper;

    @InjectMocks
    private TutoringRequestController tutoringRequestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tutoringRequestController).build();
    }

    @Test
    void createTutoringRequest_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        CreateTutoringRequestDto requestDto = new CreateTutoringRequestDto();
        requestDto.setTuteeId("user-id");
        requestDto.setNeedsDescription("Necesito ayuda con Spring Boot");
        requestDto.setSkillIds(List.of("skill-1", "skill-2"));

        TutoringRequest domainModel = new TutoringRequest();
        domainModel.setId("request-id");
        domainModel.setNeedsDescription("Necesito ayuda con Spring Boot");
        
        User user = new User();
        user.setId("user-id");
        user.setFirstName("Juan");
        user.setLastName("Pérez");
        domainModel.setTutee(user);
        
        domainModel.setRequestDate(new Date());
        domainModel.setRequestStatus(RequestStatus.Enviada);

        TutoringRequestDto responseDto = new TutoringRequestDto();
        responseDto.setTuteeId("user-id");
        responseDto.setNeedsDescription("Necesito ayuda con Spring Boot");

        when(tutoringRequestDtoMapper.toModel(any(CreateTutoringRequestDto.class))).thenReturn(domainModel);
        when(createTutoringRequestUseCase.createTutoringRequest(any(TutoringRequest.class))).thenReturn(domainModel);
        when(tutoringRequestDtoMapper.toDto(any(TutoringRequest.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tutoring-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Solicitud de tutoría creada exitosamente"))
                .andExpect(jsonPath("$.data.id").value("request-id"))
                .andExpect(jsonPath("$.data.tuteeId").value("user-id"))
                .andExpect(jsonPath("$.data.tuteeName").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.needsDescription").value("Necesito ayuda con Spring Boot"))
                .andExpect(jsonPath("$.data.requestStatus").value("Enviada"));
    }
}