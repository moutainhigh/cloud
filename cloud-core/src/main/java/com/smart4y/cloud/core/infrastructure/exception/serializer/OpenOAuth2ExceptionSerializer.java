package com.smart4y.cloud.core.infrastructure.exception.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.smart4y.cloud.core.infrastructure.exception.OpenOAuth2Exception;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * 自定义Oauth2异常提示序列化
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class OpenOAuth2ExceptionSerializer extends StdSerializer<OpenOAuth2Exception> {

    public OpenOAuth2ExceptionSerializer() {
        super(OpenOAuth2Exception.class);
    }

    @Override
    public void serialize(OpenOAuth2Exception ex, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("message", ex.getMessage());
        gen.writeStringField("data", "");
        gen.writeNumberField("timestamp", Instant.now().toEpochMilli());
        if (ex.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : ex.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                if ("code".equals(key)) {
                    gen.writeNumberField(key, new BigDecimal(add));
                } else {
                    gen.writeStringField(key, add);
                }
            }
        }
        gen.writeEndObject();
    }
}