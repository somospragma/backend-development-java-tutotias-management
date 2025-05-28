package com.pragma.sistematutorias.shared.dto;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OkResponseDtoTest {

    /**
     * Test the 'of' method with null data.
     * This test verifies that the method can handle null data input, which is
     * another potential edge case. We expect the method to create an OkResponseDto
     * object with null data without throwing an exception.
     */
    @Test
    public void testOfWithNullData() {
        String message = "Test Message";
        String nullData = null;

        OkResponseDto<String> response = OkResponseDto.of(message, nullData);

        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    /**
     * Test the 'of' method with a null message.
     * This tests how the method handles a null message input, which is an edge case
     * that could occur in practice. We expect the method to still create an
     * OkResponseDto object without throwing an exception, demonstrating that
     * it implicitly allows null messages.
     */
    @Test
    public void testOfWithNullMessage() {
        String nullMessage = null;
        String data = "Test Data";

        OkResponseDto<String> response = OkResponseDto.of(nullMessage, data);

        assertNotNull(response);
        assertNull(response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
    }

    /**
     * Test case for OkResponseDto.of() method
     * Verifies that the method correctly creates an OkResponseDto object
     * with the given message, data, and a current timestamp
     */
    @Test
    public void test_of_creates_valid_response_with_correct_data() {
        String message = "Success";
        String data = "Test Data";

        OkResponseDto<String> response = OkResponseDto.of(message, data);

        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(response.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
    }


    /**
     * Tests that the of() method correctly creates an OkResponseDto instance
     * with the provided message and data, and a current timestamp.
     */
    @Test
    public void testOfMethodWithMessageAndData() {
        String message = "Test message";
        String data = "Test data";

        OkResponseDto<String> response = OkResponseDto.of(message, data);

        assertNotNull(response);
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(response.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    /**
     * Tests the behavior of the 'of' method when passed an empty string message.
     * This verifies that the method accepts an empty string as a valid input.
     */
    @Test
    public void testOfWithEmptyMessage() {
        OkResponseDto<Object> response = OkResponseDto.of("");
        assertEquals("", response.getMessage(), "Message should be an empty string");
        assertNull(response.getData(), "Data should be null");
        assertNotNull(response.getTimestamp(), "Timestamp should not be null");
    }

    /**
     * Test the of(String message) method of OkResponseDto
     * Verifies that the method returns a new OkResponseDto with the given message,
     * null data, and a timestamp set to the current time.
     */
    @Test
    public void testOfWithMessageOnly() {
        String testMessage = "Test message";
        OkResponseDto<Object> response = OkResponseDto.of(testMessage);

        assertNotNull(response);
        assertEquals(testMessage, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(response.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    /**
     * Tests the behavior of the 'of' method when passed a null message.
     * This verifies that the method handles null input gracefully without throwing an exception.
     */
    @Test
    public void testOfWithNullMessage_2() {
        OkResponseDto<Object> response = OkResponseDto.of(null);
        assertNull(response.getMessage(), "Message should be null");
        assertNull(response.getData(), "Data should be null");
        assertNotNull(response.getTimestamp(), "Timestamp should not be null");
    }

    /**
     * Tests the behavior of OkResponseDto.of() method when provided with null parameters.
     * This test verifies that the method handles null inputs gracefully without throwing exceptions.
     */
    @Test
    public void testOfWithNullParameters() {
        OkResponseDto<Object> response = OkResponseDto.of(null, null);
        assertNotNull(response);
        assertNull(response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }
}
