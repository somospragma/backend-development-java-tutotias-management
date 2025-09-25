package com.pragma.usuarios.domain.port.input;

import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;

import java.util.Optional;

public interface GetExternalUserUseCase {
    Optional<PragmaUserDto> getExternalUserByEmail(String email);
}