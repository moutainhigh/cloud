package com.smart4y.cloud.core.configuration;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringFormEncoder;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * 处理多个文件上传
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class FeignSpringFormEncoder extends SpringFormEncoder {

    FeignSpringFormEncoder(Encoder delegate) {
        super(delegate);
        MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(ContentType.MULTIPART);
        processor.addWriter(new SpringSingleMultipartFileWriter());
        processor.addWriter(new SpringManyMultipartFilesWriter());
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (bodyType != null && bodyType.equals(MultipartFile[].class) && object != null) {
            MultipartFile[] file = (MultipartFile[]) object;
            if (file.length == 0) {
                return;
            }
            Map data = Collections.singletonMap(file[0].getName(), object);
            super.encode(data, MAP_STRING_WILDCARD, template);
            return;
        }
        if (bodyType != null) {
            super.encode(object, bodyType, template);
        }
    }
}