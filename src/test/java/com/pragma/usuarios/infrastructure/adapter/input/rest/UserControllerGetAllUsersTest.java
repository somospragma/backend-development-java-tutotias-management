package com.pragma.usuarios.infrastructure.adapter.input.rest;

import com.pragma.usuarios.domain.port.input.GetAllUsersWithTutoringCountUseCase;
import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserWithTutoringCountDto;
import com.pragma.chapter.infrastructure.adapter.input.rest.dto.ChapterDto;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import com.pragma.shared.context.UserContext;
import com.pragma.usuarios.domain.model.User;
import com.pragma.chapter.domain.model.Chapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerGetAllUsersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAllUsersWithTutoringCountUseCase getAllUsersWithTutoringCountUseCase;

    @BeforeEach
    void setUp() {
        // Mock admin user in context
        User adminUser = new User();
        adminUser.setId("admin-id");
        adminUser.setEmail("admin@test.com");
        adminUser.setRol(RolUsuario.ADMINISTRADOR);
        UserContext.setCurrentUser(adminUser);
    }

    @Test
    void getAllUsersWithTutoringCount_WithAdminUser_ShouldReturnUsersList() throws Exception {
        // Arrange
        ChapterDto chapterDto = new ChapterDto("chapter-1", "Engineering");
        
        UserWithTutoringCountDto user1 = new UserWithTutoringCountDto();
        user1.setId("user-1");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john@test.com");
        user1.setRol(RolUsuario.TUTOR);
        user1.setChapter(chapterDto);
        user1.setTutoringsAsTutor(3L);
        user1.setTutoringsAsTutee(1L);

        UserWithTutoringCountDto user2 = new UserWithTutoringCountDto();
        user2.setId("user-2");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane@test.com");
        user2.setRol(RolUsuario.TUTORADO);
        user2.setChapter(chapterDto);
        user2.setTutoringsAsTutor(0L);
        user2.setTutoringsAsTutee(2L);

        List<UserWithTutoringCountDto> users = Arrays.asList(user1, user2);
        when(getAllUsersWithTutoringCountUseCase.getAllUsersWithTutoringCount()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .header("Authorization", "admin-google-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("user-1"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john@test.com"))
                .andExpect(jsonPath("$[0].rol").value("TUTOR"))
                .andExpect(jsonPath("$[0].tutoringsAsTutor").value(3))
                .andExpect(jsonPath("$[0].tutoringsAsTutee").value(1))
                .andExpect(jsonPath("$[1].id").value("user-2"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].email").value("jane@test.com"))
                .andExpect(jsonPath("$[1].rol").value("TUTORADO"))
                .andExpect(jsonPath("$[1].tutoringsAsTutor").value(0))
                .andExpect(jsonPath("$[1].tutoringsAsTutee").value(2));
    }
}