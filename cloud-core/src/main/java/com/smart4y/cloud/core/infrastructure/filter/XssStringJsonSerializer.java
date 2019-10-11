package com.smart4y.cloud.core.infrastructure.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtil;

import java.io.IOException;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            String encodedValue = StringUtil.stripXss(value).trim();
            jsonGenerator.writeString(encodedValue);
        }
    }
}