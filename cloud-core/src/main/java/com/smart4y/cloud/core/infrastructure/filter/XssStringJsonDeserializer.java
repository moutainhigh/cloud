package com.smart4y.cloud.core.infrastructure.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

/**
 *  * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String source = jsonParser.getText().trim();
        //  富文本解码
        return StringEscapeUtils.unescapeHtml4(source);
    }

}
