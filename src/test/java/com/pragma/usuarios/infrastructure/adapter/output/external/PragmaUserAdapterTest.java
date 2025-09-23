package com.pragma.usuarios.infrastructure.adapter.output.external;

import com.pragma.shared.config.ExternalApiProperties;
import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PragmaUserAdapterTest {

    @Mock
    private ExternalApiProperties apiProperties;

    @Mock
    private RestTemplate restTemplate;

    private PragmaUserAdapter pragmaUserAdapter;

    @BeforeEach
    void setUp() {
        pragmaUserAdapter = new PragmaUserAdapter(apiProperties, restTemplate);
    }

    @Test
    void findUserByEmail_shouldReturnUser_whenApiReturnsSuccessfulResponse() {
        // Arrange
        String email = "test@pragma.com";
        String serviceUrl = "https://api.pragma.com";
        String serviceKey = "test-key";
        
        when(apiProperties.getServiceUrl()).thenReturn(serviceUrl);
        when(apiProperties.getServiceKey()).thenReturn(serviceKey);
        
        PragmaUserDto expectedUser = new PragmaUserDto();
        expectedUser.setEmail(email);
        expectedUser.setFullName("Test User");
        expectedUser.setPragmaticId(123);
        
        ResponseEntity<PragmaUserDto> response = new ResponseEntity<>(expectedUser, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(PragmaUserDto.class)))
                .thenReturn(response);

        // Act
        Optional<PragmaUserDto> result = pragmaUserAdapter.findUserByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        assertEquals("Test User", result.get().getFullName());
        assertEquals(123, result.get().getPragmaticId());
        
        verify(restTemplate).exchange(
                eq(serviceUrl + "/prod/administration/pragmatic/" + email),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(PragmaUserDto.class)
        );
    }

    @Test
    void findUserByEmail_shouldReturnEmpty_whenApiReturnsNotFound() {
        // Arrange
        String email = "notfound@pragma.com";
        when(apiProperties.getServiceUrl()).thenReturn("https://api.pragma.com");
        when(apiProperties.getServiceKey()).thenReturn("test-key");
        
        ResponseEntity<PragmaUserDto> response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(PragmaUserDto.class)))
                .thenReturn(response);

        // Act
        Optional<PragmaUserDto> result = pragmaUserAdapter.findUserByEmail(email);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findUserByEmail_shouldReturnEmpty_whenExceptionOccurs() {
        // Arrange
        String email = "error@pragma.com";
        when(apiProperties.getServiceUrl()).thenReturn("https://api.pragma.com");
        when(apiProperties.getServiceKey()).thenReturn("test-key");
        
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(PragmaUserDto.class)))
                .thenThrow(new RuntimeException("Connection error"));

        // Act
        Optional<PragmaUserDto> result = pragmaUserAdapter.findUserByEmail(email);

        // Assert
        assertFalse(result.isPresent());
    }
}