package com.smart4y.cloud.base.access.interfaces.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问控制
 *
 * @author Youtao
 * on 2020/7/30 14:57
 */
@Slf4j
@RestController
@RequestMapping(value = "/access",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class BaseAccessController {
}