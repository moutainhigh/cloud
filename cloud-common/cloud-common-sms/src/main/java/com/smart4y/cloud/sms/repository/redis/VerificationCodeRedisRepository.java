package com.smart4y.cloud.sms.repository.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart4y.cloud.sms.entity.VerificationCode;
import com.smart4y.cloud.sms.repository.IVerificationCodeRepository;
import com.smart4y.cloud.sms.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * 验证码redis储存实现
 */
@Slf4j
@Repository
@EnableConfigurationProperties(RedisProperties.class)
public class VerificationCodeRedisRepository implements IVerificationCodeRepository {

    private final RedisProperties properties;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public VerificationCodeRedisRepository(RedisProperties properties, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public VerificationCode findOne(String phone, String identificationCode) {
        String key = key(phone, identificationCode);
        String data = redisTemplate.opsForValue().get(key);

        if (StringUtils.isBlank(data)) {
            log.debug("json data is empty for key: {}", key);
            return null;
        }

        try {
            return objectMapper.readValue(data, VerificationCode.class);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void save(VerificationCode verificationCode) {
        String key = key(verificationCode.getPhone(), verificationCode.getIdentificationCode());

        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        LocalDateTime expirationTime = verificationCode.getExpirationTime();

        String value;

        try {
            value = objectMapper.writeValueAsString(verificationCode);
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        if (expirationTime == null) {
            operations.set(key, value);
        } else {
            long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            long end = expirationTime.toEpochSecond(ZoneOffset.UTC);
            long timeout = end - now;

            operations.set(key, value, timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void delete(String phone, String identificationCode) {
        redisTemplate.delete(key(phone, identificationCode));
    }

    private String key(String phone, String identificationCode) {
        String keyPrefix = StringUtils.trimToNull(properties.getKeyPrefix());
        String tempIdentificationCode = StringUtils.trimToNull(identificationCode);

        StringBuilder keyBuilder = new StringBuilder();

        if (keyPrefix != null) {
            keyBuilder.append(keyPrefix);
            keyBuilder.append("_");
        }

        keyBuilder.append(StringUtils.trimToNull(phone));

        if (tempIdentificationCode != null) {
            keyBuilder.append("_");
            keyBuilder.append(tempIdentificationCode);
        }

        return keyBuilder.toString();
    }

}
