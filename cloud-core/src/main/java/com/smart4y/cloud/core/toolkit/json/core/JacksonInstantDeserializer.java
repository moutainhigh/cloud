package com.smart4y.cloud.core.toolkit.json.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;

/**
 * {@link Instant#toEpochMilli()} 转为 {@link Instant#ofEpochMilli(long)}
 *
 * @author Youtao
 *         Created by youtao on 2018/8/7.
 */
public class JacksonInstantDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return Instant.ofEpochMilli(parser.getValueAsLong());
    }
}