package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserWithTutoringCountDto;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import java.util.List;

public interface GetAllUsersWithTutoringCountUseCase {
    List<UserWithTutoringCountDto> getAllUsersWithTutoringCount();
    List<UserWithTutoringCountDto> getAllUsersWithTutoringCountFiltered(String chapterId, String rol, Integer seniority, String email);
}