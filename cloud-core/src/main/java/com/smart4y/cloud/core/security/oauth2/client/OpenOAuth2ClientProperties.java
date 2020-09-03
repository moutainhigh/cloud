package com.smart4y.cloud.core.security.oauth2.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@ConfigurationProperties(prefix = "cloud.client")
public class OpenOAuth2ClientProperties {

    private Map<String, OpenOAuth2ClientDetails> oauth2;
}