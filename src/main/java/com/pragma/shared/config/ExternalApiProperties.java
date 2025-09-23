package com.pragma.shared.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.creci-api")
public class ExternalApiProperties {
    private String serviceUrl;
    private String serviceKey;
}