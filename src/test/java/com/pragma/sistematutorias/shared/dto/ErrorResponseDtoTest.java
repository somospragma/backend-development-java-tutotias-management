package com.pragma.sistematutorias.shared.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ErrorResponseDtoTest {

    /**
     * Test case for ErrorResponseDto constructor with a single String parameter.
     * Verifies that the constructor correctly sets the message and initializes the timestamp.
     */
    @Test
    public void testErrorResponseDtoConstructorWithMessage() {
        String testMessage = "Test error message";
        ErrorResponseDto errorResponse = new ErrorResponseDto(testMessage);

        assertNotNull(errorResponse);
        assertEquals(testMessage, errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
        assertTrue(errorResponse.getTimestamp().isBefore(LocalDateTime.now()) || 
                   errorResponse.getTimestamp().isEqual(LocalDateTime.now()));
        assertNull(errorResponse.getErrors());
    }

    /**
     * Tests the constructor of ErrorResponseDto that takes a message and a map of errors.
     * Verifies that the constructor correctly initializes the message, timestamp, and errors fields.
     */
    @Test
    public void testErrorResponseDtoConstructorWithMessageAndErrors() {
        String message = "Test error message";
        Map<String, String> errors = new HashMap<>();
        errors.put("field1", "Error 1");
        errors.put("field2", "Error 2");

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(message, errors);

        assertNotNull(errorResponseDto);
        assertEquals(message, errorResponseDto.getMessage());
        assertNotNull(errorResponseDto.getTimestamp());
        assertTrue(errorResponseDto.getTimestamp().isBefore(LocalDateTime.now()) || 
                   errorResponseDto.getTimestamp().isEqual(LocalDateTime.now()));
        assertEquals(errors, errorResponseDto.getErrors());
    }

    /**
     * Tests the ErrorResponseDto constructor with a valid message and null errors map.
     * This test verifies that the constructor handles a null errors map without throwing an exception,
     * and that it correctly sets the message and timestamp.
     */
    @Test
    public void testErrorResponseDtoWithNullErrors() {
        String message = "Test error message";

        ErrorResponseDto dto = new ErrorResponseDto(message, null);

        assertEquals(message, dto.getMessage());
        assertNotNull(dto.getTimestamp());
        assertNull(dto.getErrors());
    }

    /**
     * Tests the ErrorResponseDto constructor with a null message.
     * This verifies that the constructor handles null input gracefully.
     */
    @Test
    public void testErrorResponseDtoWithNullMessage() {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(null);
        assertNull(errorResponseDto.getMessage(), "Message should be null");
        assertNotNull(errorResponseDto.getTimestamp(), "Timestamp should not be null");
        assertTrue(errorResponseDto.getTimestamp().isBefore(LocalDateTime.now()) || 
                   errorResponseDto.getTimestamp().isEqual(LocalDateTime.now()),
                   "Timestamp should be current or in the past");
    }

    /**
     * Tests the ErrorResponseDto constructor with null message and valid errors map.
     * This test verifies that the constructor handles a null message without throwing an exception,
     * and that it correctly sets the timestamp and errors.
     */
    @Test
    public void testErrorResponseDtoWithNullMessage_2() {
        Map<String, String> errors = new HashMap<>();
        errors.put("field1", "error1");

        ErrorResponseDto dto = new ErrorResponseDto(null, errors);

        assertNull(dto.getMessage());
        assertNotNull(dto.getTimestamp());
        assertEquals(errors, dto.getErrors());
    }

}
