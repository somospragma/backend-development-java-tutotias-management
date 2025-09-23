package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.infrastructure.adapter.input.rest.dto.UserWithTutoringCountDto;
import java.util.List;

public interface GetAllUsersWithTutoringCountUseCase {
    List<UserWithTutoringCountDto> getAllUsersWithTutoringCount();
}