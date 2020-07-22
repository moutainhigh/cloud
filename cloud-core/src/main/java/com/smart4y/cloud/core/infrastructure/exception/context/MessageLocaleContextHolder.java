package com.smart4y.cloud.core.infrastructure.exception.context;

import java.util.Locale;

/**
 * @author Youtao
 * Created by Youtao on 2020/3/18 19:02
 */
public final class MessageLocaleContextHolder {

    private static final ThreadLocal<Locale> THREAD_LOCAL = new ThreadLocal<>();

    public static void setLocale(Locale locale) {
        THREAD_LOCAL.set(locale);
    }

    public static Locale getLocale() {
        return THREAD_LOCAL.get();
    }

    public static void removeLocale() {
        THREAD_LOCAL.remove();
    }
}