package com.smart4y.cloud.core.infrastructure.toolkit.json.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

/**
 * Instant 转为 {@link Instant#toEpochMilli()}
 *
 * @author Youtao
 *         Created by youtao on 2018/8/7.
 */
public class JacksonInstantSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (null == value) {
            gen.writeNumber("null");
        } else {
            gen.writeNumber(value.toEpochMilli());
        }
    }
}