package com.smart4y.cloud.spider.interfaces.rest;

import com.smart4y.cloud.core.message.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * Created by youtao on 2019-09-08.
 */
@Slf4j
@Controller
public class IndexController {

    @GetMapping("/")
    public ResultMessage<String> index() {
        return ok("ok");
    }
}