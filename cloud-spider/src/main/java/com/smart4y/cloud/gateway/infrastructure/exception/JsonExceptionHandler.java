package com.smart4y.cloud.gateway.infrastructure.exception;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.exception.global.ExceptionHelper;
import com.smart4y.cloud.gateway.application.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 统一异常处理
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class JsonExceptionHandler implements ErrorWebExceptionHandler {

    private final AccessLogService accessLogService;
    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();
    /**
     * 存储处理异常后的信息
     */
    private final ThreadLocal<ResultMessage<Void>> exceptionHandlerResult = new ThreadLocal<>();

    public JsonExceptionHandler(AccessLogService accessLogService) {
        this.accessLogService = accessLogService;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    @NonNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable ex) {
        /*
         * 按照异常类型进行处理
         */
        ResultMessage<Void> resultEntity;
        if ("/favicon.ico".equals(exchange.getRequest().getURI().getPath())) {
            return Mono.empty();
        }
        if (ex instanceof NotFoundException) {
            ex = new OpenAlertException(MessageType.SERVICE_UNAVAILABLE);
        }
        resultEntity = ExceptionHelper.resolveException((Exception) ex);
        /*
         * 参考AbstractErrorWebExceptionHandler
         */
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        exceptionHandlerResult.set(resultEntity);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        Throwable finalEx = ex;
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response, finalEx));
    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        ResultMessage<Void> result = exceptionHandlerResult.get();
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(result));
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response, Throwable ex) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        // 保存日志
        accessLogService.sendLog(exchange, (Exception) ex);
        return response.writeTo(exchange, new ResponseContext());
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {

        @NonNull
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return JsonExceptionHandler.this.messageWriters;
        }

        @NonNull
        @Override
        public List<ViewResolver> viewResolvers() {
            return JsonExceptionHandler.this.viewResolvers;
        }
    }
}