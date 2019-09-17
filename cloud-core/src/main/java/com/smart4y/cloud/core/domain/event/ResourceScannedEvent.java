package com.smart4y.cloud.core.domain.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 资源扫描事件
 *
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ResourceScannedEvent implements Serializable {

    private String application;
    private List<Mapping> mappings = Collections.emptyList();

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Mapping implements Serializable {

        private String apiName;
        private String apiCode;
        private String apiDesc;
        private String path;
        private String className;
        private String methodName;
        private String md5;
        private String requestMethod;
        private String serviceId;
        private String contentType;
        private Integer isAuth;
    }
}