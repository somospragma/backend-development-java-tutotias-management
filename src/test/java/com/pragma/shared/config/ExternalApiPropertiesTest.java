package com.pragma.shared.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "app.external-api.service-url=https://test.pragma.com",
    "app.external-api.service-key=test-key-123"
})
class ExternalApiPropertiesTest {

    @Autowired
    private ExternalApiProperties externalApiProperties;

    @Test
    void shouldLoadPropertiesCorrectly() {
        assertEquals("https://test.pragma.com", externalApiProperties.getServiceUrl());
        assertEquals("test-key-123", externalApiProperties.getServiceKey());
    }
}