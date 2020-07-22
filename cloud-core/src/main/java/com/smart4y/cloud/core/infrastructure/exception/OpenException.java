package com.smart4y.cloud.core.infrastructure.exception;

import com.smart4y.cloud.core.domain.IMessageType;
import com.smart4y.cloud.core.infrastructure.exception.context.MessageLocaleContextHolder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 异常基类
 * <p>
 * 自定义异常需要继承此类
 * </p>
 *
 * @author Youtao on 2020/5/27 17:42
 */
public abstract class OpenException extends RuntimeException {

    /**
     * 错误码
     */
    @Getter
    @Setter
    private String rtnCode;

    /**
     * 构造器
     *
     * @param rtnCode     错误码
     * @param messageText 转义后的消息，例如：`NotFound NickName: {0}` 此处`messageText`为：`NotFound NickName: Alex`
     */
    public OpenException(String rtnCode, String messageText) {
        super(messageText);
        this.rtnCode = rtnCode;
    }

    /**
     * 构造器
     *
     * @param messageType 错误类型
     * @param arguments   占位符参数列表
     */
    public OpenException(IMessageType<String> messageType, Object... arguments) {
        this(messageType.getRtnCode(), getBundleMessageText(messageType, arguments));
    }

    /**
     * 获取 Resource Message Text
     * <p>
     * 优先获取Web项目中自定义的'message'资源文件，找不到后再查询此jar包中的'lambda_message'资源文件
     * </p>
     */
    public static String getBundleMessageText(IMessageType<String> message, Object... arguments) {
        ResourceBundle bundle;
        try {
            bundle = getResourceBundle(message);
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }

        String messageText = bundle.getString(message.getType());
        if (null != arguments && arguments.length > 0) {
            messageText = MessageFormat.format(messageText, arguments);
        }
        return messageText;
    }

    /**
     * 获取 Resource Bundle
     * <p>
     * 优先获取Web项目中自定义的'message'资源文件，找不到后再查询此jar包中的'lambda_message'资源文件
     * </p>
     */
    private static ResourceBundle getResourceBundle(IMessageType<String> message) {
        ResourceBundle bundle;
        Locale messageLocale = MessageLocaleContextHolder.getLocale();
        if (null == messageLocale) {
            messageLocale = Locale.SIMPLIFIED_CHINESE;
        }
        boolean isSimplifiedChinese = messageLocale == Locale.SIMPLIFIED_CHINESE;
        String baseName = isSimplifiedChinese ? "message" : "message_" + messageLocale.toString();
        try {
            bundle = ResourceBundle.getBundle(baseName, messageLocale);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
        ResourceBundle lambdaBundle;
        boolean isMatch = bundle.containsKey(message.getType());
        if (!isMatch) {
            // Web项目中未找到自定义的Key，使用jar包内的配置
            baseName = isSimplifiedChinese ? "message" : "message_" + messageLocale.toString();
            try (InputStream stream = OpenException.class.getClassLoader().getResourceAsStream(baseName + ".properties");
                 InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(stream), StandardCharsets.UTF_8)) {
                lambdaBundle = new PropertyResourceBundle(reader);
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getLocalizedMessage());
            }
            isMatch = lambdaBundle.containsKey(message.getType());
            if (!isMatch) {
                throw new IllegalArgumentException("Not Found Key: " + message.getType());
            }
            bundle = lambdaBundle;
        }
        return bundle;
    }
}