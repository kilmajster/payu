package com.createam.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payu")
public class PayUConfigurationProperties {
    private String description;
    private String clientId;
    private String clientSecret;
    private String authorizationUri;
    private String merchantPosId;
    private String orderUrl;
}
