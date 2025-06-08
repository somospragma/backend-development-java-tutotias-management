package com.pragma.sistematutorias.shared.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import com.pragma.shared.service.MessageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MessageServiceTest {

    /**
     * Tests that getMessage returns the correct message from MessageSource
     * when given a valid message code.
     */
    @Test
    public void testGetMessageReturnsCorrectMessageForValidCode() {
        // Arrange
        MessageSource messageSourceMock = mock(MessageSource.class);
        MessageService messageService = new MessageService(messageSourceMock);
        String expectedMessage = "Test Message";
        String messageCode = "test.message.code";

        when(messageSourceMock.getMessage(eq(messageCode), isNull(), eq(LocaleContextHolder.getLocale())))
            .thenReturn(expectedMessage);

        // Act
        String actualMessage = messageService.getMessage(messageCode);

        // Assert
        assertEquals(expectedMessage, actualMessage);
        verify(messageSourceMock).getMessage(eq(messageCode), isNull(), eq(LocaleContextHolder.getLocale()));
    }

    /**
     * Tests the behavior of getMessage when a non-existent message code is provided.
     * This test verifies that the method throws a NoSuchMessageException when the
     * MessageSource cannot find a message for the given code.
     */
    @Test
    public void testGetMessage_NonExistentCode_ThrowsNoSuchMessageException() {
        // Arrange
        MessageSource mockMessageSource = mock(MessageSource.class);
        MessageService messageService = new MessageService(mockMessageSource);
        String nonExistentCode = "non.existent.code";

        when(mockMessageSource.getMessage(eq(nonExistentCode), isNull(), any()))
            .thenThrow(new NoSuchMessageException(""));

        // Act & Assert
        assertThrows(NoSuchMessageException.class, () -> {
            messageService.getMessage(nonExistentCode);
        });
    }

    /**
     * Tests the behavior of getMessage when provided with a null code.
     * This test verifies that the method properly handles null input by delegating to 
     * the underlying MessageSource, which is expected to throw an IllegalArgumentException.
     */
    @SuppressWarnings("null")
    @Test
    public void testGetMessage_NullCode() {
        MessageSource mockMessageSource = mock(MessageSource.class);
        when(mockMessageSource.getMessage(null, null, LocaleContextHolder.getLocale()))
            .thenThrow(new IllegalArgumentException("Code must not be null"));

        MessageService messageService = new MessageService(mockMessageSource);

        try {
            messageService.getMessage(null);
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Code must not be null");
        }

        verify(mockMessageSource).getMessage(null, null, LocaleContextHolder.getLocale());
    }

    /**
     * Test case for getMessage method with code and arguments
     * Verifies that the method correctly delegates to messageSource and returns the expected message
     */
    @Test
    public void test_getMessage_withCodeAndArgs() {
        MessageSource mockMessageSource = Mockito.mock(MessageSource.class);
        MessageService messageService = new MessageService(mockMessageSource);

        String code = "test.code";
        Object[] args = {"arg1", "arg2"};
        String expectedMessage = "Test message";

        when(mockMessageSource.getMessage(code, args, LocaleContextHolder.getLocale())).thenReturn(expectedMessage);

        String result = messageService.getMessage(code, args);

        assertEquals(expectedMessage, result);
    }

}
