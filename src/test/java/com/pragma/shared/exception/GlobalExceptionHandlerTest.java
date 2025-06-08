package com.pragma.sistematutorias.shared.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.pragma.shared.dto.ErrorResponseDto;
import com.pragma.shared.exception.GlobalExceptionHandler;
import com.pragma.shared.service.MessageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GlobalExceptionHandlerTest {

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentNotValidException ex;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MessageService messageService;

    /**
     * Tests that handleGenericException method returns a ResponseEntity with
     * INTERNAL_SERVER_ERROR status and an ErrorResponseDto containing the
     * appropriate error message when a generic Exception is thrown.
     */
    @SuppressWarnings("null")
    @Test
    public void testHandleGenericExceptionReturnsInternalServerError() {
        Exception testException = new Exception("Test error message");
        String expectedErrorMessage = "An internal error occurred";

        when(messageService.getMessage("general.error", testException.getMessage()))
            .thenReturn(expectedErrorMessage);

        ResponseEntity<ErrorResponseDto> response = globalExceptionHandler.handleGenericException(testException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedErrorMessage, response.getBody().getMessage());
    }

    /**
     * Tests the handleGenericException method with a generic Exception.
     * Verifies that the method returns a ResponseEntity with INTERNAL_SERVER_ERROR status
     * and an ErrorResponseDto containing the error message from MessageService.
     */
    @SuppressWarnings("null")
    @Test
    public void test_handleGenericException_withGenericException() {
        // Arrange
        MessageService messageService = mock(MessageService.class);
        GlobalExceptionHandler handler = new GlobalExceptionHandler(messageService);
        Exception testException = new Exception("Test exception message");
        String expectedErrorMessage = "Generic error occurred";
        when(messageService.getMessage("general.error", testException.getMessage())).thenReturn(expectedErrorMessage);

        // Act
        ResponseEntity<ErrorResponseDto> response = handler.handleGenericException(testException);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedErrorMessage, response.getBody().getMessage());
        verify(messageService).getMessage("general.error", testException.getMessage());
    }

    /**
     * Tests that the handleRuntimeException method correctly handles a RuntimeException
     * by returning a ResponseEntity with BAD_REQUEST status and an ErrorResponseDto
     * containing the exception message.
     */
    @SuppressWarnings("null")
    @Test
    public void test_handleRuntimeException_returnsCorrectResponseEntity() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler(null);
        String errorMessage = "Test runtime exception";
        RuntimeException ex = new RuntimeException(errorMessage);

        ResponseEntity<ErrorResponseDto> response = handler.handleRuntimeException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
    }

    /**
     * Tests that handleRuntimeException method returns a ResponseEntity with 
     * BAD_REQUEST status and an ErrorResponseDto containing the exception message.
     */
    @SuppressWarnings("null")
    @Test
    public void test_handleRuntimeException_returnsCorrectResponseEntity_2() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler(null);
        String errorMessage = "Test runtime exception";
        RuntimeException ex = new RuntimeException(errorMessage);

        ResponseEntity<ErrorResponseDto> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
    }

    /**
     * Tests the handleValidationExceptions method with an empty list of errors.
     * This edge case verifies that the method correctly handles a MethodArgumentNotValidException
     * that contains no actual field errors.
     */
    @Test
    public void test_handleValidationExceptions_emptyErrorList() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());
        when(messageService.getMessage("general.validation.failed")).thenReturn("Validation failed");

        ErrorResponseDto response = globalExceptionHandler.handleValidationExceptions(ex);

        assertNotNull(response);
        assertEquals("Validation failed", response.getMessage());
        assertTrue(response.getErrors().isEmpty());
    }

    /**
     * Tests the handleValidationExceptions method with multiple field errors.
     * This edge case verifies that the method correctly processes and returns
     * multiple field errors in the response.
     */
    @Test
    public void test_handleValidationExceptions_multipleErrors() {
        String messaginError = "La validación ha fallado.";
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field1", "Error 1"));
        fieldErrors.add(new FieldError("object", "field2", "Error 2"));

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));
        when(messageService.getMessage("general.validation.failed")).thenReturn(messaginError);

        ErrorResponseDto response = globalExceptionHandler.handleValidationExceptions(ex);

        assertNotNull(response);
        assertEquals(messaginError, response.getMessage());
        assertEquals(2, response.getErrors().size());
        assertEquals("Error 1", response.getErrors().get("field1"));
        assertEquals("Error 2", response.getErrors().get("field2"));
    }

    /**
     * Test case for handleValidationExceptions method
     * Verifies that the method correctly processes MethodArgumentNotValidException
     * and returns an ErrorResponseDto with appropriate error messages
     */
    @Test
    public void test_handleValidationExceptions_returnsErrorResponseDto() {
        String messaginError = "La validación ha fallado.";

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "field1", "Error message 1"));
        fieldErrors.add(new FieldError("objectName", "field2", "Error message 2"));

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));
        when(messageService.getMessage("general.validation.failed")).thenReturn(messaginError);

        ErrorResponseDto result = globalExceptionHandler.handleValidationExceptions(ex);

        assertNotNull(result);
        assertEquals(messaginError, result.getMessage());

        Map<String, String> errors = result.getErrors();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Error message 1", errors.get("field1"));
        assertEquals("Error message 2", errors.get("field2"));

        verify(messageService).getMessage("general.validation.failed");
        verify(ex).getBindingResult();
        verify(bindingResult).getAllErrors();
    }

}
