package com.pragma.usuarios.domain.port.output;

import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;

import java.util.Optional;

public interface ExternalUserRepository {
    Optional<PragmaUserDto> findUserByEmail(String email);
}