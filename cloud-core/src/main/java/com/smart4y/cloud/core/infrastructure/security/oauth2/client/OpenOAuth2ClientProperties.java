package com.smart4y.cloud.core.infrastructure.security.oauth2.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "opencloud.client")
public class OpenOAuth2ClientProperties {

    private Map<String, OpenOAuth2ClientDetails> oauth2;
}