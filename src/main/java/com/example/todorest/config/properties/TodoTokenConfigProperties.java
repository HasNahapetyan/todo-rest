package com.example.todorest.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "platform2.tokens.jwt")
public class TodoTokenConfigProperties {

    private long expiration;
    private String secretKey;
}